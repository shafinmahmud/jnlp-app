package net.shafin.nlp.db;

import java.sql.SQLException;
import java.util.Set;

import net.shafin.nlp.corpus.model.TermIndex;
import net.shafin.common.util.Logger;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class NounsDao extends BasicDao<TermIndex> {

    public NounsDao(SQLiteDBHandler dbConn) {
        super(dbConn);
    }

    public void createTable() {
        /* Creating the Query */
        StringBuilder SQL = new StringBuilder("CREATE TABLE IF NOT EXISTS noun(");
        SQL.append("n TEXT NOT NULL UNIQUE);")
                .append("CREATE UNIQUE INDEX idx_n ON noun(n);");

        Logger.print(SQL.toString());

		/* Executing the Query */
        try {
            String query = SQL.toString();
            this.ps = dbHandler.getConnection().prepareStatement(query);
            this.dbHandler.executeQuery(this.ps);

        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
    }

    public void deleteTable() {
        /* Creating the Query */
        StringBuilder SQL = new StringBuilder("DROP TABLE IF EXISTS noun");
        Logger.print(SQL.toString());

		/* Executing the Query */
        try {
            String query = SQL.toString();
            this.ps = dbHandler.getConnection().prepareStatement(query);
            this.dbHandler.executeQuery(this.ps);

        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
    }

    public boolean emptyTableTermIndex() {
        try {
            String query = "DELETE FROM noun;";
            this.ps = dbHandler.getConnection().prepareStatement(query);
            this.dbHandler.executeQuery(this.ps);

            return true;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return false;
    }

    public boolean isExists(String noun) {
        try {
            String query = "SELECT * FROM noun where n = ?";
            this.ps = dbHandler.getConnection().prepareStatement(query);
            this.ps.setString(1, noun);

            this.rs = this.dbHandler.retriveResultset(this.ps);
            if (rs.next()) {
                return true;
            }
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return false;
    }

    public int countNoun() {
        try {
            String query = "SELECT count(*) as total FROM noun";
            this.ps = dbHandler.getConnection().prepareStatement(query);

            this.rs = this.dbHandler.retriveResultset(this.ps);
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return 0;
    }

    public boolean insertVerbsBatch(Set<String> nouns) {
        try {
            dbHandler.getConnection().setAutoCommit(false);

            String query = "INSERT OR IGNORE INTO noun(n) VALUES (?)";
            this.ps = dbHandler.getConnection().prepareStatement(query.toString());

            final int batchSize = 20000;
            int count = 0;

            for (String n : nouns) {
                this.ps.setString(1, n);
                this.ps.addBatch();

                if (++count % batchSize == 0) {
                    this.ps.executeBatch();
                    this.dbHandler.getConnection().commit();
                }
            }

            this.ps.executeBatch();
            this.dbHandler.getConnection().commit();
            dbHandler.getConnection().setAutoCommit(true);

            return true;

        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }

        return false;
    }
}