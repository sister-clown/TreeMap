package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import java.util.Comparator;

public class DefaultComparator<K> implements Comparator<K> {
    @Override
    @SuppressWarnings("unchecked")
    public int compare(K key, K compareKey) {
        return ((Comparable<? super K>)key).compareTo(compareKey);
    }
}
