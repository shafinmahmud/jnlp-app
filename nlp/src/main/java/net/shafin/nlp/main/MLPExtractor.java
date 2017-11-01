package net.shafin.nlp.main;

import net.shafin.nlp.corpus.model.TermIndex;
import net.shafin.nlp.corpus.model.TermValue;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import net.shafin.nlp.ann.MLPLinearClassifier;
import net.shafin.nlp.db.IndexService;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class MLPExtractor {

    private final IndexService indexService;
    private final MLPLinearClassifier classifier;
    private final MultiLayerNetwork model;

    private final String trainCSV = "D:/home/dw/json/QUALIFIED/TRAIN.csv";
    //private final String testCSV = "D:/home/dw/json/QUALIFIED/TEST20.csv";

    public MLPExtractor() throws IOException, InterruptedException {
        this.indexService = new IndexService();
        this.classifier = new MLPLinearClassifier(trainCSV);
        this.model = this.classifier.train();
    }

    public String evaluate(File testCSV) throws IOException, InterruptedException {
        Evaluation evaluation = this.classifier.test(model, testCSV);
        return evaluation.stats();
    }

	/*private File writeTermValueToTempCSV(List<TermValue> termValues) throws IOException{
        File tempData = File.createTempFile("testdata", ".csv");
		tempData.deleteOnExit();
		
		List<String> csv = new ArrayList<>();
		for (TermValue value : termValues) {
			String data = value.toCsvString();
			csv.add(data);
		}
		FileUtil.writeListToFile(tempData.getAbsolutePath(), csv);
		return tempData;
	}*/

    private List<TermValue> loadTermList(int docId) {
        int numDocs = indexService.countTestDocs();
        int total = indexService.termCountByDoc(docId);
        int size = 1000;
        int totalPage = (total / size) + 1;

        List<TermValue> valueList = new LinkedList<>();

        for (int page = 1; page <= totalPage; page++) {
            List<TermIndex> indexes = indexService.getTestSet(docId, page, size);
            for (TermIndex index : indexes) {
                TermValue value = new TermValue(index, numDocs);
                valueList.add(value);
            }
        }

        //Logger.print("TESTING : docID : "+docId);

        valueList = TermValue.normalizeTermValueList(valueList);
        return valueList;
    }

    public List<TermValue> automatedKPFromTestSet(int docId, int limit) {
        List<TermValue> valueList = loadTermList(docId);
        for (TermValue value : valueList) {
            double[][] vec = new double[1][4];
            vec[0][0] = value.getTf();
            vec[0][1] = value.getIdf();
            vec[0][2] = value.getPfo();
            vec[0][3] = value.getNounFreq();

            INDArray ind = this.classifier.generatePrediction(model, vec);//e.g. [[0.37, 0.63]]
            double prob = ind.getColumn(1).getDouble(0);
            value.setProbability(prob);
        }

        Collections.sort(valueList);

        int count = 0;
        List<TermValue> topList = new LinkedList<>();

        for (TermValue value : valueList) {
            count++;
            if (count > limit) {
                break;
            }

            topList.add(value);
        }

        return topList;
    }

    public Map<Integer, List<TermValue>> automatedKPFromAllTestSet(int limitPerDoc, int totalDoc) {
        Map<Integer, List<TermValue>> docKPMap = new HashMap<>();
        List<Integer> testDocIds = indexService.getDocIds(false);

        int count = 0;
        for (int id : testDocIds) {
            count++;
            if (count > totalDoc) {
                break;
            }

            List<TermValue> valueList = new ArrayList<>();
            valueList.addAll(automatedKPFromTestSet(id, limitPerDoc));
            docKPMap.put(id, valueList);
        }
        return docKPMap;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //int tesDocs = 120;

        MLPExtractor extractor = new MLPExtractor();
        List<TermValue> terms = extractor.automatedKPFromTestSet(10103, 10);
        for (TermValue value : terms) {
            String remark = value.isManual() ? "MANUAL" : "      ";
            System.out.println(remark + " : " + value.getTerm() + " : " + value.getProbability());
        }

		/*System.out.println("[5]");
		Map<Integer, List<TermValue>>  testOutput = extractor.automatedKPFromAllTestSet(5, tesDocs);
		//String eval = extractor.evaluate(extractor.writeTermValueToTempCSV(termValues));		
		OutputEvaluation.evaluate(testOutput);
		
		System.out.println("\n[10]");
		testOutput = extractor.automatedKPFromAllTestSet(10, tesDocs);
		OutputEvaluation.evaluate(testOutput);
		
		System.out.println("\n[15]");
		testOutput = extractor.automatedKPFromAllTestSet(15, tesDocs);
		OutputEvaluation.evaluate(testOutput);
		
		System.out.println("\n[20]");
		testOutput = extractor.automatedKPFromAllTestSet(20, tesDocs);
		OutputEvaluation.evaluate(testOutput);*/
    }
}
