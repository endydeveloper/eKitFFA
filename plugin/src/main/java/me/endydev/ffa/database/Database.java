package me.endydev.ffa.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.UUID;

public class Database {

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;
    private Connection con;

    private HikariDataSource dataSource;

    public Database(final String host, final int port, final String database, final String user, final String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true");
        config.setUsername(this.user);
        config.setPassword(this.password);
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("useLocalSessionState", true);
        config.addDataSourceProperty("rewriteBatchedStatements", true);
        config.addDataSourceProperty("cacheResultSetMetadata", true);
        config.addDataSourceProperty("cacheServerConfiguration", true);
        config.addDataSourceProperty("elideSetAutoCommits", true);
        config.addDataSourceProperty("maintainTimeStats", false);
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("encoding", "UTF-8");
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("useSSL", false);
        config.addDataSourceProperty("tcpKeepAlive", true);
        config.setPoolName("ThePIT " + UUID.randomUUID());
        config.setMaxLifetime(Long.MAX_VALUE);
        config.setMinimumIdle(0);
        config.setIdleTimeout(30000L);
        config.setConnectionTimeout(10000L);
        config.setMaximumPoolSize(30);
        this.dataSource = new HikariDataSource(config);
    }

    public boolean isConnected() {
        try {
            return getConnection() != null || !getConnection().isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException ex) {
            return null;
        }
    }

    public void close() {
        this.dataSource.close();
    }

    public void close(AutoCloseable closable) {
        if (closable != null)
            try {
                closable.close();
            } catch (Exception ignored) {}
    }

    public void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void update(String query) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            close(connection, st, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String query) {
        try {
            Connection connection = getConnection();
            Statement st = connection.createStatement();
            return st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(String query, Object... args) {
        try {
            Connection connection = getConnection();
            PreparedStatement st = connection.prepareStatement(query);
            for (int i = 0; i < args.length; i++) {
                st.setObject(i + 1, args[i]);
            }
            st.executeUpdate();
            close(connection, st, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String query, Object... args) {
        try {
            Connection connection = getConnection();
            PreparedStatement st = connection.prepareStatement(query);
            for (int i = 0; i < args.length; i++) {
                st.setObject(i + 1, args[i]);
            }
            return st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
