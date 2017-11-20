package net.shafin.common.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author shafin
 * @since 11/13/16
 */
public class DataSource {

    public static Connection connection;

    protected String connectionUrl;
    protected String user;
    protected String password;

    public DataSource(ConnectionConfig config) {
        this.user = config.user;
        this.password = config.password;
        this.connectionUrl = config.connectionUrl;
    }

    public DataSource(String dbPropertiesFile) throws IOException {
        Properties dbProperties = readPropertyFile("db.properties");
        this.connectionUrl = dbProperties.getProperty("jdbc.url");
        this.user = dbProperties.getProperty("jdbc.user");
        this.password = dbProperties.getProperty("jdbc.pass");
    }

    public boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return isConnected() ? connection : initConnection();
    }

    private Connection initConnection() {
        try {
            return authNotRequired() ? DriverManager.getConnection(connectionUrl)
                    : DriverManager.getConnection(connectionUrl, user, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean authNotRequired() {
        return user == null || user.isEmpty();
    }

    private static Properties readPropertyFile(String propertyFileName) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();

        try (InputStream resourceStream = loader.getResourceAsStream(propertyFileName)) {
            props.load(resourceStream);
        }

        return props;
    }

    public static class ConnectionConfig {
        private String connectionUrl;
        private String user;
        private String password;

        public ConnectionConfig(String connectionUrl) {
            this.connectionUrl = connectionUrl;
        }

        public ConnectionConfig setUser(String user) {
            this.user = user;
            return this;
        }

        public ConnectionConfig setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}
