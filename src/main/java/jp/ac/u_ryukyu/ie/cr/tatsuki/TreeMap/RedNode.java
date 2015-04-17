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
    protected boolean isNotEmpty() {
        return true;
    }

    @Override
    public Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        return new RedNode<K, V>(key, value, left, right);
    }

    @Override
    protected Node<K, V> insBalance() {
        return this;
    }

    @Override
    protected Node deleteNode() {
        return new EmptyNode(this.getKey());
    }

    @Override
    Rotate checkRotate(Rotate side) {

        if (side == L) {
            if (left.isRed())
                return R;

            else if (right.isRed())
                return LR;

            return N;
        } else {

            if (left.isRed())
                return RL;

            else if (right.isRed())
                return L;

            return N;
        }

    }

    @Override
    boolean isRed() {
        return true;
    }

    @Override
    public Node replaceNode(Node<K, V> parent) {

        Node<K, V> newNode = null;
        if (!this.left().isNotEmpty() && !this.right().isNotEmpty()) { //自身を削除する
            return deleteNode();

        } else if (this.left().isNotEmpty() && !this.right().isNotEmpty()) { //左の部分木を昇格させる
            newNode = left().createNode(left().getKey(), left().getValue(), left().left(), left().right());
            return newNode;

        } else if (!this.left().isNotEmpty() && this.right().isNotEmpty()) { //右の部分木を昇格させる
            newNode = right().createNode(right().getKey(), right().getValue(), right().left(), right().right());
            return newNode;

        } else {//子ノードが左右にある場合
            //左の部分木の最大の値を持つNodeと自身を置き換える
            Node<K, V> cur = this.left();

            while (cur.right().isNotEmpty()) {
                cur = cur.right();
            }

            Node<K, V> leftSubTreeNode = new EmptyNode<>();

            if (this.left().right().isNotEmpty()) {
                leftSubTreeNode = this.left().deleteSubTreeMaxNode(null);
                newNode = createNode(cur.getKey(), cur.getValue(), leftSubTreeNode, this.right());
                return leftSubTreeNode.deleteBalance(newNode);

            } else {
                leftSubTreeNode = this.left().replaceNode(this);
                newNode = createNode(cur.getKey(), cur.getValue(), leftSubTreeNode, this.right());
                return leftSubTreeNode.deleteBalance(newNode);
            }

        }
    }


    @Override
    protected int checkBlackCount(int count) { // test method
        left().checkBlackCount(count);
        right().checkBlackCount(count);
        return count;
    }
}
