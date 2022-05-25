package dev.aseef.communicateanywhere.common.redis;

import dev.aseef.communicateanywhere.api.Reply;
import dev.aseef.communicateanywhere.common.AbstractMessager;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;

public class RedisMessager extends AbstractMessager {

    public RedisMessager(DatabaseCredential credential) {
        super(credential);
    }

    @Override
    public Reply message(String channel, MessageObject mo) {
        return null;
    }
}
