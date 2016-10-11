package shafin.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import shafin.nlp.corpus.model.Document;
import shafin.nlp.db.IndexService;
import shafin.nlp.pfo.FeatureExtractor;
import shafin.nlp.tokenizer.SentenceSpliter;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;
import shafin.nlp.util.ListUtil;
import shafin.nlp.util.Logger;
import shafin.nlp.util.RegexUtil;
import shafin.nlp.util.StringTool;

public class DocumentQualifier {

	public static final int DOC_CRITERIA_SENTENCE_NUM = 20;
	public static final int DOC_CRITERIA_MKP_NUM = 5;
	public static final int SPLIT_DOC_IN = 500;

	private String JSON_CORPUS_DIR = "D:/home/dw/json/";
	private String QUALIFIED_DOC_DIR_ROOT = "";

	private List<String> EXISTING_FILE_NAMES;
	private List<String> EXISTING_FILES;
	private IndexService indexService;

	public DocumentQualifier() throws IOException {
		this.indexService = new IndexService();
		preprocess();
	}

	public DocumentQualifier(String path) throws IOException {
		this.JSON_CORPUS_DIR = path;
		this.indexService = new IndexService();
		preprocess();

	}

	private void preprocess() throws IOException {
		this.QUALIFIED_DOC_DIR_ROOT = (JSON_CORPUS_DIR.endsWith("/") || JSON_CORPUS_DIR.endsWith("\\"))
				? JSON_CORPUS_DIR + "QUALIFIED/" : JSON_CORPUS_DIR + "/QUALIFIED/";

		File dir = new File(QUALIFIED_DOC_DIR_ROOT);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		this.EXISTING_FILE_NAMES = new ArrayList<>();
		this.EXISTING_FILES = FileHandler.getRecursiveFileList(QUALIFIED_DOC_DIR_ROOT);
		for (String path : this.EXISTING_FILES) {
			String name = FileHandler.getFileNameFromPathStringWithExt(path);
			this.EXISTING_FILE_NAMES.add(name);
		}
	}

	public void iterAndQualify() throws IOException {
		List<String> filePaths = FileHandler.getRecursiveFileList(JSON_CORPUS_DIR);
		String CURRENT_DIR = QUALIFIED_DOC_DIR_ROOT;

		for (String filePath : filePaths) {

			if (filePath.endsWith(".json")) {

				String fileName = FileHandler.getFileNameFromPathString(filePath);
				int docID = Integer.valueOf(RegexUtil.getFirstMatch(fileName, "[0-9]+"));

				String intentedPath = CURRENT_DIR + fileName + ".json";
				if (!this.EXISTING_FILE_NAMES.contains(fileName + ".json")) {

					JsonProcessor jsonProcessor = new JsonProcessor(new File(filePath));
					Document document = (shafin.nlp.corpus.model.Document) jsonProcessor.convertToModel(Document.class);

					String article = StringTool.removeUnicodeSpaceChars(new StringBuilder(document.getArticle()));
					LinkedList<String> SENTENCES = SentenceSpliter.getSentenceTokenListBn(article);

					document = qualifyDocuemnt(document, SENTENCES);

					if (document != null) {
						document.setDocID(docID);
						indexService.writeDocumentToDisk(document, intentedPath);
						this.EXISTING_FILES.add(intentedPath);
						this.EXISTING_FILE_NAMES.add(FileHandler.getFileNameFromPathStringWithExt(intentedPath));
						Logger.print("WRITTEN : " + intentedPath);
					} else {
						Logger.print("DISQUALIFIED : " + filePath);
					}
				} else {
					Logger.print("EXISTS : " + intentedPath);
				}

			}

		}
		redistributeFiles();
	}

	private void redistributeFiles() throws IOException {
		int totalDoc = this.EXISTING_FILES.size();
		int folderNeeded = totalDoc % SPLIT_DOC_IN == 0 ? (totalDoc / SPLIT_DOC_IN) : (totalDoc / SPLIT_DOC_IN) + 1;

		List<String> FOLDERS = new ArrayList<>();
		for (int i = 0; i < folderNeeded; i++) {
			String FOLDER = QUALIFIED_DOC_DIR_ROOT + convertToAlphabetNumbering(i) + " " + SPLIT_DOC_IN + "/";
			File dir = new File(FOLDER);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FOLDERS.add(FOLDER);
		}

		List<String> NON_EMPTY_FOLDERS = FileHandler.getRecursiveNoneEmptyChildFolders(QUALIFIED_DOC_DIR_ROOT);
		if (!NON_EMPTY_FOLDERS.isEmpty()) {
			balanceFolderFiles(NON_EMPTY_FOLDERS);
		}

		ListUtil.removeAllStringItem(NON_EMPTY_FOLDERS, FOLDERS);

		for (String restOfFolder : FOLDERS) {
			for (int count = 0; count < SPLIT_DOC_IN; count++) {

				if (EXISTING_FILES.size() > 0) {
					String file = EXISTING_FILES.get(EXISTING_FILES.size() - 1);
					if (FileHandler.moveFile(file, restOfFolder)) {
						ListUtil.removeStringItem(file, EXISTING_FILES);
						Logger.print("MOVED IN : " + file + " to " + restOfFolder);
					}
				}
			}
		}

	}

	private void balanceFolderFiles(List<String> folders) throws IOException {
		for (String folder : folders) {
			List<String> folderFiles = FileHandler.getRecursiveFileList(folder);
			int folderFileCount = folderFiles.size();

			for (String folderFile : folderFiles) {
				ListUtil.removeStringItem(folderFile, EXISTING_FILES);
			}

			if (folderFileCount < SPLIT_DOC_IN) {
				/* move in some files to fill the Qouta */
				int neededFile = SPLIT_DOC_IN - folderFileCount;
				for (int count = 0; count < neededFile; count++) {

					if (EXISTING_FILES.size() > 0) {
						String file = EXISTING_FILES.get(EXISTING_FILES.size() - 1);
						if (FileHandler.moveFile(file, folder)) {
							ListUtil.removeStringItem(file, EXISTING_FILES);
							Logger.print("MOVED IN : " + file + " to " + folder);
						}
					}
				}

			} else if (folderFileCount > SPLIT_DOC_IN) {
				/* move out some files to match the Qouta */
				int excessFile = folderFileCount - SPLIT_DOC_IN;
				for (int count = 0; count < excessFile; count++) {

					if (EXISTING_FILES.size() > 0) {
						String file = folderFiles.get(folderFiles.size() - 1);
						if (FileHandler.moveFile(file, QUALIFIED_DOC_DIR_ROOT)) {
							Logger.print("MOVED OUT : " + file + " to " + QUALIFIED_DOC_DIR_ROOT);
						}
					}
				}
			}
		}
	}

	/*
	 * DOC CRITERIA 1: Document has to contains at least 30 Sentences; DOC
	 * CRITERIA 2: All Manual KP must have minimum Term-Frequency 1. If not then
	 * the certain KP will be discarded. DOC CRITERIA 3: Remaining number of KP
	 * has to be minimum 5.
	 */
	public Document qualifyDocuemnt(Document doc, LinkedList<String> SENTENCES) {
		if (doc.getArticle().trim().isEmpty()) {
			return null;
		}

		if (SENTENCES.size() < DOC_CRITERIA_SENTENCE_NUM) {
			return null;
		}

		List<String> manualKP = doc.getManualKeyphrases();
		List<String> trueKP = new ArrayList<>();
		for (String KP : manualKP) {
			int freq = FeatureExtractor.getTermOccurrenceCount(doc.getArticle(), KP);
			if (freq > 0) {
				trueKP.add(KP);
			}
		}

		if (trueKP.size() < DOC_CRITERIA_MKP_NUM) {
			return null;
		}

		doc.setManualKeyphrases(trueKP);
		return doc;
	}

	public static String convertToAlphabetNumbering(int decimalNumber) {
		StringBuffer sb = new StringBuffer();
		if (decimalNumber == 0) {
			sb.append(getCharForNumber(0));
		}
		while (decimalNumber != 0) {
			int temp = decimalNumber % 26;
			sb.append(getCharForNumber(temp));
			decimalNumber /= 26;
		}
		return sb.reverse().toString();
	}

	private static String getCharForNumber(int i) {
		if (i < 0) {
			return null;
		}
		char[] ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		return "" + ALPHABETS[i];
	}

	public static void main(String[] args) throws IOException {
		DocumentQualifier qualifier = new DocumentQualifier();
		qualifier.iterAndQualify();
	}
}
