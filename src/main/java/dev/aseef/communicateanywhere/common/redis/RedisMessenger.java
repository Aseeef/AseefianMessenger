package dev.aseef.communicateanywhere.common.redis;

import dev.aseef.communicateanywhere.common.AbstractMessenger;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;

import java.util.concurrent.CompletableFuture;

public class RedisMessenger extends AbstractMessenger {

    public RedisMessenger(DatabaseCredential credential) {
        super(credential);
    }

    @Override
    public void onAddedChannel(String channel) {

    }

    @Override
    public void onRemovedChannel(String channel) {

    }

    @Override
    public CompletableFuture<MessageObject> message(String channel, MessageObject mo) {
        return null;
    }

}
