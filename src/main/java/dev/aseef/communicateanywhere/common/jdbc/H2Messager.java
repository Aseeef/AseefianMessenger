package dev.aseef.communicateanywhere.common.jdbc;

import dev.aseef.communicateanywhere.api.CommunicationDatabase;
import dev.aseef.communicateanywhere.api.Reply;
import dev.aseef.communicateanywhere.common.CAManager;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class H2Messager extends JDBCMessager {

    public H2Messager(@NotNull DatabaseCredential credential) {
        super(CommunicationDatabase.H2, "h2", "org.h2.Driver", credential);
        String filePath = credential.getHostName();
        File file = new File(filePath);
        if (file.isDirectory()) {
            throw new IllegalArgumentException("The file path cannot be a directory (it must be a file)!");
        }
        if (file.exists()) {
            CAManager.getLogger().warn("No file exists at path=" + file.getAbsolutePath() + "! Creating a new file...");
            try {
                file.mkdirs();
                file.createNewFile();
            } catch (IOException ex) {
                CAManager.getLogger().error("Error creating a new database file!", ex);
            }
        }
    }

    @Override
    public void onAddedNewChannel(String channel) {

    }

    @Override
    public Reply message(String channel, MessageObject mo) {
        return null;
    }

}
