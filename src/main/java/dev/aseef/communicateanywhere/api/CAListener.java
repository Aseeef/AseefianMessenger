package dev.aseef.communicateanywhere.api;

import dev.aseef.communicateanywhere.common.MessageObject;

public interface CAListener {

    String[] getListeningChannels();

    void onMessage(String channel, Reply callbackReply, MessageObject object);

}
