package dev.aseef.communicateanywhere.common.sockets;

import dev.aseef.communicateanywhere.api.Reply;
import dev.aseef.communicateanywhere.common.AbstractMessager;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;

public class SocketMessager extends AbstractMessager {


    public SocketMessager(DatabaseCredential credential) {
        super(credential);
    }

    @Override
    public void onAddedNewChannel(String channel) {

    }

    @Override
    public Reply message(String channel, MessageObject mo) {
        return null;
    }

}
