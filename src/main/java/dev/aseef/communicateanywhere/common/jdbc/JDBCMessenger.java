package dev.aseef.communicateanywhere.common.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import dev.aseef.communicateanywhere.api.MessengerType;
import dev.aseef.communicateanywhere.common.AbstractMessenger;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public abstract class JDBCMessenger extends AbstractMessenger {

    private String driverUrl;
    private String driverClass;
    private MessengerType databaseType;
    private DatabaseCredential credential;
    private HikariDataSource dataSource;

    public JDBCMessenger(MessengerType databaseType, String driverUrl, String driverClass, @NotNull DatabaseCredential credential, long listenerKeepAliveTime) {
        super(credential, listenerKeepAliveTime);
        if (databaseType != MessengerType.H2 &&
                databaseType != MessengerType.MYSQL &&
                databaseType != MessengerType.MONGODB &&
                databaseType != MessengerType.PostgreSQL)
            throw new IllegalArgumentException();
        this.driverUrl = driverUrl;
        this.driverClass = driverClass;
        this.databaseType = databaseType;
        this.credential = credential;
    }

    public void init() throws SQLException {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:" + driverUrl + "://" + credential.getHostName() +
                (credential.getPort() == -1 ? "" : ":" + credential.getPort()) +
                "/" + credential.getDatabase() + "?characterEncoding=UTF-8");
        dataSource.setUsername(credential.getUsername());
        dataSource.setPassword(credential.getPassword());
        dataSource.setDriverClassName(driverClass);

        dataSource.setPoolName("CA-" + driverUrl.toUpperCase() + "-POOL");
        dataSource.setMaxLifetime(1000 * 60 * 30L); // 30 min
        dataSource.setKeepaliveTime(1000 * 60 * 1L); // 1 min
        dataSource.setLeakDetectionThreshold(1000 * 60 * 20L); // 20 seconds
        dataSource.setMinimumIdle(2); // constant pool of 1
        dataSource.setMaximumPoolSize(2); // constant pool of 1
        dataSource.setLoginTimeout(15);
        dataSource.setLogWriter(new PrintWriter(System.out));

        try (Connection conn = this.getConnection()) {
            createTable(conn);
        }
    }

    @Override
    public CompletableFuture<MessageObject> message(String channel, MessageObject mo) {
        CompletableFuture<MessageObject> reply = new CompletableFuture<>();
        try (Connection conn = this.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO ca_messages (channel, message_blob, is_reply) VALUES (?, ?, ?);")) {
                ps.setString(1, channel);
                ps.setString(2, mo.toBson().toString());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        // add reply to waiting
        this.getPendingRepliesMap().put(mo.getMessageId(), reply);

        return reply;
    }

    @Override
    public Runnable getPollingProtocol() {
        try {
            return new JDBCPollingProtocol(this);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void createTable(Connection conn) throws SQLException {
        String query = "create table if not exists ca_messages\n" +
                "(\n" +
                "\tchannel varchar(32) not null,\n" +
                "\tmessage_blob longblob not null,\n" +
                "\tis_reply tinyint(1) not null," +
                "\tcreation long not null default ROUND(UNIX_TIMESTAMP(CURTIME(4) * 1000))" +
                ");";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.executeUpdate();
        }
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null)
            throw new IllegalStateException("The JDBC Messenger has not yet been initialized!");
        return dataSource.getConnection();
    }

}
