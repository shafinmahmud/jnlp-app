package net.shafin.nlp.ann;

import net.shafin.common.util.Logger;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

import java.io.File;
import java.io.IOException;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class MLPLinearClassifier {

    private final File TRAIN_FILE;
    //private final File TEST_FILE;

    private static final int batchSize = 50;
    private static final int nEpochs = 30;

    private static final int numInputs = 4;
    private static final int numOutputs = 2;
    private static final int numHiddenNodes = 20;

    public MLPLinearClassifier(String trainFilePath) {
        this.TRAIN_FILE = new File(trainFilePath);
        //	this.TEST_FILE = new File(testFilePath);
    }

    public MultiLayerNetwork train() throws IOException, InterruptedException {
        DataSetIterator trainIter = getDatasetIterator(TRAIN_FILE, batchSize);

        MultiLayerConfiguration conf = getDeepDenseLayerNetworkConfiguration();
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(200));
        // Print score every 200 parameter updates

        for (int n = 0; n < nEpochs; n++) {
            model.fit(trainIter);
        }

        return model;
    }


    public Evaluation test(MultiLayerNetwork model, File testFile) throws IOException, InterruptedException {
        DataSetIterator testIter = getDatasetIterator(testFile, batchSize);

        Logger.print("EVALUATING THE MODEL...");
        Evaluation eval = new Evaluation(numOutputs);
        while (testIter.hasNext()) {
            DataSet t = testIter.next();
            INDArray features = t.getFeatureMatrix();
            INDArray lables = t.getLabels();
            INDArray predicted = model.output(features, false);

            eval.eval(lables, predicted);
        }
        return eval;
    }

    public INDArray generatePrediction(MultiLayerNetwork model, double[][] values) {
        INDArray allPoints = Nd4j.create(values);
        INDArray predictionsPoints = model.output(allPoints);
        return predictionsPoints;
    }

    public DataSetIterator getDatasetIterator(File csvDataFile, int batchSize)
            throws IOException, InterruptedException {
        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(csvDataFile));
        return new RecordReaderDataSetIterator(rr, batchSize, 0, 2);
    }

    public MultiLayerConfiguration getDeepDenseLayerNetworkConfiguration() {
        final int seed = 123;

		/*
         * LEARNING RATE: The learning rate, or step rate, is the rate at which
		 * a function steps through the search space. The typical value of the
		 * learning rate is between 0.001 and 0.1. Smaller steps mean longer
		 * training times, but can lead to more precise results.
		 */
        final double learningRate = 0.01;

		/*
         * MOMENTUM: Momentum is an additional factor in determining how fast an
		 * optimization algorithm converges on the optimum point. If you want to
		 * speed up the training, increase the momentum. But you should know
		 * that higher speeds can lower a model’s accuracy. To dig deeper,
		 * momentum is a variable between 0 and 1 that is applied as a factor to
		 * the derivative of the rate of change of the matrix. It affects the
		 * change rate of the weights over time.
		 */
        final double momentum = 0.9;
        return new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(1)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(learningRate)
                .updater(Updater.NESTEROVS)
                .updater(new Nesterovs(momentum))
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(numInputs)
                        .nOut(numHiddenNodes)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER)
                        .nIn(numHiddenNodes)
                        .nOut(numOutputs).build())
                .pretrain(false).backprop(true).build();
    }
}
