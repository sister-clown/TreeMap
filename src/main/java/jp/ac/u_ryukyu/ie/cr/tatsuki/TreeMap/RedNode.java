package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import org.junit.Test;

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
    protected Node deleteNode() throws RotateParent{
        EmptyNode emptyNode = new EmptyNode(this.getKey());
        return emptyNode;
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
    public Node replaceNode(Node<K, V> parent) throws RotateParent {
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
            Node leftSubTreeNode = null;
            if (this.left().right().isNotEmpty()) {
                try {
                    leftSubTreeNode = this.left().deleteSubTreeMaxNode(null,Rotate.L);
                    newNode = createNode(cur.getKey(), cur.getValue(), leftSubTreeNode, this.right());
                    return newNode;
                } catch (RotateParent e) {
                    leftSubTreeNode = e.getParent();
                    Node<K, V> newParent = createNode(cur.getKey(), cur.getValue(), leftSubTreeNode, this.right());
                    return leftSubTreeNode.deleteBalance(newParent);
                }
            } else {
                try {
                    leftSubTreeNode = this.left().replaceNode(this);
                    newNode = createNode(cur.getKey(), cur.getValue(), leftSubTreeNode, this.right());
                    return newNode;
                } catch (RotateParent e) {
                    Node node = e.getParent();
                    Node newParent = createNode(this.left().getKey(), this.left().getValue(), leftSubTreeNode, this.right());
                    return node.deleteBalance(newParent);
                }
            }

        }
    }


    @Override
    @Test
    protected int checkDepth(int count,int minCount) { // test method
        count ++;
        minCount = left().checkDepth(count, minCount);
        minCount = right().checkDepth(count, minCount);
        count --;
        return minCount;
    }
}
