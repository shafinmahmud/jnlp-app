package shafin.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import shafin.nlp.analyzer.BanglaWordAnalyzer;
import shafin.nlp.util.MapUtil;


public class DocumentIndexer {
	
	public static final String CONTENT = "Content";
	
	private final String INDEX_STORE_PATH = "D:/home/dw/indx/";
	private final Directory directory;
	
	public DocumentIndexer(String doc) throws IOException {
		this.directory = createIndex(doc);
	}
	
	private Directory createIndex(String doc) throws IOException {
		Directory directory = FSDirectory.open(new File(INDEX_STORE_PATH).toPath());
		Analyzer analyzer = new BanglaWordAnalyzer(new StringReader(doc));
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(directory, iwc);
		addDocument(writer, doc);

		writer.close();
		return directory;
	}
	
	
	private void addDocument(IndexWriter writer, String content) throws IOException {
		Document doc = new Document();
		doc.add(new VecTextField(CONTENT, content, Store.YES));
		writer.addDocument(doc);
	}

	public Map<String, Integer> getTermFrequencies(int docId) throws IOException {
		IndexReader reader = DirectoryReader.open(directory);
		Terms vector = reader.getTermVector(docId, CONTENT);
		
		TermsEnum termsEnum = null;
		termsEnum = vector.iterator();
		Map<String, Integer> frequencies = new HashMap<>();
		BytesRef text = null;
		
		while ((text = termsEnum.next()) != null) {
			String term = text.utf8ToString();
			int freq = (int) termsEnum.totalTermFreq();
			frequencies.put(term, freq);
		}
		reader.close();
		return MapUtil.sortByValueDecending(frequencies);
	}
	
	public static void main(String[] args) throws IOException {
		String doc = "বাংলাদেশের নাগরিকদের মধ্যে আজ রোববার মধ্যে মধ্যে মধ্যে মধ্যে থেকে জাতীয় পরিচয়পত্র বা এনআইডির স্মার্টকার্ড বিতরণ শুরু হচ্ছে। চলবে পরবর্তী ৪০০ দিন।";
		DocumentIndexer indexer = new DocumentIndexer(doc);
		Map<String, Integer> values = indexer.getTermFrequencies(0);
		for(Map.Entry<String, Integer> entry : values.entrySet()){
			System.out.println(entry.getKey()+" : "+entry.getValue());
		}
	}
}
