package net.shafin.nlp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class DBConn {
    private final Connection connection;
    private boolean connectedToDatabase = false;

    public DBConn(Connection connection) {
        this.connection = connection;
        if (this.connection != null) {
            this.connectedToDatabase = true;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet retriveResultset(PreparedStatement queryStatement) throws SQLException, IllegalStateException {
        if (!this.connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }
        return queryStatement.executeQuery();
    }

    public int executeQuery(PreparedStatement queryStatement) throws SQLException, IllegalStateException {
        if (!this.connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }
        return queryStatement.executeUpdate();
    }

    public void disconnect() {
        if (connectedToDatabase) {
            /*
             * try { connection.close(); } catch (SQLException e) {
			 * e.printStackTrace(); }
			 */
        }
    }
}
