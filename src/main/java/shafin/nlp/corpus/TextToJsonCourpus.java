package shafin.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import shafin.nlp.corpus.model.Document;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;

public class TextToJsonCourpus {

	// "D:/home/corpus/ratul/"
	private final String CORPUS_LOC;

	public TextToJsonCourpus(String corpusLocation) {
		this.CORPUS_LOC = corpusLocation;
	}

	public void process() throws JsonGenerationException, JsonMappingException, IOException {
		File directory = new File(CORPUS_LOC);
		File[] folders = directory.listFiles();

		for (File folder : folders) {
			String folderName = folder.getName();

			File[] docs = new File(folder.getPath()).listFiles();

			for (File doc : docs) {

				/* process doc */
				System.out.println(folderName + " : " + doc.getName());
				String text = FileHandler.readFileAsSingleString(doc.getAbsolutePath());

				Document d = new Document();
				d.setArticle(text);
				d.setLang("BN");
				d.setSource("");
				d.setUrl("");

				d.setManualKeyphrases(new ArrayList<String>());

				List<String> autoKeypharases = new ArrayList<>();
				d.setAutomaticKeyphrases(autoKeypharases);

				JsonProcessor jsonProcessor = new JsonProcessor();
				String json = jsonProcessor.convertToJson(d);

				FileHandler.writeFile(CORPUS_LOC + "/json/" + folderName + "/" + doc.getName() + ".json", json);
			}

		}
	}

}
