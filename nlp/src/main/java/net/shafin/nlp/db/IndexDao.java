package net.shafin.nlp.db;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.shafin.common.db.GenericDao;
import net.shafin.common.db.RowMapper;
import net.shafin.nlp.corpus.model.TermIndex;
import net.shafin.common.util.Logger;
import net.shafin.nlp.main.AppBootProcess;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class IndexDao extends GenericDao<TermIndex> {

    public IndexDao() {
        super(AppBootProcess.dataSource);
    }

    public void createTable() {
        /* Creating the Query */
        String query = "CREATE TABLE term_index(doc_id INT NOT NULL,"
                + " term TEXT NOT NULL,"
                + " is_manual BOOLEAN NULL,"
                + " is_train BOOLEAN NULL,"
                + " tf INT NOT NULL,"
                + " df INT NULL,"
                + " ps double NULL,"
                + " noun_freq DOUBLE  NULL);"
                + " CREATE INDEX idx_term ON term_index(term);";

        this.template.executeQuery(query);
    }

    public void deleteTable() {
        String query = "DROP TABLE IF EXISTS term_index";
        this.template.executeQuery(query);
    }

    public void truncateTermIndex() {
        String query = "DELETE FROM term_index;";
        this.template.executeQuery(query);
    }

    public List<TermIndex> getIndexesByDocID(int docId) {
        String query = "SELECT * FROM term_index where doc_id = ? ";
        return this.template.retrieveData(query, new TermIndexMapper(), new Object[]{docId});
    }

    public List<TermIndex> getIndexesByDocIdAndTerm(int docId, String term) {
        String query = "SELECT * FROM term_index where doc_id = ? and term = ?";
        return this.template.retrieveData(query, new TermIndexMapper(),
                new Object[]{docId, term});
    }

    public List<TermIndex> getManualIndexesByDocId(int docId) {
        String query = "SELECT * FROM term_index where doc_id = ? and is_manual = 1";
        return this.template.retrieveData(query, new TermIndexMapper(), new Object[]{docId});
    }

    public List<TermIndex> getIndexesByIsTrainPagination(boolean isTrain, int page, int size) {
        String query = "SELECT * FROM term_index where is_train = ? LIMIT ?, ?";
        return this.template.retrieveData(query, new TermIndexMapper(),
                new Object[]{isTrain, (page - 1) * size, size});
    }

    public List<TermIndex> getIndexesByIsTrainPagination(int docId, boolean isTrain, int page, int size) {
        String query = "SELECT * FROM term_index where is_train = ? and doc_id = ? LIMIT ?,?";
        return this.template.retrieveData(query, new TermIndexMapper(),
                new Object[]{isTrain, docId, (page - 1) * size, size});
    }

    public boolean isExistsByDocIdAndTerm(int docId, String term) {
        String query = "SELECT doc_id FROM term_index where doc_id = ? and term = ?";
        return template.queryForExistence(query, new Object[]{docId, term});
    }

    public boolean updateIsManualKP(int docId, String term, boolean isManual) {
        String query = "UPDATE term_index SET is_manual = ? where doc_id = ? and term = ?";
        return template.executeQuery(query, new Object[]{isManual, docId, term}) > 0;
    }

    public boolean updateTermByBatch(Map<String, String> termMap) {
        String query = "UPDATE term_index SET term = ? where term = ?;";

        List<Object[]> paramList = new ArrayList<>();
        termMap.forEach((key, value) -> paramList.add(new Object[]{value, key}));

        return template.executeBatchUpdate(query, paramList) > 0;
    }

    public List<String> getDistinctTermsByPagination(int page, int size) {
        try {
            String query = "SELECT DISTINCT term FROM term_index LIMIT ?,?";
            this.ps = dbHandler.getConnection().prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            this.ps.setInt(1, (page - 1) * size);
            this.ps.setInt(2, size);

            this.rs = this.dbHandler.retriveResultset(this.ps);
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
            this.ps = dbHandler.getConnection().prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            this.ps.setInt(1, docId);

            this.rs = this.dbHandler.retriveResultset(this.ps);
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
            stmnt = dbHandler.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

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

    public int getTermCountByDoc(int docId) {
        try {
            String query = "SELECT count(*) as total FROM term_index where doc_id = ?";
            this.ps = dbHandler.getConnection().prepareStatement(query);
            this.ps.setInt(1, docId);

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

    public int getTrainTermCount() {
        try {
            String query = "SELECT count(*) as total FROM term_index where is_train = 1";
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

    public int getTestTermCount() {
        try {
            String query = "SELECT count(*) as total FROM term_index where is_train = 0";
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

    public int getDocCount() {
        try {
            String query = "SELECT count(DISTINCT doc_id) as total FROM term_index";
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

    public int getTrainDocCount() {
        try {
            String query = "SELECT count(DISTINCT doc_id) as total FROM term_index where is_train = 1";
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

    public int getTestDocCount() {
        try {
            String query = "SELECT count(DISTINCT doc_id) as total FROM term_index where is_train = 0";
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

    public List<Integer> getDocIds(boolean isTrain) {
        try {
            String query = "SELECT DISTINCT doc_id FROM term_index where is_train = ?";
            this.ps = dbHandler.getConnection().prepareStatement(query);
            this.ps.setBoolean(1, isTrain);

            this.rs = this.dbHandler.retriveResultset(this.ps);

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
            this.ps = dbHandler.getConnection().prepareStatement(query);
            this.ps.setInt(1, index.getDocId());
            this.ps.setString(2, index.getTerm());
            this.ps.setBoolean(3, index.isManual());
            this.ps.setBoolean(4, index.isTrain());
            this.ps.setInt(5, index.getTf());
            this.ps.setInt(6, index.getDf());
            this.ps.setDouble(7, index.getPs());
            this.ps.setDouble(8, index.getNounFreq());

            this.dbHandler.executeQuery(this.ps);
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
            this.ps = dbHandler.getConnection().prepareStatement(query);
            this.ps.setInt(1, index.getDocId());
            this.ps.setString(2, index.getTerm());

            this.dbHandler.executeQuery(this.ps);
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
            dbHandler.getConnection().setAutoCommit(false);

            String query = "INSERT INTO term_index(doc_id, term, is_manual, is_train, tf, df, ps, noun_freq) VALUES (?,?,?,?,?,?,?,?)";
            this.ps = dbHandler.getConnection().prepareStatement(query.toString());

            final int batchSize = 20000;
            int count = 0;

            for (TermIndex index : termIndexes) {
                this.ps.setInt(1, index.getDocId());
                this.ps.setString(2, index.getTerm());
                this.ps.setBoolean(3, index.isManual());
                this.ps.setBoolean(4, index.isTrain());
                this.ps.setInt(5, index.getTf());
                this.ps.setInt(6, index.getDf());
                this.ps.setDouble(7, index.getPs());
                this.ps.setDouble(8, index.getNounFreq());
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
            statement = this.dbHandler.getConnection().createStatement();
            this.dbHandler.getConnection().setAutoCommit(false);

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
            this.dbHandler.getConnection().commit();

            return true;
        } catch (IllegalStateException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                    this.dbHandler.getConnection().setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            leaveGracefully();
        }
        return false;
    }

    public class TermIndexMapper implements RowMapper<TermIndex> {
        @Override
        public TermIndex mapRow(ResultSet rs) throws SQLException {
            TermIndex term = new TermIndex(rs.getInt("doc_id"));
            term.setTerm(rs.getString("term"));
            term.setManual(rs.getBoolean("is_manual"));
            term.setTrain(rs.getBoolean("is_train"));
            term.setTf(rs.getInt("tf"));
            term.setDf(rs.getInt("df"));
            term.setPs(rs.getDouble("ps"));
            term.setNounFreq(rs.getDouble("noun_freq"));

            return term;
        }
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
