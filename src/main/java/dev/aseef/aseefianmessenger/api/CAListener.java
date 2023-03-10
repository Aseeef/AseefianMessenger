package dev.aseef.aseefianmessenger.api;

import dev.aseef.aseefianmessenger.common.CAReply;
import dev.aseef.aseefianmessenger.common.MessageObject;

public interface CAListener {

    default int getListenerPriority() {
        return 0;
    }

    String[] getListeningChannels();

    void onMessage(String messengerId, String channel, CAReply callbackReply, MessageObject object);

}
