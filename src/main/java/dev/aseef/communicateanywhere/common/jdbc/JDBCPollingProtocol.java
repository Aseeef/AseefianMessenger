package dev.aseef.communicateanywhere.common.jdbc;

import dev.aseef.communicateanywhere.common.MessageObject;
import org.bson.BsonDocument;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCPollingProtocol implements Runnable {

    private final JDBCMessenger messenger;
    private long lastAcknowledgedMessage = -1;

    public JDBCPollingProtocol(JDBCMessenger messenger) throws SQLException {
        this.messenger = messenger;
        // we first begin by getting the database time so we dont get any messages before this time
        try (Connection conn = messenger.getConnection()) {
            try (PreparedStatement ps1 = conn.prepareStatement("SELECT ROUND(UNIX_TIMESTAMP(CURTIME(4))*1000) as 'time';")) {
                try (ResultSet rs = ps1.executeQuery()) {
                    if (rs.next()) {
                        lastAcknowledgedMessage = rs.getLong("time");
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        try (Connection conn = messenger.getConnection()) {
            PreparedStatement ps1 = conn.prepareStatement("SELECT channel, message_id, messenger_id, message_blob, creation FROM ca_messages WHERE creation > ? ORDER BY creation ASC;");
            while (true) {

                ps1.setLong(1, lastAcknowledgedMessage);

                try {
                    long s = System.currentTimeMillis();
                    ResultSet rs = ps1.executeQuery();
                    while (rs.next()) {

                        long messageId = rs.getLong("message_id");
                        String channel = rs.getString("channel");

                        // no point in wasting resources for a channel we aren't even listening to
                        if (!this.messenger.getAllListeningChannels().contains(channel))
                            continue;

                        String senderId = rs.getString("messenger_id");
                        String data = rs.getString("message_blob");
                        lastAcknowledgedMessage = rs.getLong("creation");
                        MessageObject mo = this.messenger.bsonToMessageObject(BsonDocument.parse(data));

                        // process message
                        this.messenger.getMessageProcessor().processIncomingMessage(messageId, senderId, channel, mo);
                    }
                    rs.close();

                    // delete expired messages
                    try (PreparedStatement ps = conn.prepareStatement("DELETE FROM ca_messages WHERE expiry < ROUND(UNIX_TIMESTAMP(CURTIME(4))*1000);")) {
                        s = System.currentTimeMillis();
                        ps.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
