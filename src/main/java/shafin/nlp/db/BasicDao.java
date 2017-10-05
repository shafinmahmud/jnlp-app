package shafin.nlp.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Author : Shafin Mahmud
 * Email  : shafin.mahmud@gmail.com
 * Date	  : 02-10-2016 SUN
 */
public abstract class BasicDao<T>{

	public final DBConn DB_CONN;
	public PreparedStatement qs;
	public ResultSet rs;

	public BasicDao(DBConn dbConn) {
		DB_CONN = dbConn;
	}
	
	public void leaveGracefully() {
		if (this.qs != null) {
			try {
				this.qs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (this.rs != null) {
			try {
				this.rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
