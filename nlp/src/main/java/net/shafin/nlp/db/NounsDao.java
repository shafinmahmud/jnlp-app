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

    public NounsDao(SQLiteDBConn dbConn) {
        super(dbConn);
    }

    public void createTable() {
        /* Creating the Query */
        StringBuilder SQL = new StringBuilder("CREATE TABLE IF NOT EXISTS noun(");
        SQL.append("n TEXT NOT NULL UNIQUE);").append("CREATE UNIQUE INDEX idx_n ON noun(n);");

        Logger.print(SQL.toString());

		/* Executing the Query */
        try {
            String query = SQL.toString();
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.DB_CONN.executeQuery(this.qs);

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
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.DB_CONN.executeQuery(this.qs);

        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
    }

    public boolean emptyTableTermIndex() {
        try {
            String query = "DELETE FROM noun;";
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.DB_CONN.executeQuery(this.qs);

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
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.qs.setString(1, noun);

            this.rs = this.DB_CONN.retriveResultset(this.qs);
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
            this.qs = DB_CONN.getConnection().prepareStatement(query);

            this.rs = this.DB_CONN.retriveResultset(this.qs);
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
            DB_CONN.getConnection().setAutoCommit(false);

            String query = "INSERT OR IGNORE INTO noun(n) VALUES (?)";
            this.qs = DB_CONN.getConnection().prepareStatement(query.toString());

            final int batchSize = 20000;
            int count = 0;

            for (String n : nouns) {
                this.qs.setString(1, n);
                this.qs.addBatch();

                if (++count % batchSize == 0) {
                    this.qs.executeBatch();
                    this.DB_CONN.getConnection().commit();
                }
            }

            this.qs.executeBatch();
            this.DB_CONN.getConnection().commit();
            DB_CONN.getConnection().setAutoCommit(true);

            return true;

        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }

        return false;
    }
}