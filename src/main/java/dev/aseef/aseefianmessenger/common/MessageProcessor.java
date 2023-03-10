package dev.aseef.aseefianmessenger.common;

import dev.aseef.aseefianmessenger.api.CAListener;
import lombok.Getter;

import java.util.Arrays;
import java.util.concurrent.*;

public class MessageProcessor {

    @Getter
    private final ExecutorService executorService;
    private AbstractMessenger messenger;
    private long replyTimeout;

    public MessageProcessor(AbstractMessenger messenger, long keepAliveTime, long replyTimeout) {
        this.messenger = messenger;
        ThreadFactory threadFactory = new CAThreadFactory(AbstractMessenger.CA_LISTENER_THREAD);
        executorService = new ThreadPoolExecutor(1, Integer.MAX_VALUE,
                keepAliveTime, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(), threadFactory);
        this.replyTimeout = replyTimeout;
    }

    public void processIncomingMessage(long messageId, String messengerId, String channel, MessageObject messageObject) {
        if (messageObject.isReply()) {
            messenger.getPendingRepliesMap().get(messageObject.getReplyFor()).complete(messageObject);
            return;
        }
        for (CAListener listener : messenger.getListeners()) {
            if (Arrays.asList(listener.getListeningChannels()).contains(channel)) {
                executorService.submit(() -> {
                    CAReply reply = (replyData) -> {
                        replyData.reply = true;
                        replyData.replyFor = messageId;
                        messenger.message(channel, replyData);
                    };
                    listener.onMessage(messengerId, channel, reply, messageObject);
                });
            }
        }
    }

}
