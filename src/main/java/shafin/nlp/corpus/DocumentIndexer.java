package shafin.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import shafin.nlp.tokenizer.NGramAnalyzer;
import shafin.nlp.tokenizer.SentenceSpliter;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;
import shafin.nlp.util.Logger;

/*
 * Author : Shafin Mahmud
 * Email  : shafin.mahmud@gmail.com
 * Date	  : 02-10-2016 SUN
 */
public class DocumentIndexer {

	public static final String ID = "id";
	public static final String CONTENT = "content";

	private final String CORPUS_DIRECTORY;
	private final String INDEX_STORE_PATH = "D:/home/dw/indx/";
	private final String EXTENSION = ".json";

	private final int MIN_NGRAM = 2;
	private final int MAX_NGRAM = 3;

	private final Directory directory;

	private long docID;

	public DocumentIndexer(String corpusDir) throws IOException {
		this.CORPUS_DIRECTORY = corpusDir;
		this.directory = FSDirectory.open(new File(INDEX_STORE_PATH).toPath());
		cleanIndexDirectory();
		iterAndIndexDocuments();
	}

	private void cleanIndexDirectory() throws IOException {
		List<String> filePaths = FileHandler.getRecursiveFileList(INDEX_STORE_PATH);
		for (String filePath : filePaths) {
			File file = new File(filePath);
			file.delete();
		}
	}

	private void iterAndIndexDocuments() throws JsonParseException, JsonMappingException, IOException {
		List<String> filePaths = FileHandler.getRecursiveFileList(CORPUS_DIRECTORY);
		for (String filePath : filePaths) {
			if (filePath.endsWith(EXTENSION)) {

				Logger.print("INDEXING : " + filePath);
				JsonProcessor jsonProcessor = new JsonProcessor(new File(filePath));
				shafin.nlp.corpus.model.Document document = (shafin.nlp.corpus.model.Document) jsonProcessor
						.convertToModel(shafin.nlp.corpus.model.Document.class);

				final String article = document.getArticle();
				createIndex(article);
			}
		}
	}

	private void createIndex(final String TEXT) throws IOException {
		List<String> SENTENCES = SentenceSpliter.getSentenceTokenListBn(TEXT);
		for (String sentence : SENTENCES) {

			System.out.println(sentence);
			Analyzer analyzer = new NGramAnalyzer(new StringReader(sentence), MIN_NGRAM, MAX_NGRAM);
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(directory, iwc);
			addDocument(docID, writer, TEXT);

			writer.close();
		}
		docID++;
	}

	private void addDocument(long docID, IndexWriter writer, String content) throws IOException {
		Document doc = new Document();
		doc.add(new VecTextField(ID, String.valueOf(docID), Store.YES));
		doc.add(new VecTextField(CONTENT, content, Store.YES));
		writer.addDocument(doc);
	}

	public Map<String, TermValue> getTFIDF(String docId) throws IOException {
		IndexReader reader = DirectoryReader.open(directory);
		Map<String, TermValue> termVector = new HashMap<>();

		/*
		 * Retrieve term vector for this document and field, or null if term
		 * vectors were not indexed.
		 */
		Terms vector = reader.getTermVector(Integer.valueOf(docId), CONTENT);

		TermsEnum termsEnum = vector.iterator();
		BytesRef text = null;

		long totalTerms = vector.size() < 0 ? 0 : vector.size();
		long totalDocs = reader.getDocCount(CONTENT);

		while ((text = termsEnum.next()) != null) {
			String term = text.utf8ToString();
			int freq = (int) termsEnum.totalTermFreq();
			int docFreq = reader.docFreq(new Term(CONTENT, text));

			TermValue values = new TermValue(totalTerms, totalDocs, freq, docFreq);
			termVector.put(term, values);
		}
		return termVector;
	}

	class TermValue {
		private final double tf;
		private final double idf;

		public TermValue(long totalTerms, long numDocs, int termFreq, int docFreq) {
			super();

			/*
			 * TF : Implemented as sqrt(freq). IDF : Implemented as
			 * log(numDocs/(docFreq+1)) + 1.
			 */
			TFIDFSimilarity similarity = new ClassicSimilarity();
			this.tf = similarity.tf(termFreq);
			this.idf = similarity.idf(docFreq, numDocs);
		}

		public double getTf() {
			return tf;
		}

		public double getIdf() {
			return idf;
		}

		@Override
		public String toString() {
			return this.tf + " : " + idf;
		}
	}

	public static void main(String[] args) throws IOException {
		String path = "D:/home/dw/json/test/";
		DocumentIndexer indexer = new DocumentIndexer(path);

		Map<String, TermValue> values = indexer.getTFIDF("1");
		for (Map.Entry<String, TermValue> entry : values.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue().toString());
		}
	}
}
