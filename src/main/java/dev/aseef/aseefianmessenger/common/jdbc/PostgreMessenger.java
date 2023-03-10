package dev.aseef.aseefianmessenger.common.jdbc;

import dev.aseef.aseefianmessenger.api.MessengerType;
import dev.aseef.aseefianmessenger.common.DatabaseCredential;
import org.jetbrains.annotations.NotNull;

public class PostgreMessenger extends JDBCMessenger {

    public PostgreMessenger(@NotNull DatabaseCredential credential, long listenerKeepAliveTime, long replyTimeout, double compressionThreshold, long maxPersist) {
        super(MessengerType.PostgreSQL, "postgresql", "org.postgresql.Driver", credential, listenerKeepAliveTime, replyTimeout, compressionThreshold, maxPersist);
    }

}
