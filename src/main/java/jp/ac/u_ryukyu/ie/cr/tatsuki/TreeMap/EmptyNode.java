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

    public EmptyNode(K key) { //keyは削除時の回転処理に使用する
        super(key, null);
    }

    @Override // 回転処理時にEmptyNodeの子を見ることがあるのでleft rightでEmptyNodeを返すようにする
    public Node<K, V> left() {
        return new EmptyNode<>();
    }

    @Override
    public Node<K, V> right() {
        return new EmptyNode<>();
    }


    @Override
    protected boolean isNotEmpty() {
        return false;
    }


    @Override
    public Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        return new RedNode<K, V>(key, value, new EmptyNode<K, V>(), new EmptyNode<K, V>());
    }


    public Node<K, V> put(K k, V value) {
        return new RedNode(k, value, new EmptyNode<K, V>(), new EmptyNode<K, V>());
    }

    @Override
    public Node replaceNode(Node<K, V> parent) { // not use method
        return this;
    }

    @Override
    protected Node deleteNode() { //not use method
        return this;
    }

    @Override
    Node insBalance() {
        return this;
    }

    @Override
    Rotate checkRotate(Rotate side) {
        return N;
    }

    @Override
    boolean isRed() {
        return false;
    }

    @Override
    protected int checkBlackCount(int count) { // test method
        System.out.println("blackCount = " + count);
        return count;
    }

}
