package dev.aseef.communicateanywhere.common.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import dev.aseef.communicateanywhere.api.MessengerType;
import dev.aseef.communicateanywhere.common.AbstractMessenger;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.sql.*;
import java.util.concurrent.CompletableFuture;

//todo: subscribe pubsub a seperate table
public abstract class JDBCMessenger extends AbstractMessenger {

    private String driverUrl;
    private String driverClass;
    private MessengerType databaseType;
    private DatabaseCredential credential;
    private HikariDataSource dataSource;

    public JDBCMessenger(MessengerType databaseType, String driverUrl, String driverClass, @NotNull DatabaseCredential credential, long listenerKeepAliveTime, long replyTimeout, double compressionThreshold, long maxPersist) {
        super(credential, listenerKeepAliveTime, replyTimeout, compressionThreshold, maxPersist);
        if (databaseType != MessengerType.H2 &&
                databaseType != MessengerType.MYSQL &&
                databaseType != MessengerType.MONGODB &&
                databaseType != MessengerType.PostgreSQL)
            throw new IllegalArgumentException();
        this.driverUrl = driverUrl;
        this.driverClass = driverClass;
        this.databaseType = databaseType;
        this.credential = credential;
        try {
            init();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        initPolling();
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
        dataSource.setMinimumIdle(3); // constant pool of 3, one for sending one for receiving and one for deleting expired messages
        dataSource.setMaximumPoolSize(3); // constant pool of 3, one for sending one for receiving and one for deleting expired messages
        dataSource.setLoginTimeout(15);
        dataSource.setLogWriter(new PrintWriter(System.out));

        try (Connection conn = this.getConnection()) {
            createTables(conn);
        }

    }

    @Override
    public CompletableFuture<MessageObject> message(String channel, MessageObject mo) {
        try (Connection conn = this.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO ca_messages (messenger_id, channel, message_blob, expiry) VALUES (?, ?, ?, (ROUND(UNIX_TIMESTAMP(CURTIME(4))*1000 + ?)));", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, this.getMessengerId().toString());
                ps.setString(2, channel);
                ps.setString(3, mo.toBson().toString());
                ps.setLong(4, this.getMaxPersist());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        CompletableFuture<MessageObject> reply = new CompletableFuture<>();
                        long messageId = rs.getLong(1);
                        // add reply to waiting
                        this.getPendingRepliesMap().put(messageId, reply);
                        return reply;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        throw new IllegalStateException("Something went wrong whilst sending the following message through channel '" + channel + "':\n" + mo.toString());
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

    public void createTables(Connection conn) throws SQLException {
        String query = "create table if not exists ca_messages\n" +
                "(\n" +
                "\tmessage_id bigint(20) auto_increment,\n" +
                "\tmessenger_id varchar(36) not null,\n" +
                "\tchannel varchar(32) not null,\n" +
                "\tmessage_blob longblob not null,\n" +
                "\tcreation bigint(20) not null default ROUND(UNIX_TIMESTAMP(CURTIME(4))*1000)," +
                "\texpiry bigint(20) not null," +
                "\tprimary key (message_id)," +
                "\tindex (creation)" +
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
