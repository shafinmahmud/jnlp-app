package shafin.nlp.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import shafin.nlp.corpus.model.TermIndex;
import shafin.nlp.corpus.model.TermValue;
import shafin.nlp.db.IndexService;
import shafin.nlp.util.MapUtil;

public class NGramRanking {

	public enum RankingApproach {
		TF_IDF, PFO, COMBINED
	}

	private final IndexService indexService;
	private final RankingApproach APPROACH;

	private final int KP_NUMBER;

	public NGramRanking(RankingApproach approach, int numKP) {
		this.indexService = new IndexService();
		this.APPROACH = approach;
		this.KP_NUMBER = numKP;
	}

	public Map<String, TermValue> generateAutomatedKP(int docID) throws IOException {
		Map<String, TermValue> termVector = getFeatureVector(docID);
		termVector = normalizeTermVector(termVector);

		termVector = rankVector(termVector, APPROACH);

		int count = 0;
		Map<String, TermValue> output = new LinkedHashMap<>();
		for (Map.Entry<String, TermValue> entry : termVector.entrySet()) {
			if(count > KP_NUMBER){
				break;
			}
			output.put(entry.getKey(), entry.getValue());
			count++;
		}
		return output;
	}

	private Map<String, TermValue> normalizeTermVector(Map<String, TermValue> termVector) {
		Map<String, Double> tfIdf = new HashMap<>();
		Map<String, Double> pfo = new HashMap<>();
		Map<String, Double> combined = new HashMap<>();

		for (Map.Entry<String, TermValue> entry : termVector.entrySet()) {
			TermValue value = entry.getValue();
			tfIdf.put(entry.getKey(), value.getTf_idf());
			pfo.put(entry.getKey(), value.getPfo());
			combined.put(entry.getKey(), value.getCombind());
		}

		tfIdf = MapUtil.normalizeMapValue(tfIdf);
		pfo = MapUtil.normalizeMapValue(pfo);
		combined = MapUtil.normalizeMapValue(combined);

		for (Map.Entry<String, TermValue> entry : termVector.entrySet()) {
			String term = entry.getKey();
			TermValue value = entry.getValue();
			value.setTf_idf(tfIdf.get(term));
			value.setPfo(pfo.get(term));
			value.setCombind(combined.get(term));
		}
		return termVector;
	}

	public Map<String, TermValue> rankVector(Map<String, TermValue> termVector, RankingApproach approach) {
		Map<String, Double> vector = new HashMap<>();
		switch (APPROACH) {
		case COMBINED:
			for (Map.Entry<String, TermValue> entry : termVector.entrySet()) {
				vector.put(entry.getKey(), entry.getValue().getCombind());
			}
			break;

		case TF_IDF:
			for (Map.Entry<String, TermValue> entry : termVector.entrySet()) {
				vector.put(entry.getKey(), entry.getValue().getTf_idf());
			}
			break;
		case PFO:
			for (Map.Entry<String, TermValue> entry : termVector.entrySet()) {
				vector.put(entry.getKey(), entry.getValue().getPfo());
			}
			break;
		default:
			for (Map.Entry<String, TermValue> entry : termVector.entrySet()) {
				vector.put(entry.getKey(), entry.getValue().getCombind());
			}
			break;
		}

		vector = MapUtil.sortByValueDecending(vector);

		Map<String, TermValue> sortedTermValue = new LinkedHashMap<>();
		for (Map.Entry<String, Double> entry : vector.entrySet()) {
			sortedTermValue.put(entry.getKey(), termVector.get(entry.getKey()));
		}
		return sortedTermValue;
	}

	public Map<String, TermValue> getFeatureVector(int docId) throws IOException {
		Map<String, TermValue> termVector = new HashMap<>();
		List<TermIndex> terms = indexService.getIndexTerm(docId);
		int numDocs = indexService.countDocs();

		for (TermIndex term : terms) {
			termVector.put(term.getTerm(), new TermValue(term, numDocs));
		}

		return termVector;
	}

	public static void main(String[] args) throws IOException {
		NGramRanking ranking = new NGramRanking(RankingApproach.COMBINED, 10);
		Map<String, TermValue> map = ranking.generateAutomatedKP(94);
		for(Map.Entry<String, TermValue> entry: map.entrySet()){
			System.out.println(entry.getKey()+" : "+entry.getValue().getCombind()+" > "+entry.getValue().isManual());
		}
	}
}
