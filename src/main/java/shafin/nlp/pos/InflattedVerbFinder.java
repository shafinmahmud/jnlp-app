package shafin.nlp.pos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import shafin.nlp.corpus.CorpusIO;
import shafin.nlp.corpus.model.Document;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;

public class InflattedVerbFinder {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		PosTagger tagger = new PosTagger();
		Set<String> VERB_SET = new HashSet<>();
		
		String courpusPath = "D:/home/dw/json/test/";
		CorpusIO io = new CorpusIO(courpusPath, ".json", true);
		
		int docCount = 0;
		
		Iterator<String> iter = io.getDocumentPaths();
		while(iter.hasNext()){
			String path = iter.next();
			JsonProcessor processor = new JsonProcessor(new File(path));
			Document document = (Document) processor.convertToModel(Document.class);
			
			tagger.setTEXT(document.getArticle());
			List<String> verbs = tagger.findVerbTaggedTokens();
			
			VERB_SET.addAll(verbs);
			System.out.println(docCount+" : "+(double)VERB_SET.size()/docCount);
			docCount++;
		}
		
		FileHandler.writeListToFile(courpusPath+"VERBS.txt", new ArrayList<>(VERB_SET));
	}
}
