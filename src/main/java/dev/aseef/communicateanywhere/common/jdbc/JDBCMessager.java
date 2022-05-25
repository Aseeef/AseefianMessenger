package dev.aseef.communicateanywhere.common.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import dev.aseef.communicateanywhere.api.CommunicationDatabase;
import dev.aseef.communicateanywhere.common.AbstractMessager;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class JDBCMessager extends AbstractMessager {

    private String driverUrl;
    private String driverClass;
    private CommunicationDatabase databaseType;
    private DatabaseCredential credential;
    private HikariDataSource dataSource;

    public JDBCMessager(CommunicationDatabase databaseType, String driverUrl, String driverClass, @NotNull DatabaseCredential credential) {
        super(credential);
        if (databaseType != CommunicationDatabase.H2 &&
                databaseType != CommunicationDatabase.MYSQL &&
                databaseType != CommunicationDatabase.MONGODB &&
                databaseType != CommunicationDatabase.PostgreSQL)
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
        dataSource.setMinimumIdle(2); // constant pool of 2
        dataSource.setMaximumPoolSize(2); // constant pool of 2
        dataSource.setLoginTimeout(15);
        dataSource.setLogWriter(new PrintWriter(System.out));

    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null)
            throw new IllegalStateException("The JDBC Messager has not yet been initialized!");
        return dataSource.getConnection();
    }

}
