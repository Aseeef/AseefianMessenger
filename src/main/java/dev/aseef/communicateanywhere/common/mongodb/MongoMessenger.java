package dev.aseef.communicateanywhere.common.mongodb;

import com.mongodb.client.MongoClients;
import dev.aseef.communicateanywhere.api.CAMessenger;
import dev.aseef.communicateanywhere.api.MessengerType;
import dev.aseef.communicateanywhere.common.AbstractMessenger;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import dev.aseef.communicateanywhere.common.jdbc.JDBCMessenger;
import org.bson.BsonDocument;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class MongoMessenger extends JDBCMessenger {


    public MongoMessenger(@NotNull DatabaseCredential credential) {
        super(MessengerType.MONGODB, "mongodb", "com.dbschema.MongoJdbcDriver", credential, 0l, 0l);
    }

    @Override
    public void onAddedChannel(String channel) {
        MongoClients.create();
    }

    @Override
    public void onRemovedChannel(String channel) {

    }

    @Override
    public CompletableFuture<MessageObject> message(String channel, MessageObject mo) {
        BsonDocument doc = mo.toBson();

        try (Connection conn = this.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            AbstractMessenger.getLogger().error("An error occurred sending message '" + doc.toString() + "'", ex);
        }

        return null;
    }

}
