package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

/**
 * Created by e115731 on 15/03/23.
 */
public class Entry<K, V> {
    private K key;
    private V value;
    private Entry<K, V> right;
    private Entry<K, V> left;
    private boolean color = Color.RED;

    public Entry(boolean color,K key, V value) {
        this.key = key;
        this.value = value;
        this.right = null;
        this.left = null;
    }

    public Entry(Entry e) {
        this.key = (K) e.getKey();
        this.value = (V) e.getValue();
        this.right = e.right();
        this.left = e.left;
    }

    public Entry(boolean color,K key, V value, Entry<K, V> left, Entry<K, V> right) {
        this.key = key;
        this.value = value;
        this.right = right;
        this.left = left;
        this.color = color;
    }

    public Entry left() {
        return left;
    }

    public Entry right() {
        return right;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public boolean getColor() {
        return color;
    }

}
