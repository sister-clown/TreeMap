package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import static jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.Rotate.*;


public class BlackNode<K, V> extends Node<K, V> {


    public BlackNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        super(key, value, left, right);
    }

    @Override
    public Node clone() {
        return new BlackNode<K, V>(key, value, left, right);
    }

    @Override
    public Node<K,V> createNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        return new BlackNode<K, V>(key, value, left, right);
    }


    @Override
    Node balance() {
        Rotate spin = left.firstCheckColor(L);

        if (spin == R) {
            Node<K, V> leftChild = new BlackNode<K,V>(left.left().getKey(), left.left().getValue(), left.left().left(), left.left().right());
            Node<K,V> rightChild = new BlackNode<K,V>(getKey(), getValue(), left.right(), right);
            return new RedNode<K,V>(left.getKey(), left.getValue(), leftChild, rightChild);

        } else if (spin == LR) {
            Node<K,V> leftChild = new BlackNode<K,V>(left.getKey(), left.getValue(), left.left(), left.right().left());
            Node<K,V> rightChild = new BlackNode<K,V>(getKey(), getValue(), left.right().right(), right);
            return new RedNode<K,V>(left.right().getKey(), left.right().getValue(), leftChild, rightChild);

        }

        spin = right.firstCheckColor(R);
        if (spin == L) {
            Node<K, V> leftChild = new BlackNode<K,V>(getKey(), getValue(), left, right.left());
            Node<K, V> rightChild = new BlackNode<K,V>(right.right().getKey(), (V) right.right().getValue(), right.right().left(), right.right().right());
            return new RedNode<K,V>(right.getKey(), right.getValue(), leftChild, rightChild);

        } else if (spin == RL) {
            Node<K, V> leftChild = new BlackNode<K,V>(getKey(), getValue(), left, right.left());
            Node<K, V> rightChild = new BlackNode<K,V>(right.right().getKey(), right.right().getValue(), right.right().left(), right.right().right());
            return new RedNode<K,V>(right.getKey(), right.getValue(), leftChild, rightChild);

        }

        return this;
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
