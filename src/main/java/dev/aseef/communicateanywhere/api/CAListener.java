package dev.aseef.communicateanywhere.api;

import dev.aseef.communicateanywhere.common.CAReply;
import dev.aseef.communicateanywhere.common.MessageObject;

public interface CAListener {

    default int getListenerPriority() {
        return 0;
    }

    String[] getListeningChannels();

    void onMessage(String messengerId, String channel, CAReply callbackReply, MessageObject object);

}
