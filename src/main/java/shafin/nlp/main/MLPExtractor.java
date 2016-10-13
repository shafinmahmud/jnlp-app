package shafin.nlp.main;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

import shafin.nlp.ann.MLPLinearClassifier;
import shafin.nlp.corpus.model.TermIndex;
import shafin.nlp.corpus.model.TermValue;
import shafin.nlp.db.IndexService;
import shafin.nlp.util.Logger;

public class MLPExtractor {

	private final IndexService indexService;
	private final MLPLinearClassifier classifier;
	private final MultiLayerNetwork model;

	private final String trainCSV = "D:/home/dw/json/QUALIFIED/TRAIN500.csv";
	private final String testCSV = "D:/home/dw/json/QUALIFIED/TEST100.csv";

	public MLPExtractor() throws IOException, InterruptedException {
		this.indexService = new IndexService();
		this.classifier = new MLPLinearClassifier(trainCSV, testCSV);
		this.model = this.classifier.train();
	}

	public void evaluate() throws IOException, InterruptedException {
		Evaluation evaluation = this.classifier.test(model);
		System.out.println(evaluation.stats());
	}

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

		Logger.print("NORMALIZING TERM VALUES...");

		valueList = TermValue.normalizeTermValueList(valueList);
		return valueList;
	}

	public void automatedKPFromTestSet(int docId) {
		List<TermValue> valueList = loadTermList(docId);
		for (TermValue value : valueList) {
			double[][] vec = new double[1][3];
			vec[0][0] = value.getTf();
			vec[0][1] = value.getIdf();
			vec[0][2] = value.getPfo();

			INDArray ind = this.classifier.generatePrediction(model, vec);//e.g. [[0.37, 0.63]] 
			double prob = ind.getColumn(1).getDouble(0); 
			value.setProbability(prob);
		}

		Collections.sort(valueList);
		for (TermValue value : valueList) {
			String status = value.isManual() ? "OK!" : "   ";
			System.out.println(status + " : " + value.getTerm() + " : " + value.getProbability());
		}

	}

	public static void main(String[] args) throws IOException, InterruptedException {
		MLPExtractor extractor = new MLPExtractor();
		extractor.evaluate();
		extractor.automatedKPFromTestSet(10001);
	}
}
