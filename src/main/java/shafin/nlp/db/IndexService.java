package shafin.nlp.db;

import java.io.IOException;
import java.util.List;

public class IndexService {

	private final IndexDao dao;

	public IndexService() {
		this.dao = new IndexDao(SQLiteDBConn.getSQLiteDBConn());
	}

	public void recreatIndex() {
		dao.deleteTable();
		dao.createTable();
	}

	public List<TermIndex> getIndexTerm(int docId) {
		return dao.getIndexesByDocID(docId);
	}

	public boolean insertIndex(TermIndex index){
		return dao.insertTerm(index.getDocId(), index.getTerm(), index.getTf(), index.getPs());
	}

	public boolean batchInsertIndex(List<TermIndex> termIndexes) {
		return dao.insertTermInBatch(termIndexes);
	}

	public int countDocs() {
		return dao.getDocCount();
	}

	public boolean updateDF() {
		return dao.updateDF();
	}

	public boolean enlistAsDiscardedTerm(TermIndex index) {
		try {
			return dao.insertAsDiscardedTerm(index);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
