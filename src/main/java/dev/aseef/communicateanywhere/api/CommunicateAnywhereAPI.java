package dev.aseef.communicateanywhere.api;

import dev.aseef.communicateanywhere.common.MessageObject;

public interface CommunicateAnywhereAPI {

    Reply message(String channel, MessageObject mo);

}
