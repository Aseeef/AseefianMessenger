package dev.aseef.aseefianmessenger.common.jdbc;

import dev.aseef.aseefianmessenger.api.MessengerType;
import dev.aseef.aseefianmessenger.common.AbstractMessenger;
import dev.aseef.aseefianmessenger.common.DatabaseCredential;
import dev.aseef.aseefianmessenger.common.MessageObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class H2Messenger extends JDBCMessenger {

    public H2Messenger(@NotNull DatabaseCredential credential, long listenerKeepAliveTime, long replyTimeout, double compressionThreshold, long maxPersist) {
        super(MessengerType.H2, "h2", "org.h2.Driver", credential, listenerKeepAliveTime, replyTimeout, compressionThreshold, maxPersist);
        String filePath = credential.getHostName();
        File file = new File(filePath);
        if (file.isDirectory()) {
            throw new IllegalArgumentException("The file path cannot be a directory (it must be a file)!");
        }
        if (file.exists()) {
            AbstractMessenger.getLogger().warn("No file exists at path=" + file.getAbsolutePath() + "! Creating a new file...");
            try {
                file.mkdirs();
                file.createNewFile();
            } catch (IOException ex) {
                AbstractMessenger.getLogger().error("Error creating a new database file!", ex);
            }
        }
        try {
            this.init();
        } catch (SQLException ex) {
            AbstractMessenger.getLogger().error("An error occurred initialing H2Messenger", ex);
        }
    }

    @Override
    public CompletableFuture<MessageObject> message(String channel, MessageObject mo) {
        return null;
    }

}
