package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;


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
        return new RedNode<>(key, value, new EmptyNode<>(), new EmptyNode<>());
    }


    @Override
    public rebuildNode<K, V> replaceNode(Node<K, V> parent, Comparator ctr) { // not use method
        return new rebuildNode<>(false, this);
    }

    @Override
    protected rebuildNode<K, V> deleteNode() { //not use method
        return new rebuildNode<>(false, this);
    }

    @Override
    Node<K, V> insBalance() {
        return this;
    }

    @Override
    Rotate checkRotate(Rotate side) {
        return Rotate.N;
    }

    @Override
    boolean isRed() {
        return false;
    }

    @Override
    @Test
    protected int checkDepth(int count, int minCount) { // test method
        if (count < minCount | minCount == 0)
            minCount = count;
        System.out.println("depth = " + count);

        Assert.assertTrue(count <= 2 * minCount);
        return minCount;
    }

}
