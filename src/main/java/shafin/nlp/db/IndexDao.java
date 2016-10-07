package shafin.nlp.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import shafin.nlp.util.Logger;

public class IndexDao extends BasicDao<TermIndex> {

	public IndexDao(SQLiteDBConn dbConn) {
		super(dbConn);
	}

	public void createTable() {
		File discardFile = new File(SQLiteDBConn.DISCARDED_FILE);
		if (!discardFile.exists()) {
			try {
				discardFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/* Creating the Query */
		StringBuilder SQL = new StringBuilder("CREATE TABLE term_index(");
		SQL.append("doc_id INT NOT NULL,").append(" ").append("term TEXT NOT NULL,").append(" ")
				.append("tf INT NOT NULL,").append(" ").append("df INT NULL,").append(" ").append("ps INT NOT NULL);")
				.append("CREATE UNIQUE INDEX idx_term ON term(doc_id,term);");

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
		File discardFile = new File(SQLiteDBConn.DISCARDED_FILE);
		if (discardFile.exists()) {
			discardFile.delete();
		}

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
				term.setTf(rs.getInt("tf"));
				term.setDf(rs.getInt("df"));
				term.setPs(rs.getInt("ps"));

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

	public List<String> getDistinctTermsByPagination(int page, int size) {
		Statement stmnt = null;
		try {
			String query = "SELECT DISTINCT term FROM term_index LIMIT " + (page - 1) * size + ", " + size;
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

	public boolean insertTerm(int docId, String term, int tf, int ps) {
		try {
			String query = "INSERT INTO term_index(doc_id, term, tf, ps) VALUES (?,?,?,?)";
			this.qs = DB_CONN.getConnection().prepareStatement(query);
			this.qs.setInt(1, docId);
			this.qs.setString(2, term);
			this.qs.setInt(3, tf);
			this.qs.setInt(4, ps);

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

	public boolean insertTerm(List<TermIndex> termIndexes) {
		StringBuffer query = null;
		try {
			query = new StringBuffer("INSERT INTO term_index(doc_id, term, tf, ps) VALUES  ");

			int size = termIndexes.size();
			for (int i = 0; i < size; i++) {
				TermIndex index = termIndexes.get(i);
				query.append(" (" + index.getDocId() + ",'" + index.getTerm() + "'," + index.getTf() + ","
						+ index.getPs() + ")");
				if (i + 1 != size) {
					query.append(",");
				}
			}

			this.qs = DB_CONN.getConnection().prepareStatement(query.toString());
			this.DB_CONN.executeQuery(this.qs);

			return true;

		} catch (IllegalStateException | SQLException e) {
			System.out.println(query);
			e.printStackTrace();
		} finally {
			leaveGracefully();
		}
		return false;

	}

	public boolean insertAsDiscardedTerm(TermIndex index) throws IOException {
		File discardFile = new File(SQLiteDBConn.DISCARDED_FILE);
		// if file doesnt exists, then create it
		if (!discardFile.exists()) {
			discardFile.createNewFile();
		}

		String textData = index.getDocId() + ": " + index.getTerm() + "\n";
		FileOutputStream fileOutputStream = new FileOutputStream(discardFile, true);

		try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF8")) {
			textData = textData.replaceAll("\n", System.lineSeparator());
			outputStreamWriter.write(textData);

			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateDF() {
		Statement statement = null;
		try {
			statement = this.DB_CONN.getConnection().createStatement();
			this.DB_CONN.getConnection().setAutoCommit(false);

			Logger.print("TOTAL UNIQUE TERMS : " + getDistictTermCount());

			/* DROP Temp tables if exists */
			statement.addBatch("DROP TABLE IF EXISTS _temp;");
			statement.addBatch("DROP TABLE IF EXISTS _term_index;");

			/* CREATE _temp and _temp_index tables */
			statement.addBatch("CREATE TABLE _temp(term TEXT, df INT);");
			statement.addBatch("CREATE TABLE _term_index(doc_id INT NOT NULL,"
					+ "term TEXT NOT NULL,tf INT NOT NULL,df INT NULL,ps INT NOT NULL);");

			/*
			 * INSERT _temp the term wise document count and by joing with
			 * term_index saving it to _term_index
			 */
			statement.addBatch("INSERT INTO _temp SELECT term, count(doc_id) as df FROM term_index group by term;");
			statement.addBatch("INSERT INTO _term_index(doc_id, term, tf, df, ps) SELECT i.doc_id, i.term, i.tf, t.df, i.ps  "
					+ "FROM term_index  i, _temp t where t.term = i.term;");

			/* EMPTY the original term_index and copy from _term_index */
			statement.addBatch("DELETE FROM term_index;");
			statement.addBatch("INSERT INTO term_index(doc_id, term, tf, df, ps) SELECT * FROM _term_index;");

			/* DROP Temp tables if exists */
			statement.addBatch("DROP TABLE IF EXISTS _temp; DROP TABLE IF EXISTS _term_index;");

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

}
