package shafin.nlp.analyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import shafin.nlp.tokenizer.SentenceSpliter;
import shafin.nlp.util.FileHandler;

public class NGramCandidate {

	private final String STOP_WORDS_URL = "D:/home/corpus/stopwords.txt";

	private List<String> STOP_WORDS;
	private String DOCUMENT_TEXT;

	public NGramCandidate(String text) {
		this.DOCUMENT_TEXT = text;
		this.STOP_WORDS = FileHandler.readFile(STOP_WORDS_URL);
	}

	public List<String> getNGramCandidateKeysFilteringSW(int min, int max) throws IOException {

		List<String> nGramCandidates = new ArrayList<>();
		List<String> sentenceList = SentenceSpliter.getSentenceTokenListBn(DOCUMENT_TEXT);

		for (String sentence : sentenceList) {
			/* tuning NGram for uni- bi- tri gram */
			NGramAnalyzer analyzer = new NGramAnalyzer(new StringReader(sentence), min, max);
			List<String> nGramTokens = analyzer.getNGramTokens();
			analyzer.close();

			/*
			 * filter NGram for removing tokens starts or ends with stop-words
			 */
			for (String token : nGramTokens) {
				boolean accept = true;
				for (String stop : STOP_WORDS) {
					if (token.startsWith(stop) | token.endsWith(stop)) {
						accept = false;
					}
				}
				if (accept) {
					nGramCandidates.add(token);
				}
			}
		}

		return nGramCandidates;
	}

	public static void main(String[] args) throws IOException {

		String text = "নোয়াখালীর হাতিয়া উপজেলার চরকিং ইউনিয়নে ভোট কেন্দ্রে  যাওয়ার পথে আজ মঙ্গলবার সকাল সাতটার দিকে দুই নির্বাচনী কর্মকর্তাকে গুলি করেছে দুর্বৃত্তরা। দাসপাড়া বালিকা উচ্চবিদ্যালয় কেন্দ্রের কাছাকাছি এ ঘটনা ঘটে।"
				+ "গুলিবিদ্ধ দু’জন হলেন, ওই কেন্দ্রের সহকারী প্রিসাইডিং কর্মকর্তা আব্দুল আউয়াল ও পোলিং কর্মকর্তা শাহাদাত হোসেন। আহত অবস্থায় কেন্দ্রে যাওয়ার পর তাঁদের হাতিয়া উপজেলা স্বাস্থ্য কমপ্লেক্সে নেওয়া হয়।"
				+ "নোয়াখালীর পুলিশ সুপার মো. ইলিয়াস শরিফ বিষয়টি নিশ্চিত করেছেন। কারা তাঁদের গুলি করেছে শেষ খবর পাওয়া পর্যন্ত কিছু জানা যায়নি। ওই কেন্দ্রে ভোটগ্রহণ চলছে। ";
		NGramCandidate candidate = new NGramCandidate(text);
		List<String> candiKeys = candidate.getNGramCandidateKeysFilteringSW(2, 3);

		for (String string : candiKeys) {
			System.out.println(string);
		}
	}
}
