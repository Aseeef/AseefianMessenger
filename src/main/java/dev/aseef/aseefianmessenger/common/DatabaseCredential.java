package dev.aseef.aseefianmessenger.common;

import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Getter
public class DatabaseCredential {

    @Nonnull String hostName;
    @Nullable String username = null;
    @Nullable String password = null;
    int port = -1;
    @Nullable String database = null;

    public DatabaseCredential(@Nonnull String hostName) {
        this.hostName = hostName;
    }

    public DatabaseCredential(@Nonnull String hostName, @Nonnull String password, int port) {
        this.hostName = hostName;
        this.password = password;
        this.port = port;
    }

    public DatabaseCredential(@Nonnull String hostName, @Nonnull String username, @Nonnull String password, int port) {
        this.hostName = hostName;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public DatabaseCredential(@Nonnull String hostName, @Nonnull String username, @Nonnull String password, int port, @Nonnull String database) {
        this.hostName = hostName;
        this.username = username;
        this.password = password;
        this.port = port;
        this.database = database;
    }

}
