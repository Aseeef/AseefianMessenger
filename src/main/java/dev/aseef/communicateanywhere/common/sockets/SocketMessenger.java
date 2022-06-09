package dev.aseef.communicateanywhere.common.sockets;

import dev.aseef.communicateanywhere.common.AbstractMessenger;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SocketMessenger extends AbstractMessenger {


    public SocketMessenger(@NotNull DatabaseCredential credential, long listenerThreadsKeepAliveTime, long replyTimeout, double compressionThreshold, long maxPersist) {
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
