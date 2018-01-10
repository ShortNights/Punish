package me.punishment;

public class FallbackDetails {

    private String username, password, host, database, poolName;
    private int port;

    public FallbackDetails(String username, String password, String host, int port, String database, String poolName) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.database = database;
        this.poolName = poolName;
    }

    public String getUsername() {
        return username;
    }

    public String getPoolName() {
        return poolName;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public int getPort() {
        return port;
    }
}