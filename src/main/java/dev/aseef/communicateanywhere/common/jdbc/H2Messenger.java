package dev.aseef.communicateanywhere.common.jdbc;

import dev.aseef.communicateanywhere.api.CAMessenger;
import dev.aseef.communicateanywhere.api.MessengerType;
import dev.aseef.communicateanywhere.common.AbstractMessenger;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class H2Messenger extends JDBCMessenger {

    public H2Messenger(@NotNull DatabaseCredential credential) {
        super(MessengerType.H2, "h2", "org.h2.Driver", credential);
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
    public void onAddedChannel(String channel) {

    }

    @Override
    public void onRemovedChannel(String channel) {

    }

    @Override
    public CompletableFuture<MessageObject> message(String channel, MessageObject mo) {
        return null;
    }

}
