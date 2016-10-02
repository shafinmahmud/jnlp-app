package shafin.nlp.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import shafin.nlp.tokenizer.WordTokenizer;
import shafin.nlp.util.MapUtil;

public class WrdAnalyzer {

	private final String TEXT;

	public WrdAnalyzer(String text) {
		this.TEXT = text;
	}

	public List<String> getWordTokens() {
		return WordTokenizer.getTokenizedBnList(TEXT);
	}

	public List<String> getUniqueWordTokens() {
		List<String> tokens = getWordTokens();
		Set<String> uniqueTokens = new HashSet<>();

		for (String token : tokens) {
			uniqueTokens.add(token);
		}

		List<String> list = new ArrayList<>();
		list.addAll(tokens);
		return list;
	}

	public Map<String, Integer> getUniqueWordsWithFrequency() {
		Map<String, Integer> wordOccurence = new HashMap<>();
		List<String> tokens = getWordTokens();
		for (String token : tokens) {
			int count = wordOccurence.containsKey(token) ? wordOccurence.get(token) : 0;
			wordOccurence.put(token, count + 1);
		}
		return MapUtil.sortByValueDecending(wordOccurence);
	}

	public static void main(String[] args) {
		String text = "আফগানিস্তান ইনিংসের তৃতীয় ওভারের প্রথম বলটিতেই গগনবিদারী উল্লাসের ছাপ শেষ হয়েছে কি হয়নি, "
				+ "হঠাৎই চুপ পুরো স্টেডিয়াম। সম্ভবত স্তম্ভিত হয়ে পড়েছিল বাংলাদেশও। পায়ে ব্যথা পেয়ে যে মাটিতে পড়ে আছেন মাশরাফি বিন মুর্তজা।  "
				+ "আফগানিস্তান আগের বলেই দুর্দান্ত সুইং ডেলিভারিতে মোহাম্মদ শেহজাদকে বোল্ড করেছেন বাংলাদেশ অধিনায়ক। পরের বলটি করতে গিয়েই পা ফেলতে হলো গড়বড়, "
				+ "মাশরাফি বসে পড়লেন মাটিতে। খেলোয়াড়েরা তাঁকে ঘিরে ছিলেন, ফিজিও এলেন। আর পুরো স্টেডিয়ামে তখন শঙ্কার রেণু উড়ছে, এত এত অস্ত্রোপচার করা পায়েই যে ব্যথা পেলেন মাশরাফি।";
		WrdAnalyzer wordAnalyzer = new WrdAnalyzer(text);
		List<String> unique = wordAnalyzer.getUniqueWordTokens();
		
		System.out.println(unique);
		/*for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " <> " + entry.getValue());
		}*/
	}

}
