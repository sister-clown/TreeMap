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
    protected boolean exitNode() {
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
    public Node deleteBalance(Node<K, V> parent) {

        if (0 > (parent.getKey().hashCode() - this.getKey().hashCode()))
            return parent.createNode(parent.getKey(), parent.getValue(), parent.left(), this);
        else
            return parent.createNode(parent.getKey(), parent.getValue(), this, parent.right());
    }

    @Override
    protected Node deleteNode() {
        return new EmptyNode(this.getKey());
    }

    @Override
    Rotate checkRotate(Rotate side) {

        if (side == L) {
            if (left.checkColor())
                return R;

            else if (right.checkColor())
                return LR;

            return N;
        } else {

            if (left.checkColor())
                return RL;

            else if (right.checkColor())
                return L;

            return N;
        }

    }

    @Override
    boolean checkColor() {
        return true;
    }

    @Override
    DeleteRebuildFlag RebuildDelete(Rotate side) {

        DeleteRebuildFlag flag;
        if (side == Rotate.R) {
            flag = this.left().childRebuildDelete(side);
        } else {
            flag = this.right().childRebuildDelete(side);
        }

        if (flag == DeleteRebuildFlag.allBlack)
            return DeleteRebuildFlag.four;

        return flag;
    }

    @Override
    DeleteRebuildFlag childRebuildDelete(Rotate side) {
        return DeleteRebuildFlag.two;
    }

    @Override
    public Node replaceNode(Node<K, V> parent) {

        Node<K, V> newNode = null;
        if (!this.left().exitNode() && !this.right().exitNode()) { //自身を削除する
            return deleteNode();

        } else if (this.left().exitNode() && !this.right().exitNode()) { //左の部分木を昇格させる
            newNode = left().createNode(left().getKey(), left().getValue(), left().left(), left().right());
            return newNode;

        } else if (!this.left().exitNode() && this.right().exitNode()) { //右の部分木を昇格させる
            newNode = right().createNode(right().getKey(), right().getValue(), right().left(), right().right());
            return newNode;

        } else {//子ノードが左右にある場合
            //左の部分木の最大の値を持つNodeと自身を置き換える
            Node<K, V> cur = this.left();

            while (cur.right().exitNode()) {
                cur = cur.right();
            }

            Node<K, V> leftSubTreeNode = new EmptyNode<>();

            if (this.left().right().exitNode()) {
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
