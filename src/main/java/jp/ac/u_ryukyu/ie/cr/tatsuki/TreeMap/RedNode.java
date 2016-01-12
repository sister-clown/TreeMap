package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import org.junit.Test;

import java.util.Comparator;


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
        return new RedNode<>(key, value, left, right);
    }

    @Override
    protected Node<K, V> insBalance() {
        return this;
    }

    @Override
    protected rebuildNode deleteNode() {
        Node<K,V> emptyNode = new EmptyNode<>(this.getKey());
        return new rebuildNode<>(false, emptyNode);
    }

    @Override
    Rotate checkRotate(Rotate side) {
        if (side == Rotate.L) {
            if (left.isRed())
                return Rotate.R;
            else if (right.isRed())
                return Rotate.LR;
            return Rotate.N;
        } else {
            if (left.isRed())
                return Rotate.RL;
            else if (right.isRed())
                return Rotate.L;
            return Rotate.N;
        }
    }

    @Override
    boolean isRed() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public rebuildNode replaceNode(Node<K, V> parent, Comparator ctr) {
        Node<K, V> newNode;
        if (!this.left().isNotEmpty() && !this.right().isNotEmpty()) { //自身を削除する
            return deleteNode();
        } else if (this.left().isNotEmpty() && !this.right().isNotEmpty()) { //左の部分木を昇格させる
            newNode = left().createNode(left().getKey(), left().getValue(), left().left(), left().right());
            return new rebuildNode(false, newNode);
        } else if (!this.left().isNotEmpty() && this.right().isNotEmpty()) { //右の部分木を昇格させる
            newNode = right().createNode(right().getKey(), right().getValue(), right().left(), right().right());
            return new rebuildNode(false, newNode);
        } else {//子ノードが左右にある場合
            //左の部分木の最大の値を持つNodeと自身を置き換える
            Node<K, V> cur = this.left();

            while (cur.right().isNotEmpty()) {
                cur = cur.right();
            }
            if (this.left().right().isNotEmpty()) {
                rebuildNode<K, V> leftSubTreeNodeRebuildNode = this.left().deleteSubTreeMaxNode(null, ctr, Rotate.L);
                if (leftSubTreeNodeRebuildNode.rebuild()) {
                    Node<K, V> node = leftSubTreeNodeRebuildNode.getNode();
                    Node<K, V> newParent = createNode(cur.getKey(), cur.getValue(), node, this.right());
                    return node.deleteBalance(newParent, ctr);
                }
                Node<K, V> leftSubTreeNode = leftSubTreeNodeRebuildNode.getNode();
                newNode = createNode(cur.getKey(), cur.getValue(), leftSubTreeNode, this.right());
                return new rebuildNode<>(false, newNode);
            } else {
                rebuildNode<K, V> leftSubTreeNodeRebuildNode = this.left().replaceNode(this, ctr);
                if (leftSubTreeNodeRebuildNode.rebuild()) {
                    Node<K, V> node = leftSubTreeNodeRebuildNode.getNode();
                    Node<K, V> newParent = createNode(this.left().getKey(), this.left().getValue(), node, this.right());
                    return node.deleteBalance(newParent, ctr);
                }
                Node<K, V> leftSubTreeNode = leftSubTreeNodeRebuildNode.getNode();
                newNode = createNode(cur.getKey(), cur.getValue(), leftSubTreeNode, this.right());
                return new rebuildNode<>(false, newNode);
            }

        }
    }


    @Override
    @Test
    protected int checkDepth(int count, int minCount) { // test method
        count++;
        minCount = left().checkDepth(count, minCount);
        minCount = right().checkDepth(count, minCount);
        return minCount;
    }
}
