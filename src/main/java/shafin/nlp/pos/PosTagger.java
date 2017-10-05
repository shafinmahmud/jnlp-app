package shafin.nlp.pos;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PosTagger {

	private static final String TAGGER_MODEL = "taggers/bengaliModelFile.tagger";
	private static final String[] VERB_TAG_LIST = { "VM", "VAUX" };

	private final MaxentTagger TAGGER;
	private final TokenizerFactory<CoreLabel> ptbTokenizerFactory;
	private String TEXT;

	public PosTagger(String text) throws ClassNotFoundException, IOException {
		this.TAGGER = new MaxentTagger(TAGGER_MODEL);
		this.ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "untokenizable=noneKeep");
		this.TEXT = text;
	}

	public PosTagger() throws ClassNotFoundException, IOException {
		this.TAGGER = new MaxentTagger(TAGGER_MODEL);
		this.ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "untokenizable=noneKeep");
	}

	public void setTEXT(String tEXT) {
		TEXT = tEXT;
	}

	public List<TaggedWord> getPosTaggedWords() {
		DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(new StringReader(TEXT));
		documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);

		List<TaggedWord> taggedWords = new ArrayList<>();
		for (List<HasWord> sentence : documentPreprocessor) {
			List<TaggedWord> tSentence = this.TAGGER.tagSentence(sentence);
			taggedWords.addAll(tSentence);
		}
		return taggedWords;
	}

	public List<String> findTaggedTokens(final String[] TAG_LIST) {
		List<String> filteredTokens = new ArrayList<>();
		List<TaggedWord> taggedWords = getPosTaggedWords();
		for (TaggedWord taggedWord : taggedWords) {
			
			for (String tag : TAG_LIST) {			
				if (taggedWord.tag().equals(tag)) {
					filteredTokens.add(taggedWord.word());
				}
			}
		}
		return filteredTokens;
	}

	public List<String> findVerbTaggedTokens() {
		return findTaggedTokens(VERB_TAG_LIST);
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// The sample string
		String sample = "‘আরাফাত সানির বোলিং অ্যাকশন অবৈধ’—কাল  সকাল সকাল  এই খবরটা হজম হতে না হতেই এল আরও বড় ধাক্কা। বিকেলেই আইসিসির ঘোষণা, কোনো কোনো ডেলিভারিতে তাসকিন আহমেদের কনুইও ১৫ ডিগ্রির বেশি বেঁকে যায়। সানির সঙ্গে তাই তিনিও বোলিং থেকে নিষিদ্ধ। তবে চেন্নাইয়ের রামাচন্দ্র বিশ্ববিদ্যালয়ের পরীক্ষাগারে তাসকিনের বোলিং অ্যাকশনের পরীক্ষায় যে প্রক্রিয়া অনুসরণ করা হয়েছে, প্রশ্ন উঠেছে সেটির বৈধতা নিয়েই।"
				+ "বিসিবি সূত্র জানিয়েছে, ১৫ মার্চের পরীক্ষায় মাত্র ৩-৪ মিনিটের মধ্যে তাসকিনকে ৮-৯টি বাউন্সার দিতে বলা হয়। এর মধ্যে তিনটি বাউন্সারে অবৈধ অ্যাকশন ধরা পড়েছে। এত অল্প সময়ে অতগুলো বাউন্সার দিতে হলে এ রকম হওয়াটা অস্বাভাবিকও নয়। মূলত এই পরীক্ষার ফলের ওপর ভিত্তি করেই তাসকিনের বোলিং নিষিদ্ধ ঘোষণা করে আইসিসি। কিন্তু নিয়ম হলো, ম্যাচের যে ডেলিভারিতে অ্যাকশন সন্দেহজনক মনে হয়েছে, পরীক্ষায় সেটাই করতে বলা হবে বোলারকে। সে অনুযায়ী পরীক্ষায় তাসকিনকে বাউন্সার দিতে বলার কথা নয়। হল্যান্ডের বিপক্ষে যে তিনি কোনো বাউন্সারই দেননি! বাউন্সারে তাঁর অ্যাকশন নিয়ে সন্দেহ ওঠারও তাই কোনো অবকাশ ছিল না ওই ম্যাচে। আইসিসির সিদ্ধান্তে আরেকটি নিয়মেরও ব্যত্যয় ঘটার অভিযোগ আছে। ‘স্টক ডেলিভারি’ ছাড়া অন্য কোনো ডেলিভারিতে কনুই ১৫ ডিগ্রির বেশি বাঁকা হলেও আইসিসি কোনো বোলারের বোলিং নিষিদ্ধ করতে পারে না বলে জানিয়েছে সূত্র। শুধু ভবিষ্যতের জন্য সতর্কই করতে পারে। তাসকিনের অ্যাকশন নিয়ে অভিযোগ তার গুড লেংথ অথবা ইয়র্কার বলগুলোর মতো ‘স্টক ডেলিভারি’তে নয় বলেও তাঁর বোলিংয়ের নিষেধাজ্ঞা নিয়ে প্রশ্ন তোলা যায়।"
				+ "দলের কম্পিউটার বিশ্লেষকদের মাধ্যমে হল্যান্ড ম্যাচের ভিডিও ফুটেজ বিশ্লেষণ করে এসব বিষয়ে আরও নিশ্চিত হন কোচ চন্ডিকা হাথুরুসিংহে। তাসকিনের বোলিং অ্যাকশন নিয়ে প্রশ্ন ওঠার পর তাঁর পরামর্শেই আইনজীবীদের শরণাপন্ন হয় বিসিবি। আম্পায়ারদের আনা অভিযোগ, রামাচন্দ্র বিশ্ববিদ্যালয়ের পরীক্ষাগারে হওয়া তাসকিনের বোলিং অ্যাকশন পরীক্ষা এবং আইসিসির চূড়ান্ত সিদ্ধান্তের যথার্থতা বিবেচনা করে দেখছেন আইনজীবীরা। এ ব্যাপারে বিসিবির আইনি পরামর্শক সুপ্রিম কোর্টের আইনজীবী ব্যারিস্টার মুস্তাফিজুর রহমান খান কাল রাতে মুঠোফোনে এই প্রতিবেদককে বলেছেন, ‘তাসকিনের বোলিং অ্যাকশনের পরীক্ষায় প্রক্রিয়াগত কিছু ত্রুটি আছে। আইসিসির নিয়ম অনুযায়ী এ ধরনের পরীক্ষায় প্রক্রিয়াগত ত্রুটি থাকলে সংশ্লিষ্ট বোর্ড রিভিউ চাইতে পারে। আমি মনে করি বিসিবিরও রিভিউ চাওয়া উচিত।’";

		// Output the result
		PosTagger posTagger = new PosTagger(sample);
		// System.out.println(posTagger.getPosTaggedWords());
		System.out.println(posTagger.findVerbTaggedTokens());
	}
}
