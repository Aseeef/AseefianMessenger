package dev.aseef.communicateanywhere.common.jdbc;

import dev.aseef.communicateanywhere.api.MessengerType;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import org.jetbrains.annotations.NotNull;

public class SQLMessenger extends JDBCMessenger {

    public SQLMessenger(@NotNull DatabaseCredential credential, long listenerKeepAliveTime, long replyTimeout) {
        super(MessengerType.MYSQL, "mysql", "com.mysql.cj.jdbc.Driver", credential, listenerKeepAliveTime, replyTimeout);
    }

    @Override
    public void onAddedChannel(String channel) {

    }

    @Override
    public void onRemovedChannel(String channel) {

    }

}
