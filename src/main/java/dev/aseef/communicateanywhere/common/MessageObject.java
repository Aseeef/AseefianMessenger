package dev.aseef.communicateanywhere.common;

import dev.aseef.communicateanywhere.api.CommunicableObject;
import lombok.SneakyThrows;
import org.bson.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.objenesis.ObjenesisSerializer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MessageObject {

    // data strings larger than this many megabytes will be compressed
    private static final int COMPRESSION_THRESHOLD = 25;

    private final long messageId;
    private final Class<?> dataType;
    private byte[] messageData;
    private boolean compressed = false;

    @SneakyThrows
    private MessageObject(Object msg) {
        this.dataType = msg.getClass();
        this.messageId = System.nanoTime();

        if (msg instanceof Byte) {
            byte b = (byte) msg;
            messageData = new byte[] {b};
        }

        else if (msg instanceof Short) {
            short s = (short) msg;
            messageData = ByteBuffer.allocate(2).putShort(s).array();
        }

        else if (msg instanceof Integer) {
            int i = (int) msg;
            messageData = ByteBuffer.allocate(4).putInt(i).array();
        }

        else if (msg instanceof Long) {
            long l = (long) msg;
            messageData = ByteBuffer.allocate(8).putLong(l).array();
        }

        else if (msg instanceof Float) {
            float f = (float) msg;
            messageData = ByteBuffer.allocate(4).putFloat(f).array();
        }

        else if (msg instanceof Double) {
            double d = (double) msg;
            messageData = ByteBuffer.allocate(8).putDouble(d).array();
        }

        else if (msg instanceof Boolean) {
            boolean b = (boolean) msg;
            messageData = b ? new byte[] {1} : new byte[]{0};
        }

        else if (msg instanceof File) {
            File file = (File) msg;
            if (!file.exists())
                throw new IllegalArgumentException("No file exists in " + file.getAbsolutePath() + "!");
            // create a byte output stream
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            // init the file input
            FileInputStream fis = new FileInputStream(file);
            // if file size larger than threshold, compress
            if (file.length() > COMPRESSION_THRESHOLD * 1000000) {
                GZIPOutputStream gzip = new GZIPOutputStream(output);
                int read;
                while ((read = fis.read()) != -1) {
                    gzip.write(read);
                }
                compressed = true;
            } else {
                int read;
                while ((read = fis.read()) != -1) {
                    output.write(read);
                }
            }
            messageData = output.toByteArray();
        }

        else if (msg instanceof String || msg instanceof CommunicableObject || msg instanceof BsonDocument || msg instanceof BsonArray) {
            String stringData = msg instanceof CommunicableObject ? ((CommunicableObject) msg).serialize() : msg.toString();
            messageData = stringData.getBytes(StandardCharsets.UTF_8);
            if (messageData.length > COMPRESSION_THRESHOLD * 1000000) {
                ByteArrayInputStream input = new ByteArrayInputStream(messageData);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                GZIPOutputStream gzip = new GZIPOutputStream(output);
                int read;
                while ((read = input.read()) != -1) {
                    gzip.write(read);
                }
                messageData = output.toByteArray();
            }
        }

        else throw new IllegalArgumentException("Unknown datatype. Don't know how to convert into a MessageObject!");

    }

    @SneakyThrows
    private MessageObject(byte[] msg) {
        this.dataType = msg.getClass();
        this.messageId = System.nanoTime();
        this.messageData = msg;
        if (messageData.length > COMPRESSION_THRESHOLD * 1000000) {
            InputStream stream = new GZIPInputStream(new ByteArrayInputStream(messageData));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read;
            while ((read = stream.read()) != -1) {
                baos.write(read);
            }
            messageData = baos.toByteArray();
        }
    }

    protected MessageObject(long messageId, Class<?> dataType, byte[] messageData, boolean compressed) {
        this.messageId = messageId;
        this.dataType = dataType;
        this.messageData = messageData;
        this.compressed = compressed;
    }

    public Class<?> getDataType() {
        return this.dataType;
    }

    public long getMessageId() {
        return messageId;
    }

    public byte[] getMessageData() {
        return messageData;
    }

    public boolean isCompressed() {
        return compressed;
    }

    protected static MessageObject receivedDataFromBson (BsonDocument document) throws ClassNotFoundException {
        long messageId = document.getInt64("message-id").longValue();
        Class<?> dataType = Class.forName(document.getString("data-type").getValue());
        boolean compressed = document.getBoolean("compressed").getValue();
        byte[] messageData = document.getBinary("data").getData();
        return new MessageObject(messageId, dataType, messageData, compressed);
    }

    // since json is not binary safe, we use bson to transmit data in key value pairs
    protected BsonDocument toBson() {
        return new BsonDocument()
                .append("message-id", new BsonInt64(this.messageId))
                .append("data-type", new BsonString(this.dataType.getName()))
                .append("compressed", new BsonBoolean(this.compressed))
                .append("data", new BsonBinary(this.messageData));
    }

    @SneakyThrows
    public Object getData() {
        if (dataType == Byte.class) {
            return ByteBuffer.wrap(this.messageData).get();
        }
        else if (dataType == Short.class) {
            return ByteBuffer.wrap(this.messageData).getShort();
        }
        else if (dataType == Integer.class) {
            return ByteBuffer.wrap(this.messageData).getInt();
        }
        else if (dataType == Long.class) {
            return ByteBuffer.wrap(this.messageData).getLong();
        }
        else if (dataType == Float.class) {
            return ByteBuffer.wrap(this.messageData).getFloat();
        }
        else if (dataType == Double.class) {
            return ByteBuffer.wrap(this.messageData).getDouble();
        }
        else if (dataType == Boolean.class) {
            return ByteBuffer.wrap(this.messageData).get() == 1;
        }
        else if (dataType == File.class) {
            InputStream stream = new ByteArrayInputStream(this.messageData);
            if (this.compressed) {
                stream = new GZIPInputStream(stream);
            }
            return stream;
        }
        else if (dataType == String.class || dataType == CommunicableObject.class ||
                dataType == JSONObject.class || dataType == JSONArray.class) {
            String string;
            if (compressed) {
                ByteArrayInputStream input = new ByteArrayInputStream(this.messageData);
                GZIPInputStream gzip = new GZIPInputStream(input);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                int read;
                while ((read = gzip.read()) != -1)
                    output.write(read);
                string = new String(output.toByteArray(), StandardCharsets.UTF_8);
            } else {
                string = new String(messageData);
            }
            return string;
        }

        else throw new IllegalArgumentException("Unknown datatype. Don't know how to deserialize this object!");
    }

    public String getAsString() {
        return getData().toString();
    }

    @SuppressWarnings("unchecked")
    public <T extends CommunicableObject> T getAsCommunicableObject(Class<T> clazz) {
        T obj = new ObjenesisSerializer().newInstance(clazz);
        return (T) obj.deserialize(getAsString());
    }

    public File getAsWrittenFile(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        InputStream stream = (InputStream) getData();
        int read;
        while ((read = stream.read()) != -1)
            fos.write(read);
        return file;
    }

    public BsonDocument getAsBsonDocument() {
        return BsonDocument.parse(getAsString());
    }

    public BsonArray getAsBsonArray() {
        return BsonArray.parse(getAsString());
    }

    public byte getAsByte() {
        return (byte) getData();
    }

    public short getAsShort() {
        return (short) getData();
    }

    public int getAsInteger() {
        return (int) getData();
    }

    public long getAsLong() {
        return (long) getData();
    }

    public float getAsFloat() {
        return (float) getData();
    }

    public double getAsDouble() {
        return (double) getData();
    }

    public boolean getAsBoolean() {
        return (boolean) getData();
    }
    
    public static MessageObject from(byte msg) {
        return new MessageObject(msg);
    }

    public static MessageObject from(short msg) {
        return new MessageObject(msg);
    }

    public static MessageObject from(int msg) {
        return new MessageObject(msg);
    }

    public static MessageObject from(long msg) {
        return new MessageObject(msg);
    }

    public static MessageObject from(float msg) {
        return new MessageObject(msg);
    }

    public static MessageObject from(double msg) {
        return new MessageObject(msg);
    }

    public static MessageObject from(boolean msg) {
        return new MessageObject(msg);
    }

    public static MessageObject from(File msg) {
        return new MessageObject(msg);
    }

    public static MessageObject from(String msg) {
        return new MessageObject(msg);
    }

    public static MessageObject from(CommunicableObject communicableObject) {
        return new MessageObject(communicableObject);
    }

    public static MessageObject from(BsonDocument jo) {
        return new MessageObject(jo);
    }

    public static MessageObject from(BsonArray ja) {
        return new MessageObject(ja);
    }

    public static MessageObject from(byte[] bytes) {
        return new MessageObject(bytes);
    }
    
}
