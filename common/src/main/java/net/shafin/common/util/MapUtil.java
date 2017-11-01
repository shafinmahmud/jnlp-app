package net.shafin.common.util;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class MapUtil {

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map<K, V> map) {
        Set<Entry<K, V>> sortedSet = map.entrySet().stream()
                .sorted(Entry.comparingByValue())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return getMapFromEntrySet(sortedSet);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        Set<Entry<K, V>> sortedSet = map.entrySet().stream()
                .sorted(Entry.<K, V>comparingByValue().reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return getMapFromEntrySet(sortedSet);
    }

    public static <K> Map<K, Double> normalizeMapValue(Map<K, Double> map) {
        Double maxVal = new Double(0);
        for (Entry<K, Double> entry : map.entrySet()) {
            if (entry.getValue() > maxVal) {
                maxVal = entry.getValue();
            }
        }

        Map<K, Double> normalizedMap = new HashMap<>();
        for (Entry<K, Double> entry : map.entrySet()) {
            Double normVal = new Double((double) (entry.getValue() / maxVal));
            normalizedMap.put(entry.getKey(), normVal);
        }
        return normalizedMap;
    }

    public static <K, V> Entry<K, V> findFirstKeyAssociatedMaxValue(Map<K, V> map) {
        Entry<K, V> maxEntry = null;
        for (Entry<K, V> entry : map.entrySet()) {
            if (maxEntry == null || ((Double) entry.getValue()).compareTo((Double) maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

    public static <K, V> Entry<K, V> findLastKeyAssociatedMaxValue(Map<K, V> map) {
        Entry<K, V> maxEntry = null;
        for (Entry<K, V> entry : map.entrySet()) {
            if (maxEntry == null || ((Double) entry.getValue()).compareTo((Double) maxEntry.getValue()) >= 0) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

    public static <K> Double getSumOftheValues(Map<K, Double> map) {
        return map.values().stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    private static <K, V> Map<K, V> getMapFromEntrySet(Set<Entry<K, V>> entrySet) {
        return entrySet.stream()
                .collect(Collectors.toMap(Entry::getKey,
                        Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    public static void main(String[] args) {
        Map<String, Integer> sampleMap = new LinkedHashMap<>();
        sampleMap.put("eeni", 24);
        sampleMap.put("meeni", 76);
        sampleMap.put("miini", 100);
        sampleMap.put("miiniCat", 100);
        sampleMap.put("miiniCow", 100);
        sampleMap.put("moh", 9);

        System.out.println(sampleMap);
        System.out.println("sorted by value Asc: " + sortByValueAscending(sampleMap));
        System.out.println("sorted by value Dsc: " + sortByValueDescending(sampleMap));
        System.out.println("Sum of Map values: " + getSumOftheValues(sampleMap));

        System.out.println("first key assosicated max: " + findFirstKeyAssociatedMaxValue(sampleMap));
        System.out.println("last key assosicated max: " + findLastKeyAssociatedMaxValue(sampleMap));
        //System.out.println("mormalized map: " + normalizeMapValue(sampleMap));
    }
}
