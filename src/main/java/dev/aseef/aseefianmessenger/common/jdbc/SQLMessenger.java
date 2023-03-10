package dev.aseef.aseefianmessenger.common.jdbc;

import dev.aseef.aseefianmessenger.api.MessengerType;
import dev.aseef.aseefianmessenger.common.DatabaseCredential;
import org.jetbrains.annotations.NotNull;

public class SQLMessenger extends JDBCMessenger {

    public SQLMessenger(@NotNull DatabaseCredential credential, long listenerKeepAliveTime, long replyTimeout, double compressionThreshold, long maxPersist) {
        super(MessengerType.MYSQL, "mysql", "com.mysql.cj.jdbc.Driver", credential, listenerKeepAliveTime, replyTimeout, compressionThreshold, maxPersist);
    }

}
