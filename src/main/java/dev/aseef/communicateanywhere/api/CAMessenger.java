package dev.aseef.communicateanywhere.api;

import dev.aseef.communicateanywhere.common.MessageObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public interface CAMessenger {

    CompletableFuture<MessageObject> message(String channel, MessageObject mo);

    String getMessengerId();

    List<CAListener> getListeners();

    CAMessenger addListener(CAListener listener);

    CAMessenger removeListener(CAListener listener);

    default Set<String> getAllListeningChannels() {
        HashSet<String> channels = new HashSet<>();
        for (CAListener listener : getListeners()) {
            channels.addAll(Arrays.asList(listener.getListeningChannels()));
        }
        return channels;
    }

}
