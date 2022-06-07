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
            PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM ca_messages WHERE creation > ? ORDER BY creation ASC;");
            while (true) {
                // poll new messages for the listening channels
                ps1.setLong(1, lastAcknowledgedMessage);
                try {
                    ResultSet rs = ps1.executeQuery();
                    while (rs.next()) {
                        String channel = rs.getString("channel");

                        // no point in wasting resources for a channel we aren't even listening to
                        if (!this.messenger.getAllListeningChannels().contains(channel))
                            continue;

                        long messageId = rs.getLong("message_id");
                        String senderId = rs.getString("sender_id");
                        String data = rs.getString("message_blob");
                        lastAcknowledgedMessage = rs.getLong("creation");
                        MessageObject mo = this.messenger.bsonToMessageObject(BsonDocument.parse(data));
                        this.messenger.getMessageProcessor().processReply(messageId, senderId, channel, mo);
                    }
                    rs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
