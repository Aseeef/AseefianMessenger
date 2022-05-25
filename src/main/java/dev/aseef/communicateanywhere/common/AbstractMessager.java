package dev.aseef.communicateanywhere.common;

import dev.aseef.communicateanywhere.api.CAMessenger;
import dev.aseef.communicateanywhere.api.CAListener;
import dev.aseef.communicateanywhere.api.Reply;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class AbstractMessager implements CAMessenger {

    @Getter
    private final DatabaseCredential credential;
    @Getter
    private final UUID identifier;
    @Getter
    private final List<CAListener> listeners = new LinkedList<>();

    public AbstractMessager(@Nonnull DatabaseCredential credential) {
        this.credential = credential;
        this.identifier = UUID.randomUUID();
    }

    public Set<String> getAllListeningChannels() {
        HashSet<String> channels = new HashSet<>();
        for (CAListener listener : listeners) {
            channels.addAll(Arrays.asList(listener.getListeningChannels()));
        }
        return channels;
    }

    public AbstractMessager addListener(CAListener listener) {
        Set<String> currentChannels = getAllListeningChannels();
        // if a new channel was registered as a result of this
        // call, then call the onAddedNewChannel method.
        // (its possible that a new listener can be registered but
        // it's listening to the same channels as another
        // listener)
        for (String newChannel : listener.getListeningChannels()) {
            if (!currentChannels.contains(newChannel))
                onAddedNewChannel(newChannel);
        }
        listeners.add(listener);
        return this;
    }

    public abstract void onAddedNewChannel(String channel);

    public abstract Reply message(String channel, MessageObject mo);

}
