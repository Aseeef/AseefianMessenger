package dev.aseef.communicateanywhere.common.jdbc;

import dev.aseef.communicateanywhere.api.CommunicationDatabase;
import dev.aseef.communicateanywhere.api.Reply;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import org.jetbrains.annotations.NotNull;

public class PostgreMessager extends JDBCMessager {

    public PostgreMessager(@NotNull DatabaseCredential credential) {
        super(CommunicationDatabase.PostgreSQL, "postgresql", "org.postgresql.Driver", credential);
    }

    @Override
    public void onAddedNewChannel(String channel) {

    }

    @Override
    public Reply message(String channel, MessageObject mo) {
        return null;
    }
}
