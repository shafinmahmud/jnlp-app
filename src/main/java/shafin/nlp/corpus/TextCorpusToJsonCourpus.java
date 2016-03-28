package shafin.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import shafin.nlp.corpus.model.Document;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;

public class TextCorpusToJsonCourpus {

	private static String CORPUS_LOC = "D:/home/corpus/ratul/";

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {

		File directory = new File(CORPUS_LOC);

		File[] folders = directory.listFiles();
		for (File folder : folders) {

			String category = folder.getName();

			File[] docs = new File(folder.getPath()).listFiles();
			
			for(File doc : docs){
				
				/* process doc */
				System.out.println(category+" : "+doc.getName());
				String text = FileHandler.readFileAsSingleString(doc.getAbsolutePath());
				
				Document document = new Document();
				List<String> categories = new ArrayList<>();
				categories.add(category);
				document.setCategories(categories);
				document.setArticle(text);
				document.setTitle("");
				document.setLang("BN");
				document.setSource("");
				document.setUrl("");
				
				document.setManualKeyphrases(new ArrayList<String>());
				
				HashMap<String, List<String>> autoKeypharases = new HashMap<>();
				document.setAutomaticKeyphrases(autoKeypharases);
				
				JsonProcessor jsonProcessor = new JsonProcessor();
				String json = jsonProcessor.convertToJson(document);
				
				FileHandler.writeFile("D:/home/corpus/ratul_json/"+category+"/"+category+"_"+doc.getName()+".json", json);
			}

		}

	}

}
