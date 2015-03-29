package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import static jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.Rotate.*;

/**
 * Created by e115731 on 15/03/25.
 */
public class RedNode<K, V> extends Node<K, V> {


    public RedNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        super(key, value, left, right);
    }


    @Override
    public Node<K,V> clone() {
        return new RedNode<K,V>(key, value, left, right);
    }

    @Override
    protected boolean exitNode() {
        return true;
    }

    @Override
    public Node<K,V> createNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        return new RedNode<K,V>(key, value, left, right);
    }

    @Override
    protected Node<K,V> balance() {
        return this;
    }

    @Override
    Rotate firstCheckColor(Rotate side) {

        if (side == L) {
            if (left.secondCheckColor())
                return R;

            else if (right.secondCheckColor())
                return LR;

            return N;
        } else {

            if (left.secondCheckColor())
                return RL;

            else if (right.secondCheckColor())
                return L;

            return N;
        }

    }

    @Override
    boolean secondCheckColor() {
        return true;
    }


}
