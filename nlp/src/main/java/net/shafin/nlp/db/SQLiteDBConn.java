package net.shafin.nlp.db;

import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class SQLiteDBConn extends DBConn {

    private static Connection staticConnection;
    private static final String DB_SCHEMA = "jdbc:sqlite:";
    private static final String DRIVER_CLASS = "org.sqlite.JDBC";

    public static void initializeDB(String databaseFilePath) {
        try {
            if(!isFilePathValid(databaseFilePath)) {
                throw new IllegalArgumentException("Invalid database File Path");
            }

            if (staticConnection == null) {
                Class.forName(DRIVER_CLASS);
                final String DATABASE_URL = DB_SCHEMA + databaseFilePath;

                SQLiteConfig config = new SQLiteConfig();
                config.setEncoding(SQLiteConfig.Encoding.UTF8);
                staticConnection = DriverManager.getConnection(DATABASE_URL, config.toProperties());
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static SQLiteDBConn getSQLiteDBConn() {
        if (staticConnection == null) {
            throw new IllegalStateException("No Connection has been initialized.");
        }

        return new SQLiteDBConn(staticConnection);
    }

    public SQLiteDBConn(Connection conn) {
        super(conn);
    }

    private static boolean isFilePathValid(String path) {
        if (path == null || "".equals(path) || !path.endsWith(".sqlite")) {
            return false;
        }

        return !new File(path).isDirectory();
    }
}
