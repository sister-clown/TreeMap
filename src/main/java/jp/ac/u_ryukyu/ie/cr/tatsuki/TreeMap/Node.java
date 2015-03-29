package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import java.util.*;

/**
 * Created by e115731 on 15/03/23.
 */
public abstract class Node<K, V> {

    protected K key;
    protected V value;
    protected Node<K, V> right;
    protected Node<K, V> left;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Node(K key, V value, Node<K, V> left, Node<K, V> right) {
        this.key = key;
        this.value = value;
        this.right = right;
        this.left = left;
    }

    public Node<K, V> left() {
        return left;
    }


    public Optional<V> get(Comparable<? super K> key) {

        int result = key.compareTo(getKey());

        if (result > 0)
            return right().get(key);

        else if (result < 0)
            return left().get(key);

        else if (result == 0)
            return Optional.ofNullable(getValue());

        return Optional.ofNullable(null);
    }

    public Optional<V> getLoop(Comparable<? super K> key) {

        Node<K, V> cur = this;

        while (cur.exitNode()) {
            int result = key.compareTo(cur.getKey());

            if (result > 0)
                cur = cur.right();

            else if (result < 0)
                cur = cur.left();

            else if (result == 0)
                return Optional.ofNullable(cur.getValue());
        }

        return Optional.ofNullable(null);
    }


    public Node<K, V> right() {
        return right;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }


    public Node<K, V> put(Comparable<? super K> k, V value) {

        int result = k.compareTo(this.key);

        if (result > 0) {
            Node<K, V> node = right.put(k, value);
            node = createNode(key, this.value, left, node);
            return node.balance();
        } else if (result < 0) {
            Node node = left.put(k, value);
            return createNode(key, this.value, node, right).balance();
        }

        return createNode(key, value, left, right); // equals

    }

    public abstract Node clone();


    protected abstract boolean exitNode();

    public abstract Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right);

    abstract Node<K, V> balance();

    abstract Rotate firstCheckColor(Rotate side);

    abstract boolean secondCheckColor();
}
