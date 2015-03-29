package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;


import java.util.Iterator;
import java.util.Optional;

import static jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.Rotate.*;

/**
 * Created by e115731 on 15/03/23.
 */
public class TreeMap<K, V> {
    Node<K, V> root;
    int size;

    public TreeMap() {
        this.root = null;
        this.size = 0;
    }


    public Node getRoot() {
        return root;
    }

    public TreeMap(Node<K, V> root, int size) {
        this.root = root;
        this.size = size;
    }

    public Optional<V> get(K key) {
        return root.get((Comparable<? super K>) key);
    }

    public Optional<V> getLoop(K key) {
        return root.getLoop((Comparable<? super K>) key);
    }

    public TreeMap put(K key, V value) {

        if (key == null || value == null)  // null check
            throw new NullPointerException();

        if (isEmpty()) {
            Node<K, V> newRoot = new BlackNode(key, value, new EmptyNode(), new EmptyNode());
            return new TreeMap<K, V>(newRoot, size++);
        }

        Node<K, V> newEntry = root.put((Comparable<? super K>) key, value);
        Node<K, V> newRoot = new BlackNode(newEntry.getKey(), newEntry.getValue(), newEntry.left(), newEntry.right());
        return new TreeMap(newRoot, 0);
    }


    public boolean isEmpty() {
        return root == null;
    }

    public Iterator<K> keys() {

        return new Iterator<K>() {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public K next() {
                return null;
            }
        };
    }
}
