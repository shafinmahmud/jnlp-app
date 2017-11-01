package net.shafin.nlp.db;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.shafin.nlp.corpus.model.TermIndex;
import net.shafin.common.util.Logger;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class IndexDao extends BasicDao<TermIndex> {

    public IndexDao(SQLiteDBConn dbConn) {
        super(dbConn);
    }

    public void createTable() {
		/* Creating the Query */
        StringBuilder SQL = new StringBuilder("CREATE TABLE term_index(");
        SQL.append("doc_id INT NOT NULL,").append(" ").append("term TEXT NOT NULL,").append(" ")
                .append("is_manual BOOLEAN NULL,").append(" ")
                .append("is_train BOOLEAN NULL,").append(" ")
                .append("tf INT NOT NULL,").append(" ")
                .append("df INT NULL,").append(" ")
                .append("ps double NULL,").append(" ")
                .append("noun_freq DOUBLE  NULL);")
                .append("CREATE INDEX idx_term ON term_index(term);");

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
        StringBuilder SQL = new StringBuilder("DROP TABLE IF EXISTS term_index");
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
            String query = "DELETE FROM term_index;";
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

    public List<TermIndex> getIndexesByDocID(int docId) {
        try {
            String query = "SELECT * FROM term_index where doc_id = ? ";
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.qs.setInt(1, docId);

            List<TermIndex> terms = new ArrayList<>();
            this.rs = this.DB_CONN.retriveResultset(this.qs);
            while (rs.next()) {
                TermIndex term = new TermIndex(rs.getInt("doc_id"));
                term.setTerm(rs.getString("term"));
                term.setManual(rs.getBoolean("is_manual"));
                term.setTrain(rs.getBoolean("is_train"));
                term.setTf(rs.getInt("tf"));
                term.setDf(rs.getInt("df"));
                term.setPs(rs.getDouble("ps"));
                term.setNounFreq(rs.getDouble("noun_freq"));

                terms.add(term);
            }
            return terms;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return null;
    }

    public List<TermIndex> getIndexesByDocIdAndTerm(int docId, String term) {
        try {
            String query = "SELECT * FROM term_index where doc_id = ? and term = ?";
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.qs.setInt(1, docId);
            this.qs.setString(2, term);

            List<TermIndex> indexes = new ArrayList<>();
            this.rs = this.DB_CONN.retriveResultset(this.qs);
            while (rs.next()) {
                TermIndex index = new TermIndex(rs.getInt("doc_id"));
                index.setTerm(rs.getString("term"));
                index.setManual(rs.getBoolean("is_manual"));
                index.setTrain(rs.getBoolean("is_train"));
                index.setTf(rs.getInt("tf"));
                index.setDf(rs.getInt("df"));
                index.setPs(rs.getDouble("ps"));
                index.setNounFreq(rs.getDouble("noun_freq"));

                indexes.add(index);
            }
            return indexes;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return null;
    }

    public List<TermIndex> getManualIndexesByDocId(int docId) {
        try {
            String query = "SELECT * FROM term_index where doc_id = ? and is_manual = 1";
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.qs.setInt(1, docId);

            List<TermIndex> indexes = new ArrayList<>();
            this.rs = this.DB_CONN.retriveResultset(this.qs);
            while (rs.next()) {
                TermIndex index = new TermIndex(rs.getInt("doc_id"));
                index.setTerm(rs.getString("term"));
                index.setManual(rs.getBoolean("is_manual"));
                index.setTrain(rs.getBoolean("is_train"));
                index.setTf(rs.getInt("tf"));
                index.setDf(rs.getInt("df"));
                index.setPs(rs.getDouble("ps"));
                index.setNounFreq(rs.getDouble("noun_Freq"));

                indexes.add(index);
            }
            return indexes;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return null;
    }

    public List<TermIndex> getIndexesByIsTrainPagination(boolean isTrain, int page, int size) {
        try {
            String query = "SELECT * FROM term_index where is_train = ? LIMIT ?,?";
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.qs.setBoolean(1, isTrain);
            this.qs.setInt(2, (page - 1) * size);
            this.qs.setInt(3, size);

            List<TermIndex> terms = new ArrayList<>();
            this.rs = this.DB_CONN.retriveResultset(this.qs);
            while (rs.next()) {
                TermIndex term = new TermIndex(rs.getInt("doc_id"));
                term.setTerm(rs.getString("term"));
                term.setManual(rs.getBoolean("is_manual"));
                term.setTrain(rs.getBoolean("is_train"));
                term.setTf(rs.getInt("tf"));
                term.setDf(rs.getInt("df"));
                term.setPs(rs.getDouble("ps"));
                term.setNounFreq(rs.getDouble("noun_freq"));

                terms.add(term);
            }
            return terms;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return null;
    }

    public List<TermIndex> getIndexesByIsTrainPagination(int docId, boolean isTrain, int page, int size) {
        try {
            String query = "SELECT * FROM term_index where is_train = ? and doc_id = ? LIMIT ?,?";
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.qs.setBoolean(1, isTrain);
            this.qs.setInt(2, docId);
            this.qs.setInt(3, (page - 1) * size);
            this.qs.setInt(4, size);

            List<TermIndex> terms = new ArrayList<>();
            this.rs = this.DB_CONN.retriveResultset(this.qs);
            while (rs.next()) {
                TermIndex term = new TermIndex(rs.getInt("doc_id"));
                term.setTerm(rs.getString("term"));
                term.setManual(rs.getBoolean("is_manual"));
                term.setTrain(rs.getBoolean("is_train"));
                term.setTf(rs.getInt("tf"));
                term.setDf(rs.getInt("df"));
                term.setPs(rs.getDouble("ps"));
                term.setNounFreq(rs.getDouble("noun_Freq"));

                terms.add(term);
            }
            return terms;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return null;
    }

    public boolean isExistsByDocIdAndTerm(int docId, String term) {
        try {
            String query = "SELECT doc_id FROM term_index where doc_id = ? and term = ?";
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.qs.setInt(1, docId);
            this.qs.setString(2, term);

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

    public boolean updateIsManualKP(int docId, String term, boolean isManual) {
        try {
            String query = "UPDATE term_index SET is_manual = ? where doc_id = ? and term = ?";
            this.qs = DB_CONN.getConnection().prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            this.qs.setBoolean(1, isManual);
            this.qs.setInt(2, docId);
            this.qs.setString(3, term);

            this.DB_CONN.executeQuery(this.qs);
            return true;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return false;
    }

    public boolean updateTermByBatch(Map<String, String> termMap) {
        try {
            DB_CONN.getConnection().setAutoCommit(false);

            String query = "UPDATE term_index SET term = ? where term = ?;";
            this.qs = DB_CONN.getConnection().prepareStatement(query);

            final int batchSize = 1000;
            int count = 0;

            for (Map.Entry<String, String> entry : termMap.entrySet()) {
                this.qs.setString(1, entry.getValue());
                this.qs.setString(2, entry.getKey());
                this.qs.addBatch();

                if (++count % batchSize == 0) {
                    this.qs.executeBatch();
                    this.DB_CONN.getConnection().commit();

                    Logger.print("RUNNING QUERY : " + count);
                }
            }

            this.qs.executeBatch();
            this.DB_CONN.getConnection().commit();
            return true;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return false;
    }

    public List<String> getDistinctTermsByPagination(int page, int size) {
        try {
            String query = "SELECT DISTINCT term FROM term_index LIMIT ?,?";
            this.qs = DB_CONN.getConnection().prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            this.qs.setInt(1, (page - 1) * size);
            this.qs.setInt(2, size);

            this.rs = this.DB_CONN.retriveResultset(this.qs);
            List<String> terms = new ArrayList<>();
            while (rs.next()) {
                terms.add(rs.getString("term"));
            }
            return terms;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return null;
    }

    public List<String> getDistinctTermsByDocId(int docId) {
        try {
            String query = "SELECT DISTINCT term FROM term_index where doc_id = ?";
            this.qs = DB_CONN.getConnection().prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            this.qs.setInt(1, docId);

            this.rs = this.DB_CONN.retriveResultset(this.qs);
            List<String> terms = new ArrayList<>();
            while (rs.next()) {
                terms.add(rs.getString("term"));
            }
            return terms;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return null;
    }

    public List<String> getDistinctTerms() {
        Statement stmnt = null;
        try {
            String query = "SELECT DISTINCT term FROM term_index  ";
            stmnt = DB_CONN.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            this.rs = stmnt.executeQuery(query);
            List<String> terms = new ArrayList<>();
            while (rs.next()) {
                terms.add(rs.getString("term"));
            }
            return terms;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmnt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            leaveGracefully();
        }
        return null;
    }

    public int getDistictTermCount() {
        try {
            String query = "SELECT count(DISTINCT term) as total FROM term_index";
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

    public int getTermCountByDoc(int docId) {
        try {
            String query = "SELECT count(*) as total FROM term_index where doc_id = ?";
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.qs.setInt(1, docId);

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

    public int getTrainTermCount() {
        try {
            String query = "SELECT count(*) as total FROM term_index where is_train = 1";
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

    public int getTestTermCount() {
        try {
            String query = "SELECT count(*) as total FROM term_index where is_train = 0";
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

    public int getDocCount() {
        try {
            String query = "SELECT count(DISTINCT doc_id) as total FROM term_index";
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

    public int getTrainDocCount() {
        try {
            String query = "SELECT count(DISTINCT doc_id) as total FROM term_index where is_train = 1";
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

    public int getTestDocCount() {
        try {
            String query = "SELECT count(DISTINCT doc_id) as total FROM term_index where is_train = 0";
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

    public List<Integer> getDocIds(boolean isTrain) {
        try {
            String query = "SELECT DISTINCT doc_id FROM term_index where is_train = ?";
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.qs.setBoolean(1, isTrain);

            this.rs = this.DB_CONN.retriveResultset(this.qs);

            List<Integer> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rs.getInt("doc_id"));
            }

            return list;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return null;
    }

    public boolean insertTermIndex(TermIndex index) {
        try {
            String query = "INSERT INTO term_index(doc_id, term, is_manual, is_train, tf, idf, ps, noun_freq) VALUES (?,?,?,?,?,?,?,?)";
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.qs.setInt(1, index.getDocId());
            this.qs.setString(2, index.getTerm());
            this.qs.setBoolean(3, index.isManual());
            this.qs.setBoolean(4, index.isTrain());
            this.qs.setInt(5, index.getTf());
            this.qs.setInt(6, index.getDf());
            this.qs.setDouble(7, index.getPs());
            this.qs.setDouble(8, index.getNounFreq());

            this.DB_CONN.executeQuery(this.qs);
            Logger.print(query);
            return true;

        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return false;

    }

    public boolean deleteTermIndex(TermIndex index) {
        try {
            String query = "DELETE FROM term_index where doc_id = ? and term = ?";
            this.qs = DB_CONN.getConnection().prepareStatement(query);
            this.qs.setInt(1, index.getDocId());
            this.qs.setString(2, index.getTerm());

            this.DB_CONN.executeQuery(this.qs);
            return true;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            leaveGracefully();
        }
        return false;
    }

    public boolean insertTermInBatch(List<TermIndex> termIndexes) {
        try {
            DB_CONN.getConnection().setAutoCommit(false);

            String query = "INSERT INTO term_index(doc_id, term, is_manual, is_train, tf, df, ps, noun_freq) VALUES (?,?,?,?,?,?,?,?)";
            this.qs = DB_CONN.getConnection().prepareStatement(query.toString());

            final int batchSize = 20000;
            int count = 0;

            for (TermIndex index : termIndexes) {
                this.qs.setInt(1, index.getDocId());
                this.qs.setString(2, index.getTerm());
                this.qs.setBoolean(3, index.isManual());
                this.qs.setBoolean(4, index.isTrain());
                this.qs.setInt(5, index.getTf());
                this.qs.setInt(6, index.getDf());
                this.qs.setDouble(7, index.getPs());
                this.qs.setDouble(8, index.getNounFreq());
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

    public boolean insertAsDiscardedTerm(TermIndex index, File file) {
        FileOutputStream fos = getFileOutputStream(file);

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos, "UTF8")) {
            String textData = index.getDocId() + ": " + index.getTerm() + "\n";
            textData = textData.replaceAll("\n", System.lineSeparator());
            outputStreamWriter.write(textData);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateDF(boolean dataSetType) {
        Statement statement = null;
        try {
            statement = this.DB_CONN.getConnection().createStatement();
            this.DB_CONN.getConnection().setAutoCommit(false);

            Logger.print("TOTAL UNIQUE TERMS : " + getDistictTermCount());

			/* DROP Temp tables if exists */
            statement.addBatch("DROP TABLE IF EXISTS _temp;");
            statement.addBatch("DROP TABLE IF EXISTS _term_index;");

			/* CREATE _temp and _temp_index tables */
            statement.addBatch("CREATE TABLE _temp(term TEXT, is_train BOOLEAN NOT NULL, df INT);");
            statement.addBatch("CREATE TABLE _term_index(doc_id INT NOT NULL,term TEXT NOT NULL, "
                    + "is_manual BOOLEAN NULL,is_train BOOLEAN NULL, "
                    + "tf INT NOT NULL,df INT NULL,ps DOUBLE NOT NULL,noun_freq DOUBLE NULL);");

			/*
             * INSERT _temp the term wise document count and by joining with
			 * term_index saving it to _term_index
			 */
            statement.addBatch("INSERT INTO _temp SELECT term, is_train, count(doc_id) as df "
                    + "FROM term_index where is_train = 1 group by term;");
            statement.addBatch("INSERT INTO _temp SELECT term, is_train, count(doc_id) as df "
                    + "FROM term_index where is_train = 0 group by term;");
            statement.addBatch("INSERT INTO _term_index(doc_id, term, is_manual, is_train, tf, df, ps, noun_freq) "
                    + "SELECT i.doc_id, i.term, i.is_manual, i.is_train, i.tf, t.df, i.ps, i.noun_freq "
                    + "FROM term_index  i, _temp t where t.term = i.term and i.is_train = t.is_train;");

			/* EMPTY the original term_index and copy from _term_index */
            statement.addBatch("DELETE FROM term_index;");
            statement.addBatch("INSERT INTO term_index(doc_id, term, is_manual, is_train, tf, df, ps, noun_freq) "
                    + "SELECT * FROM _term_index;");

			/* DROP Temp tables if exists */
            statement.addBatch("DROP TABLE IF EXISTS _temp;");
            statement.addBatch("DROP TABLE IF EXISTS _term_index;");

            Logger.print("UPDATING DOCUMENT FREQ...");
            statement.executeBatch();
            this.DB_CONN.getConnection().commit();

            return true;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                    this.DB_CONN.getConnection().setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            leaveGracefully();
        }
        return false;
    }

    private FileOutputStream getFileOutputStream(File file) {
        // if file doesn't exists, then create it
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileOutputStream;
    }
}
