package dev.aseef.communicateanywhere.common.jdbc;

import dev.aseef.communicateanywhere.api.CommunicationDatabase;
import dev.aseef.communicateanywhere.api.Reply;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import org.jetbrains.annotations.NotNull;

public class SQLMessager extends JDBCMessager {

    public SQLMessager(@NotNull DatabaseCredential credential) {
        super(CommunicationDatabase.MYSQL, "mysql", "com.mysql.jdbc.Driver", credential);
    }

    @Override
    public void onAddedNewChannel(String channel) {

    }

    @Override
    public Reply message(String channel, MessageObject mo) {
        return null;
    }

}
