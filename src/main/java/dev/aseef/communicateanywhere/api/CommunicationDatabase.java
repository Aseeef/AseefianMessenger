package dev.aseef.communicateanywhere.api;

public enum CommunicationDatabase {

    /**
     * For using Redis as the intended communication database.
     */
    REDIS,
    /**
     * For using MySQL/MariaDB as the intended communication database.
     */
    MYSQL,
    /**
     * For using SQLLite as the intended communication database.
     */
    SQLLite,
    /**
     * For using MongoDB as the intended communication database.
     */
    MONGODB,
    /**
     * For using Postgre as the intended communication database.
     */
    PostgreSQL,
    /**
     * For using the Java the networking API to communicate data.
     *
     * Using a socket-based communication system can be a serious
     * security hazard especially if you do not have a proper
     * firewall setup. Use with caution.
     */
    SOCKETS,
    /**
     * For using proxy-based plugin messaging as the intended communication database.
     * Only intended for spigot/bungee minecraft servers.
     */
    PLUGIN_MESSAGING,

}
