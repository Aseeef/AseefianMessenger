package dev.aseef.communicateanywhere.api;

import dev.aseef.communicateanywhere.common.AbstractMessenger;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.jdbc.H2Messenger;
import dev.aseef.communicateanywhere.common.mongodb.MongoMessenger;
import dev.aseef.communicateanywhere.common.jdbc.PostgreMessenger;
import dev.aseef.communicateanywhere.common.jdbc.SQLMessenger;
import dev.aseef.communicateanywhere.common.redis.RedisMessenger;
import dev.aseef.communicateanywhere.common.sockets.SocketMessenger;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;

public enum MessengerType {

    /**
     * For using Redis as the intended communication database.
     */
    REDIS(RedisMessenger.class),
    /**
     * For using MySQL/MariaDB as the intended communication database.
     */
    MYSQL(SQLMessenger.class),
    /**
     * For using H2 as the intended communication database.
     */
    H2(H2Messenger.class),
    /**
     * For using MongoDB as the intended communication database.
     */
    MONGODB(MongoMessenger.class),
    /**
     * For using Postgre as the intended communication database.
     */
    PostgreSQL(PostgreMessenger.class),
    /**
     * For using the Java the networking API to communicate data.
     *
     * Using a socket-based communication system can be a serious
     * security hazard especially if you do not have a proper
     * firewall setup. Use with caution.
     */
    SOCKETS(SocketMessenger.class),

    ;

    @Getter
    @Nonnull
    private final Class<? extends AbstractMessenger> clazz;

    MessengerType(@NotNull Class<? extends AbstractMessenger> clazz) {
        this.clazz = clazz;
    }

    public MessengerBuilder builder() {
        return new MessengerBuilder(this);
    }




}
