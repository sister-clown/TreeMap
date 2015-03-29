package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import java.util.Optional;

import static jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.Rotate.*;

/**
 * Created by e115731 on 15/03/25.
 */
public class EmptyNode<K, V> extends Node<K, V> {

    public EmptyNode() {
        super(null, null);
    }

    @Override
    protected boolean exitNode() {
        return false;
    }

    @Override
    public Node<K, V> clone() {
        return new EmptyNode<K, V>();
    }

    @Override
    public Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        return new RedNode<K, V>(key, value, new EmptyNode<K, V>(), new EmptyNode<K, V>());
    }

    @Override
    public Node<K, V> put(Comparable<? super K> k, V value) {
        return new RedNode(k, value, new EmptyNode<K, V>(), new EmptyNode<K, V>());
    }

    @Override
    Node balance() {
        return clone();
    }

    @Override
    public Optional<V> get(Comparable<? super K> key) {
        return Optional.ofNullable((V) getValue());
    }

    @Override
    Rotate firstCheckColor(Rotate side) {
        return N;
    }

    @Override
    boolean secondCheckColor() {
        return false;
    }
}
