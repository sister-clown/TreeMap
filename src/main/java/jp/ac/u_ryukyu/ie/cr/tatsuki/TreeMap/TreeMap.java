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
        this.root = new EmptyNode();
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
        return root.get(key);
    }

    public TreeMap put(K key, V value) {

        if (key == null || value == null)  // null check
            throw new NullPointerException();

        if (isEmpty()) {
            Node<K, V> newRoot = new BlackNode(key, value, new EmptyNode(), new EmptyNode());
            return new TreeMap<K, V>(newRoot, size++);
        }

        Node<K, V> newEntry = root.put(key, value);
        Node<K, V> newRoot = new BlackNode(newEntry.getKey(), newEntry.getValue(), newEntry.left(), newEntry.right());
        return new TreeMap(newRoot, 0);
    }


    public boolean isEmpty() {
        return root == null;
    }

    public TreeMap<K,V> delete(K key) {
       Node node = root.delete(key,null);
        Node newRoot = new BlackNode(node.getKey(),node.getValue(),node.left(),node.right());
        return new TreeMap(newRoot,0);
    }

}
