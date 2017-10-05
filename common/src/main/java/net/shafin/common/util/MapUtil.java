package net.shafin.common.util;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
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


    public static <K> Map<K, Double> normalizeMapValue(Map<K, Double> map) {
        Double maxVal = new Double(0);
        for (Map.Entry<K, Double> entry : map.entrySet()) {
            if (entry.getValue() > maxVal) {
                maxVal = entry.getValue();
            }
        }

        Map<K, Double> normalizedMap = new HashMap<>();
        for (Map.Entry<K, Double> entry : map.entrySet()) {
            Double normVal = new Double((double) (entry.getValue() / maxVal));
            normalizedMap.put(entry.getKey(), normVal);
        }
        return normalizedMap;
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

    public static void main(String[] args) {
        Map<String, Double> sampleMap = new LinkedHashMap<>();
        sampleMap.put("eeni", 24.215);
        sampleMap.put("meeni", 76.231);
        sampleMap.put("miini", 100.00);
        sampleMap.put("miiniCat", 100.00);
        sampleMap.put("miiniCow", 100.00);
        sampleMap.put("moh", 9.653);

        System.out.println(sampleMap);
        System.out.println("first key assosicated max: " + findFirstKeyAssociatedMaxValue(sampleMap));
        System.out.println("last key assosicated max: " + findLastKeyAssociatedMaxValue(sampleMap));
        System.out.println("mormalized map: " + normalizeMapValue(sampleMap));

    }
}
