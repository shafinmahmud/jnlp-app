package shafin.nlp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.sqlite.SQLiteConfig;

public class SQLiteDBConn extends DBConn{

	private static Connection staticConnection;
	private static final String DATABASE_URL = "jdbc:sqlite:/home/dw/indx/corpus.sqlite";
	private static final String DRIVER_CLASS = "org.sqlite.JDBC";
	
	public static final String DISCARDED_FILE = "/home/dw/indx/zero_freq_terms.txt";


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
