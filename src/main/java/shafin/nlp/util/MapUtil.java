package shafin.nlp.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtil {

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDecending(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o2, Map.Entry<K, V> o1) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static <K, Double> Map<K, Double> normalizeMapValue(Map<K, Double> map) {
		
		Double maxVal = 0;
		for (Map.Entry<K, Double> entry : map.entrySet()) {
			if (entry.getValue().c > maxVal) {
				maxVal = entry.getValue();
			}
		}
		
		
		Map<K, Double> normalizedMap = new HashMap<>();
		for(Map.Entry<K, Double> entry: map.entrySet()){
			
			double temp = (double)((double)entry.getValue()/maxValue);
			Double normV = new Double() ;
			
			normalizedMap.put(entry.getKey(), normV); 
		}

	}

	public static <K, V> Entry<K, V> findFirstKeyAssociatedMaxValue(Map<K, V> map) {
		Map.Entry<K, V> maxEntry = null;
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (maxEntry == null || ((Double) entry.getValue()).compareTo((Double) maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}
		return maxEntry;
	}
	
	public static <K, V> Entry<K, V> findLastKeyAssociatedMaxValue(Map<K, V> map) {
		Map.Entry<K, V> maxEntry = null;
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (maxEntry == null || ((Double) entry.getValue()).compareTo((Double) maxEntry.getValue()) >= 0) {
				maxEntry = entry;
			}
		}
		return maxEntry;
	}

	public static Integer getSumOftheValues(Map<String, Integer> map) {
		int sum = 0;
		for (String key : map.keySet()) {
			sum += map.get(key).intValue();
		}
		return sum;
	}
}
