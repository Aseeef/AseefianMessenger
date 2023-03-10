package dev.aseef.aseefianmessenger.common.redis;

import dev.aseef.aseefianmessenger.common.AbstractMessenger;
import dev.aseef.aseefianmessenger.common.DatabaseCredential;
import dev.aseef.aseefianmessenger.common.MessageObject;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class RedisMessenger extends AbstractMessenger {

    public RedisMessenger(@NotNull DatabaseCredential credential, long listenerThreadsKeepAliveTime, long replyTimeout, double compressionThreshold, long maxPersist) {
        super(credential, listenerThreadsKeepAliveTime, replyTimeout, compressionThreshold, maxPersist);
    }

    @Override
    public CompletableFuture<MessageObject> message(String channel, MessageObject mo) {
        return null;
    }

    @Override
    public Runnable getPollingProtocol() {
        return null;
    }

}
