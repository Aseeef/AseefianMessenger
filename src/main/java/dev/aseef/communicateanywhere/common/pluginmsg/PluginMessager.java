package dev.aseef.communicateanywhere.common.pluginmsg;

import dev.aseef.communicateanywhere.api.Reply;
import dev.aseef.communicateanywhere.common.AbstractMessager;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;

public class PluginMessager extends AbstractMessager {


    public PluginMessager(DatabaseCredential credential) {
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
