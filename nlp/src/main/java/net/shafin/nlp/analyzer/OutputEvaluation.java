package net.shafin.nlp.analyzer;

import net.shafin.nlp.corpus.model.TermValue;
import net.shafin.nlp.db.IndexService;

import java.util.List;
import java.util.Map;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class OutputEvaluation {

    public static void evaluate(Map<Integer, List<TermValue>> testOutput) {
        IndexService indexService = new IndexService();
        int totalMatched = 0;
        int totalManual = 0;
        int totalAssigned = 0;

        for (Map.Entry<Integer, List<TermValue>> entry : testOutput.entrySet()) {
            totalManual = totalManual + indexService.getManualIndexesByDocId(entry.getKey()).size();
            List<TermValue> auto = entry.getValue();

            for (TermValue val : auto) {
                if (val.isManual()) {
                    totalMatched = totalMatched + 1;
                }
            }

            totalAssigned = totalAssigned + auto.size();
        }

        double precision = precision(totalAssigned, totalMatched);
        double recall = recall(totalManual, totalMatched);
        double f_meaure = fMeasure(precision, recall);

        System.out.println("PRECSION  : " + precision);
        System.out.println("RECALL    : " + recall);
        System.out.println("F-MEASURE : " + f_meaure);
    }

    public static double precision(int total_assigned, int total_matched) {
        return (double) total_matched / total_assigned;
    }

    public static double recall(int total_manual, int total_matched) {
        return (double) total_matched / total_manual;
    }

    public static double fMeasure(double precision, double recall) {
        double beta = 1;
        return (((beta * beta) + 1) * (precision * recall)) / (((beta * beta) * precision) + recall);
    }

    public static void main(String[] args) {
        System.out.println(fMeasure(0.2, 0.5));
    }
}
