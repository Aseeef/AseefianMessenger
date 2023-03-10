package dev.aseef.aseefianmessenger.api;

import dev.aseef.aseefianmessenger.common.DatabaseCredential;

import java.lang.reflect.InvocationTargetException;

public class MessengerBuilder {

    long listenerThreadsKeepAliveTime = 120;
    private final MessengerType type;
    private DatabaseCredential credential;
    private int replyTimeout = 300;
    private double compressionThreshold = 20d;
    private long maxPersist = 120000L;

    protected MessengerBuilder(MessengerType type) {
        this.type = type;
    }

    public MessengerBuilder setCredentials(DatabaseCredential credential) {
        this.credential = credential;
        return this;
    }

    public MessengerBuilder setThreadKeepAliveTime(int milliseconds) {
        this.listenerThreadsKeepAliveTime = milliseconds;
        return this;
    }

    public MessengerBuilder setReplyTimeout(int milliseconds) {
        this.replyTimeout = milliseconds;
        return this;
    }

    public MessengerBuilder setCompressionThreshold(double megabytes) {
        this.compressionThreshold = megabytes;
        return this;
    }

    public MessengerBuilder setMaxPersistenceTime(long millis) {
        this.maxPersist = millis;
        return this;
    }

    public CAMessenger build() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // settings validations here
        //todo

        switch (this.type) {
            case H2:
            case MYSQL:
            case PostgreSQL:
            case MONGODB:
            case REDIS:
                return this.type.getClazz().getConstructor(DatabaseCredential.class, Long.TYPE, Long.TYPE, Double.TYPE, Long.TYPE).newInstance(credential, this.listenerThreadsKeepAliveTime, this.replyTimeout, this.compressionThreshold, this.maxPersist);
            case SOCKETS:
                return null;
            default:
                throw new NullPointerException();
        }
    }

}
