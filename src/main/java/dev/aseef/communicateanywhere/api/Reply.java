package dev.aseef.communicateanywhere.api;

import dev.aseef.communicateanywhere.common.MessageObject;

public interface Reply {

    void onReply(MessageObject replyObject);

}
