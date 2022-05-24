package dev.aseef.communicateanywhere.api;

import dev.aseef.communicateanywhere.common.MessageObject;

public interface CommunicateAnywhereListener {

    void onMessage(String channel, Reply callbackReply, MessageObject object);

}
