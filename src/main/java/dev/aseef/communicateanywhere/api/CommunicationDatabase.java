package dev.aseef.communicateanywhere.api;

import dev.aseef.communicateanywhere.common.AbstractMessager;
import dev.aseef.communicateanywhere.common.jdbc.H2Messager;
import dev.aseef.communicateanywhere.common.jdbc.MongoMessager;
import dev.aseef.communicateanywhere.common.jdbc.PostgreMessager;
import dev.aseef.communicateanywhere.common.jdbc.SQLMessager;
import dev.aseef.communicateanywhere.common.pluginmsg.PluginMessager;
import dev.aseef.communicateanywhere.common.redis.RedisMessager;
import dev.aseef.communicateanywhere.common.sockets.SocketMessager;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public enum CommunicationDatabase {

    /**
     * For using Redis as the intended communication database.
     */
    REDIS(RedisMessager.class),
    /**
     * For using MySQL/MariaDB as the intended communication database.
     */
    MYSQL(SQLMessager.class),
    /**
     * For using H2 as the intended communication database.
     */
    H2(H2Messager.class),
    /**
     * For using MongoDB as the intended communication database.
     */
    MONGODB(MongoMessager.class),
    /**
     * For using Postgre as the intended communication database.
     */
    PostgreSQL(PostgreMessager.class),
    /**
     * For using the Java the networking API to communicate data.
     *
     * Using a socket-based communication system can be a serious
     * security hazard especially if you do not have a proper
     * firewall setup. Use with caution.
     */
    SOCKETS(SocketMessager.class),
    /**
     * For using proxy-based plugin messaging as the intended communication database.
     * Only intended for spigot/bungee minecraft servers.
     */
    PLUGIN_MESSAGING(PluginMessager.class),

    ;

    @Getter
    @Nonnull
    private final Class<? extends AbstractMessager> clazz;

    CommunicationDatabase(@NotNull Class<? extends AbstractMessager> clazz) {
        this.clazz = clazz;
    }

    public void init() {
        this.getClazz().
    }



}
