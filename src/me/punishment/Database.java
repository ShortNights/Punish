package me.punishment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Database {
    private HikariDataSource dataSource;
    private boolean initialized = false;


    public Database(RDBMS rdbms, String username, String password, String host, int port, String database, String poolName, int maxConnections) {
        initializeConnPool(rdbms, username, password, host, port, database, poolName, maxConnections); // Initialize pool
    }


    public Database(DatabaseTemplate template, String poolName, int maxConnections) {
        this(template.rdbms, template.username, template.password, template.host, template.port, template.database, poolName, maxConnections);
    }

    @Deprecated
    public PreparedStatement prepare(String sql) {
        try {
            return getIdleConnection().prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public void executeUpdate(String update) {
        try {
            PreparedStatement statement = prepare(update);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public ResultSet executeQuery(String query) {
        try {
            PreparedStatement statement = prepare(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Connection getIdleConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // You better hope this doesn't happen :/
        }
    }

    public void killPool() {
        try {
            dataSource.unwrap(HikariDataSource.class).close();
            dataSource.close();
        } catch (SQLException e) {
            System.err.println(" ____________________________________");
            System.err.println("|                                    |");
            System.err.println("| The database pool failed to close! |"); 
            System.err.println("|____________________________________|");
        }
    }

    private void initializeConnPool(RDBMS rdbms, String username, String password, String host, int port, String database, String poolName, int maxConnections) {
        if (initialized) return;

        HikariConfig conf = new HikariConfig();
        conf.setDriverClassName(rdbms.driver);
        conf.setJdbcUrl("jdbc:" + rdbms.name().toLowerCase() + "://" + host + ":" + port + "/" + database);
        conf.setUsername(username);
        conf.setPassword(password);
        conf.addDataSourceProperty("connectionTimeout", 30000);
        conf.addDataSourceProperty("idleTimeout", 60000);
        conf.addDataSourceProperty("maxLifetime", 90000); 
        conf.addDataSourceProperty("minimumIdle", Math.round(maxConnections / 4)); 
        conf.addDataSourceProperty("maximumPoolSize", maxConnections);
        if (!poolName.isEmpty()) conf.addDataSourceProperty("poolName", poolName);
        dataSource = new HikariDataSource(conf);
        initialized = true;
    }

    public enum DatabaseTemplate {
        MYSQL(RDBMS.MYSQL, "root", "password", "localhost", 3306, "punishments");

        RDBMS rdbms;
        String username, password, host, database;
        int port;

        DatabaseTemplate(RDBMS rdbms, String username, String password, String host, int port, String database) {
            this.rdbms = rdbms;
            this.username = username;
            this.password = password;
            this.host = host;
            this.port = port;
            this.database = database;
        }
    }

    public enum RDBMS {
        MYSQL("com.mysql.jdbc.Driver"), POSTGRESQL("org.postgresql.Driver");

        String driver;

        RDBMS(String driver) {
            this.driver = driver;
        }
    }
}