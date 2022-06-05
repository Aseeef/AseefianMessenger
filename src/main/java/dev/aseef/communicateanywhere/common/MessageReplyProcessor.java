package dev.aseef.communicateanywhere.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.aseef.communicateanywhere.api.CAListener;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.*;

import static dev.aseef.communicateanywhere.common.AbstractMessenger.CA_POLLING_THREAD;

public class MessageReplyProcessor {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final PriorityQueue<CAListener> queue = new PriorityQueue<>(Comparator.comparingInt(CAListener::getListenerPriority).reversed());

    private AbstractMessenger messenger;
    private long replyTimeout;

    public MessageReplyProcessor(AbstractMessenger messenger, long keepAliveTime, long replyTimeout) {
        this.messenger = messenger;
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(CA_POLLING_THREAD).build();
        new ThreadPoolExecutor(1, Integer.MAX_VALUE,
                keepAliveTime, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(), threadFactory);
        this.replyTimeout = replyTimeout;
    }

    public void process(String senderId, String channel, MessageObject messageObject) {
        if (messageObject.isReply()) {
            messenger.getPendingRepliesMap().get(messageObject.getReplyTo()).complete(messageObject);
            return;
        }
        for (CAListener listener : messenger.getListeners()) {
            if (Arrays.asList(listener.getListeningChannels()).contains(channel)) {
                executorService.submit(() -> {
                    CAReply reply = (replyData, replyTimeout) -> {
                        replyData.reply = true;
                        replyData.replyTo = messageObject.getMessageId();
                        messenger.message(channel, replyData, replyTimeout);
                    };
                    listener.onMessage(senderId, channel, reply, messageObject);
                });
            }
        }
    }

}
