package shafin.nlp.pfo;

import java.util.List;

import shafin.nlp.tokenizer.SentenceTokenizer;

public class PhraseFirstOccurrence {

	public static double getPhraseFirstOccurrence(List<String> sentenceArray, String phrase) {
		int SP = 0;
		for (int i = 0; i < sentenceArray.size(); i++) {
			if (sentenceArray.get(i).contains(phrase)) {
				SP = i + 1;
				return (double) (1 / Math.sqrt(SP));
			}
		}
		return SP;
	}

	public static double getPhraseFirstOccurrence(String text, String phrase) {

		int SP = 0;
		/* sentence analysis */
		List<String> sentenceArray = SentenceTokenizer.getSentenceTokenListBn(text);
		for (int i = 0; i < sentenceArray.size(); i++) {
			if (sentenceArray.get(i).contains(phrase)) {
				SP = i + 1;
				return (double) (1 / Math.sqrt(SP));
			}
		}
		return SP;
	}
	
	public static void main(String[] args) {
		String text = "ম্যাচের আগেই অস্ট্রেলিয়ান অধিনায়ক স্মিথ বলেছিলেন, ‘ওদের ব্যাটিং তো ভয়ংকর, বিশেষ করে বিরাট কোহলি।’ ওদের মানে ভারত।"
				+"সেই ভারতের ব্যাটিং নিয়ে কথা বলতে গিয়ে স্মিথ কেন আলাদা করে কোহলির নামটা বলেছিলেন, ম্যাচ শেষে মনে হয় তা বুঝে গেছেন সবাই। মোহালির আইএস বিন্দ্রা স্টেডিয়ামে কাল ভারত তো আসলে"
				+"শুধু নামেই অস্ট্রেলিয়ার প্রতিপক্ষ ছিল। খেললেন তো কোহলিই। ৫১ বলে অপরাজিত ৮২ রানের অসাধারণ এক ইনিংস। অধিনায়ক স্টিভেন স্কোরকার্ড বলছে, ১৬০ রান করেও অস্ট্রেলিয়া ৬ উইকেটে হেরেছে ভারতের কাছে।"
				+"কিন্তু আসলে তো হারটা কোহলির কাছেই!কোহলির ব্যাটে ভর করে পাওয়া এই দুর্দান্ত জয়ে টি-টোয়েন্টি বিশ্বকাপের সেমিফাইনালেও পৌঁছে গেছে ভারত। শেষ চারে ৩১ মার্চ মুম্বাইয়ে ধোনির দলের প্রতিপক্ষ ওয়েস্ট ইন্ডিজ।";
		String phrase = "অধিনায়ক স্টিভেন";
		System.out.println(getPhraseFirstOccurrence(text, phrase));
	}
}
