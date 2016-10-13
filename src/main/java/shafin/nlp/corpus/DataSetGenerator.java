package shafin.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shafin.nlp.corpus.model.TermIndex;
import shafin.nlp.corpus.model.TermValue;
import shafin.nlp.db.IndexService;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.Logger;


/*
 * Author : Shafin Mahmud
 * Email  : shafin.mahmud@gmail.com
 * Date	  : 02-10-2016 SUN
 */
public class DataSetGenerator {

	public enum DatasetType {
		TRAIN, TEST
	}

	private final DatasetType TYPE;
	private final String OUT_PUT_PATH;
	private final IndexService indexService;

	public DataSetGenerator(DatasetType type, String outputPath) {
		this.indexService = new IndexService();
		this.OUT_PUT_PATH = FileHandler.getCanonicalPath(outputPath);
		this.TYPE = type;
	}

	public void convertToCSV() throws IOException {
		int numDocs = TYPE == DatasetType.TRAIN ? indexService.countTrainDocs() : indexService.countTestDocs();
		int total = TYPE == DatasetType.TRAIN ? indexService.trainTermCount() : indexService.testTermCount();
		int size = 1000;
		int totalPage = (total / size) + 1;

		String fileName = TYPE.name() + numDocs + ".csv";
		File file = new File(OUT_PUT_PATH + "\\" + fileName);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		List<TermValue> valueList = new ArrayList<>();

		for (int page = 1; page <= totalPage; page++) {
			List<TermIndex> indexes = TYPE == DatasetType.TRAIN ? indexService.getTrainSet(page, size)
					: indexService.getTestSet(page, size);
			List<TermIndex> balancedData = equalizeDataClass(indexes);
			for (TermIndex index : balancedData) {
				TermValue value = new TermValue(index, numDocs);
				valueList.add(value);
				Logger.print("LOADING : " + TYPE.name() + " : " + index.getTerm());
			}
		}

		Logger.print("NORMALIZING TERM VALUES...");
		valueList = TermValue.normalizeTermValueList(valueList);

		for (TermValue value : valueList) {
			String data  = value.toCsvString();
			FileHandler.appendFile(OUT_PUT_PATH + "\\" + fileName, data + "\n");
			Logger.print("WRITTING : "+data);
		}
	}


	public List<TermIndex> equalizeDataClass(List<TermIndex> indexes) {
		List<TermIndex> balanced = new ArrayList<>();
		int positive = 0;

		for (TermIndex index : indexes) {
			if (index.isManual()) {
				balanced.add(index);
				positive++;
			}
		}
		positive = 5*positive;
		
		Random random = new Random();
		while (positive > 0) {
			int i = random.nextInt(indexes.size());
			TermIndex index = indexes.get(i);
			if (!index.isManual()) {
				balanced.add(index);
				positive--;
			}
		}
		return balanced;
	}

	public static void main(String[] args) throws IOException {
		DataSetGenerator dataSetGenerator = new DataSetGenerator(DatasetType.TEST, "D:/home/dw/json/QUALIFIED");
		dataSetGenerator.convertToCSV();
		
		dataSetGenerator = new DataSetGenerator(DatasetType.TRAIN, "D:/home/dw/json/QUALIFIED");
		dataSetGenerator.convertToCSV();
	}

}
