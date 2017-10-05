package net.shafin.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.shafin.nlp.corpus.model.TermIndex;
import net.shafin.nlp.corpus.model.TermValue;
import net.shafin.nlp.db.IndexService;
import net.shafin.common.util.Logger;
import net.shafin.common.util.FileHandler;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
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

    public void convertToCSV(int docLimit) throws IOException {
        int numDocs = TYPE == DatasetType.TRAIN ? indexService.countTrainDocs() : indexService.countTestDocs();

        String fileName = TYPE.name() + ".csv";
        File file = new File(OUT_PUT_PATH + "\\" + fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        int count = 0;
        boolean isTrain = TYPE == DatasetType.TRAIN ? true : false;
        List<Integer> docIdList = indexService.getDocIds(isTrain);
        List<TermValue> valueList = new ArrayList<>();


        for (Integer docId : docIdList) {
            count++;
            if (count > docLimit) {
                break;
            }

            List<TermIndex> indexes = indexService.getTermIndexesByDocId(docId);
            List<TermIndex> balancedIndexes = TYPE == DatasetType.TRAIN ? equalizeDataClass(indexes) : indexes;

            for (TermIndex index : balancedIndexes) {
                TermValue value = new TermValue(index, numDocs);
                valueList.add(value);
                Logger.print("LOADING : " + TYPE.name() + " : " + index.getTerm());
            }
        }

        System.out.println(numDocs + " : " + docIdList.size() + " : " + valueList.size());
        Logger.print("NORMALIZING TERM VALUES...");
        valueList = TermValue.normalizeTermValueList(valueList);

        for (TermValue value : valueList) {
            String data = value.toCsvString();
            FileHandler.appendFile(OUT_PUT_PATH + "\\" + fileName, data + "\n");
            Logger.print("WRITTING : " + data);
        }
    }

    public List<TermIndex> equalizeDataClass(List<TermIndex> indexes) {
        List<TermIndex> balanced = new ArrayList<>();
        int positive = 0;

        List<TermIndex> autoTerms = new ArrayList<>();

        for (TermIndex index : indexes) {
            if (index.isManual()) {
                balanced.add(index);
                positive++;
            } else {
                autoTerms.add(index);
            }
        }

		/*Collections.sort(autoTerms);

		for(TermIndex index : autoTerms){
			if(positive < 1){
				break;
			}
			
			balanced.add(index);
			positive--;
		}*/

        Random random = new Random();
        List<Integer> temp = new ArrayList<>();
        while (positive > 0) {
            int i = random.nextInt(autoTerms.size());
            TermIndex index = indexes.get(i);

            if (!temp.contains(i)) {
                balanced.add(index);
                temp.add(i);
                positive--;
            }
        }
        return balanced;
    }

    public static void main(String[] args) throws IOException {
        DataSetGenerator dataSetGenerator = new DataSetGenerator(DatasetType.TEST, "D:/home/dw/json/QUALIFIED");
        dataSetGenerator.convertToCSV(120);

        dataSetGenerator = new DataSetGenerator(DatasetType.TRAIN, "D:/home/dw/json/QUALIFIED");
        dataSetGenerator.convertToCSV(600);
    }

}
