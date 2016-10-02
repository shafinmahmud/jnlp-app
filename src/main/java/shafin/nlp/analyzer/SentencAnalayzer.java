package shafin.nlp.analyzer;

import java.util.List;

import shafin.nlp.tokenizer.SentenceSpliter;

public class SentencAnalayzer {
	
	private final String TEXT;
	
	public SentencAnalayzer(String text) {
		this.TEXT = text;
	}
	
	public List<String> getSentenceTokens() {
		return SentenceSpliter.getSentenceTokenListBn(TEXT);
	}
	
	public static void main(String[] args) {
		String text = "আফগানিস্তান ইনিংসের তৃতীয় ওভারের প্রথম বলটিতেই গগনবিদারী উল্লাসের ছাপ শেষ হয়েছে কি হয়নি, "
				+ "হঠাৎই চুপ পুরো স্টেডিয়াম। সম্ভবত স্তম্ভিত হয়ে পড়েছিল বাংলাদেশও। পায়ে ব্যথা পেয়ে যে মাটিতে পড়ে আছেন মাশরাফি বিন মুর্তজা।  "
				+ "আফগানিস্তান আগের বলেই দুর্দান্ত সুইং ডেলিভারিতে মোহাম্মদ শেহজাদকে বোল্ড করেছেন বাংলাদেশ অধিনায়ক। পরের বলটি করতে গিয়েই পা ফেলতে হলো গড়বড়, "
				+ "মাশরাফি বসে পড়লেন মাটিতে। খেলোয়াড়েরা তাঁকে ঘিরে ছিলেন, ফিজিও এলেন। আর পুরো স্টেডিয়ামে তখন শঙ্কার রেণু উড়ছে, এত এত অস্ত্রোপচার করা পায়েই যে ব্যথা পেলেন মাশরাফি।";
		
		SentencAnalayzer analyzer = new SentencAnalayzer(text);
		for(String string : analyzer.getSentenceTokens()){
			System.out.println(string);
		}
	}
	
}
