package dev.aseef.communicateanywhere.common;

import dev.aseef.communicateanywhere.api.CAMessenger;
import dev.aseef.communicateanywhere.api.CAListener;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BsonDocument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractMessenger implements CAMessenger {

    @Getter
    private static final Logger logger = LogManager.getLogger(AbstractMessenger.class.getSimpleName());

    public static final String CA_POLLING_THREAD = "CA-POLL-THREAD";
    public static final String CA_LISTENER_THREAD = "CA-LISTENER-THREAD-#%d";

    private final DatabaseCredential credential;
    /**
     * A unique ID that is unique per JVM instance
     */
    private final UUID messengerId;
    private final List<CAListener> listeners = new LinkedList<>();
    @Getter
    private final HashMap<Long,CompletableFuture<MessageObject>> pendingRepliesMap = new HashMap<>();
    @Nullable
    private Thread pollingThread = null;
    @Getter
    private final MessageProcessor messageProcessor;

    public AbstractMessenger(@Nonnull DatabaseCredential credential, long listenerThreadsKeepAliveTime, long replyTimeout) {
        this.credential = credential;
        this.messengerId = UUID.randomUUID();
        this.messageProcessor = new MessageProcessor(this, listenerThreadsKeepAliveTime, replyTimeout);
    }

    public void initPolling() {
        Thread thread = new Thread(getPollingProtocol());
        thread.setDaemon(false);
        thread.setName(CA_POLLING_THREAD);
        this.setPollingThread(thread);
        thread.start();
    }

    public AbstractMessenger addListener(CAListener listener) {
        Set<String> currentChannels = getAllListeningChannels();
        // if a new channel was registered as a result of this
        // call, then call the onAddedNewChannel method.
        // (its possible that a new listener can be registered but
        // it's listening to the same channels as another
        // listener)
        for (String newChannel : listener.getListeningChannels()) {
            if (!currentChannels.contains(newChannel))
                onAddedChannel(newChannel);
        }
        listeners.add(listener);
        return this;
    }

    public AbstractMessenger removeListener(CAListener listener) {
        //todo
        return this;
    }

    @Override
    public UUID getMessengerId() {
        return this.messengerId;
    }

    @Override
    public List<CAListener> getListeners() {
        return this.listeners;
    }

    public DatabaseCredential getCredential() {
        return credential;
    }

    @Nullable
    public Thread getPollingThread() {
        return pollingThread;
    }

    public void setPollingThread(@Nullable Thread pollingThread) {
        this.pollingThread = pollingThread;
    }

    public MessageObject bsonToMessageObject(BsonDocument bsonDocument) throws ClassNotFoundException {
        return MessageObject.receivedDataFromBson(bsonDocument);
    }

    public abstract void onAddedChannel(String channel);

    public abstract void onRemovedChannel(String channel);

    public abstract CompletableFuture<MessageObject> message(String channel, MessageObject mo);

    public abstract Runnable getPollingProtocol();
}
