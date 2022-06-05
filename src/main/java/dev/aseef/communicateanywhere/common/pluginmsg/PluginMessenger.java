package dev.aseef.communicateanywhere.common.pluginmsg;

import dev.aseef.communicateanywhere.api.CAMessenger;
import dev.aseef.communicateanywhere.common.AbstractMessenger;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import dev.aseef.communicateanywhere.minecraft.bungee.CABungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.CompletableFuture;

public class PluginMessenger extends AbstractMessenger {


    public PluginMessenger(DatabaseCredential credential) {
        super(credential);

    }

    @Override
    public void onAddedChannel(String channel) {

    }

    @Override
    public void onRemovedChannel(String channel) {

    }

    @Override
    public CompletableFuture<MessageObject> message(String channel, MessageObject mo) {
        ProxiedPlayer carrier = CABungee.getInstance().getProxy().getPlayers().stream().findAny().orElse(null);
        if (carrier == null) {
            AbstractMessenger.getLogger().warn("Unable to send message '" + mo.toBson() + "' on channel=" + channel + " because there are no players online to carry the message!");
        }
        return null;
    }

}
