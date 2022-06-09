package dev.aseef.communicateanywhere.common.mongodb;

import dev.aseef.communicateanywhere.common.AbstractMessenger;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import org.bson.BsonDocument;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MongoMessenger extends AbstractMessenger {


    public MongoMessenger(@NotNull DatabaseCredential credential, double compressionThreshold,  long maxPersist) {
        super(credential, 0l, 0l, compressionThreshold, maxPersist);
    }

    @Override
    public CompletableFuture<MessageObject> message(String channel, MessageObject mo) {
        BsonDocument doc = mo.toBson();

        return null;
    }

    @Override
    public Runnable getPollingProtocol() {
        return null;
    }

}
