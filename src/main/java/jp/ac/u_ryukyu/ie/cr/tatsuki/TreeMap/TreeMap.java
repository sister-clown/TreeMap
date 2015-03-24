package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;


/**
 * Created by e115731 on 15/03/23.
 */
public class TreeMap<K, V> {
    Entry<K, V> root;
    int size;

    public static void main(String args[]) {
        java.util.TreeMap<Integer, Integer> map = new java.util.TreeMap<Integer, Integer>();
        for (int count = 0; count < 100; count++) {
            map.put(count, count);
        }
    }

    public TreeMap() {
        this.root = null;
        this.size = 0;
    }

    public TreeMap(Entry<K, V> root, int size) {
        this.root = root;
        this.size = size;
    }

    public V get(K key) {
        Entry cur = root;
        Comparable<? super K> k = (Comparable<? super K>) key;
        do {
            int result = k.compareTo((K) cur.getKey());
            if (result > 0)
                cur = cur.right();

            else if (result < 0)
                cur = cur.left();

            else if (result == 0)
                return (V)cur.getValue();

        }while(cur != null);
        return null;
    }

    public TreeMap put(K key, V value) {

        if (key == null || value == null)  // null check
            throw new NullPointerException();

        if (isEmpty()) {
            Entry<K, V> newRoot = new Entry<K, V>(Color.BLACK, key, value);
            return new TreeMap(newRoot, size++);
        }

        Comparable<? super K> k = (Comparable<? super K>) key;

        Entry newEntry = insert(k, value, root);
        Entry root = new Entry(Color.BLACK, newEntry.getKey(), newEntry.getValue(),newEntry.left(),newEntry.right());
        return new TreeMap(root, 0);
    }

    private Entry insert(Comparable<? super K> key, V value, Entry cur) {
        int result = key.compareTo((K) cur.getKey());

        if (result > 0) {
            if (cur.right() == null) {
                Entry right = new Entry(Color.RED, key, value);
                return new Entry(cur.getColor(), cur.getKey(), cur.getValue(), cur.left(), right);

            }
            return balance(cur.left(), cur, insert(key, value, cur.right()));
        } else if (result < 0) {
            if (cur.left() == null) {
                Entry left = new Entry(Color.RED, key, value);
                return new Entry(cur.getColor(), cur.getKey(), cur.getValue(), left, cur.right());

            }
            return balance(insert(key, value, cur.left()), cur, cur.right());

        } else // equal
            return new Entry(cur.getColor(), key, value, cur.left(), cur.right());

    }

    private Entry balance(Entry left, Entry cur, Entry right) {
        if (cur.getColor() == Color.BLACK && colorRedcheck(left) && colorRedcheck(left.left())) { // rotate right
            Entry<K, V> leftChild = new Entry<K, V>(Color.BLACK, (K) left.left().getKey(), (V) left.left().getValue(), left.left().left(), left.left().right());
            Entry<K, V> rightChild = new Entry<K, V>(Color.BLACK, (K) cur.getKey(), (V) cur.getValue(), left.right(), right);
            return new Entry<K, V>(Color.RED, (K) left.getKey(), (V) left.getValue(), leftChild, rightChild);

        } else if (cur.getColor() == Color.BLACK && colorRedcheck(left) && colorRedcheck(left.right())) { // rotate left right
            Entry<K, V> leftChild = new Entry<K, V>(Color.BLACK, (K) left.getKey(), (V) left.getValue(), left.left(), left.right().left());
            Entry<K, V> rightChild = new Entry<K, V>(Color.BLACK, (K) cur.getKey(), (V) cur.getValue(), left.right().right(), right);
            return new Entry<K, V>(Color.RED, (K) left.right().getKey(), (V) left.right().getValue(), leftChild, rightChild);

        } else if (cur.getColor() == Color.BLACK && colorRedcheck(right) && colorRedcheck(right.left())) { //rotate right left
            Entry<K, V> leftChild = new Entry<K, V>(Color.BLACK, (K) cur.getKey(), (V) cur.getValue(), left, right.left().left());
            Entry<K, V> rightChild = new Entry<K, V>(Color.BLACK, (K) right.getKey(), (V) right.getValue(), right.left().right(), right.right());
            return new Entry<K, V>(Color.RED, (K) right.left().getKey(), (V) right.left().getValue(), leftChild, rightChild);

        } else if (cur.getColor() == Color.BLACK && colorRedcheck(right) && colorRedcheck(right.right())) { //rotate left
            Entry<K, V> leftChild = new Entry<K, V>(Color.BLACK, (K) cur.getKey(), (V) cur.getValue(), left, right.left());
            Entry<K, V> rightChild = new Entry<K, V>(Color.BLACK, (K) right.right().getKey(), (V) right.right().getValue(), right.right().left(), right.right().right());
            return new Entry<K, V>(Color.RED, (K) right.getKey(), (V) right.getValue(), leftChild, rightChild);

        }
        return new Entry(cur.getColor(),cur.getKey(),cur.getValue(),left,right);
    }

    private boolean colorRedcheck(Entry e) {
        return e != null && e.getColor() == Color.RED;
    }

    public boolean isEmpty() {
        return root == null;
    }

}
