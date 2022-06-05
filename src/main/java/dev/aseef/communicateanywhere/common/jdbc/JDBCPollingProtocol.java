package dev.aseef.communicateanywhere.common.jdbc;

import dev.aseef.communicateanywhere.common.MessageObject;
import org.bson.BsonDocument;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCPollingProtocol implements Runnable {

    private final JDBCMessenger messenger;
    private long startTime = -1;
    private long lastAcknowledgedMessage = -1;

    public JDBCPollingProtocol(JDBCMessenger messenger) throws SQLException {
        this.messenger = messenger;
        // we first begin by getting the database time so we can sync to it
        try (Connection conn = messenger.getConnection()) {
            try (PreparedStatement ps1 = conn.prepareStatement("SELECT ROUND(UNIX_TIMESTAMP(CURTIME(4))*1000) as 'time';")) {
                try (ResultSet rs = ps1.executeQuery()) {
                    if (rs.next()) {
                        startTime = rs.getLong("time");
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        try (Connection conn = messenger.getConnection()) {
            PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM ca_messages WHERE channel IN ? AND creation > ? ORDER BY message_id ASC;");
            while (true) {
                ps2.setArray(1, conn.createArrayOf("String", messenger.getAllListeningChannels().toArray(new String[0])));
                ps2.setLong(2, lastAcknowledgedMessage);
                try {
                    ResultSet rs = ps2.executeQuery();
                    while (rs.next()) {
                        String senderId = rs.getString("sender_id");
                        String data = rs.getString("message_blob");
                        String channel = rs.getString("channel");
                        MessageObject mo = this.messenger.bsonToMessageObject(BsonDocument.parse(data));
                        this.messenger.getMessageProcessor().process(senderId, channel, mo);
                    }
                    rs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ps2.close();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
