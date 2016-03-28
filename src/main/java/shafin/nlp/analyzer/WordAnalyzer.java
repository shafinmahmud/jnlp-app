package shafin.nlp.analyzer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import shafin.nlp.corpus.model.Document;
import shafin.nlp.tokenizer.Tokenizer;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;
import shafin.nlp.util.MapUtil;

public class WordAnalyzer {

	private String CORPUS_LOC = "D:/home/corpus/test/sample/";
	private String STOP_WORD_LOC = "D:/home/corpus/stopwords.txt";

	public WordAnalyzer(String corpusPath) {
		this.CORPUS_LOC = corpusPath;
	}

	public Map<String, Integer> getUniqueWordsWithFrequency() throws JsonParseException, JsonMappingException, IOException {

		Map<String, Integer> termFrequency = new HashMap<>();

		Iterator<String> fileIterator = FileHandler.getRecursiveFileList(CORPUS_LOC).iterator();

		while (fileIterator.hasNext()) {
			File jsonFile = new File(fileIterator.next());

			if (jsonFile.getName().endsWith(".json")) {

				JsonProcessor processor = new JsonProcessor(jsonFile);

				System.out.print(jsonFile.getName() + " : ");

				Document doc = (Document) processor.convertToModel(Document.class);

				String article = doc.getArticle();
				List<String> tokens = Tokenizer.getTokenizedBnList(article);

				for (String token : tokens) {
					int count = termFrequency.containsKey(token) ? termFrequency.get(token) : 0;
					termFrequency.put(token, count + 1);
				}
				System.out.println(tokens.size());
			}

		}

		System.out.println("End of reading . Total term " + termFrequency.size());

		return MapUtil.sortByValueDecending(termFrequency);
	}

	
	public double countStopWordOccurence(Map<String, Integer> termFrequency){
		
		int countOccurence = 0;
		List<String> stopWords =  FileHandler.readFile(STOP_WORD_LOC);					
		Set<String> wordSet = termFrequency.keySet();
		
		for(String stopWord : stopWords){		
			if(wordSet.contains(stopWord)){
				countOccurence += termFrequency.get(stopWord).intValue();
			}
			
		}
		
		int totalWords = MapUtil.getSumOftheValues(termFrequency);
		System.out.println(countOccurence+"/"+totalWords);
		return countOccurence*100/totalWords;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		
		String loc = "D:/home/corpus/test/sample/";
		WordAnalyzer wordAnalyzer = new WordAnalyzer(loc);
		
		Map<String, Integer> map =  wordAnalyzer.getUniqueWordsWithFrequency();
		
		StringBuilder sBuilder = new StringBuilder();
		for(String word : map.keySet()){
			sBuilder.append(word+"\n");
			//sBuilder.append(word).append(" : ").append(map.get(word)).append("\n");
		}
		
		//System.out.println(wordAnalyzer.countStopWordOccurence(map));
		FileHandler.writeFile(loc + "manual_freq-terms.txt",sBuilder.toString());
	}

}
