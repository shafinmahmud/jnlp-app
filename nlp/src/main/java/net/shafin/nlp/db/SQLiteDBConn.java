package net.shafin.nlp.db;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class SQLiteDBConn extends DBConn {

    private static Connection staticConnection;
    private static final String DATABASE_URL = "jdbc:sqlite:/home/dw/indx/corpus.sqlite";
    private static final String DRIVER_CLASS = "org.sqlite.JDBC";

    public static final String ZERO_FREQ_FILE = "/home/dw/indx/zero_freq_terms.txt";
    public static final String STOP_FILTERED_FILE = "/home/dw/indx/stop_filtered_terms.txt";
    public static final String VERBSUFX_FILTERED_FILE = "/home/dw/indx/verbsuffx_filtered_terms.txt";


    public static SQLiteDBConn getSQLiteDBConn() {
        try {
            if (staticConnection == null) {
                Class.forName(DRIVER_CLASS);

                SQLiteConfig config = new SQLiteConfig();
                config.setEncoding(SQLiteConfig.Encoding.UTF8);
                staticConnection = DriverManager.getConnection(DATABASE_URL, config.toProperties());
            }
            return new SQLiteDBConn(staticConnection);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SQLiteDBConn(Connection conn) {
        super(conn);
    }
}
