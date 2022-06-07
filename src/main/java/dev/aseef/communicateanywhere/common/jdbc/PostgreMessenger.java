package dev.aseef.communicateanywhere.common.jdbc;

import dev.aseef.communicateanywhere.api.MessengerType;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import org.jetbrains.annotations.NotNull;

public class PostgreMessenger extends JDBCMessenger {

    public PostgreMessenger(@NotNull DatabaseCredential credential, long listenerKeepAliveTime, long replyTimeout) {
        super(MessengerType.PostgreSQL, "postgresql", "org.postgresql.Driver", credential, listenerKeepAliveTime, replyTimeout);
    }

    @Override
    public void onAddedChannel(String channel) {

    }

    @Override
    public void onRemovedChannel(String channel) {

    }

}
