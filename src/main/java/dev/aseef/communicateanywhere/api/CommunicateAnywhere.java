package dev.aseef.communicateanywhere.api;

import dev.aseef.communicateanywhere.common.MessageObject;

public interface CommunicateAnywhere {

    Reply message(String channel, MessageObject mo);

}
