package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import org.junit.Test;

import java.util.Comparator;


public class BlackNode<K, V> extends Node<K, V> {


    public BlackNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        super(key, value, left, right);
    }




    @Override
    public rebuildNode deleteNode() {
        EmptyNode<K, V> emptyNode = new EmptyNode<>(key);
        return new rebuildNode<>(true, emptyNode);
    }

    @Override
    @Test
    protected int checkDepth(int count, int minCount) { // test method
        count++;
        minCount = left().checkDepth(count, minCount);
        minCount = right().checkDepth(count, minCount);
        return minCount;
    }


    @Override
    protected boolean isNotEmpty() {
        return true;
    }

    @Override
    public Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        return new BlackNode<>(key, value, left, right);
    }


    @Override
    Node<K, V> insBalance() {
        Rotate spin = left.checkRotate(Rotate.L);

        if (spin == Rotate.R) {
            Node<K, V> leftChild = new BlackNode<>(left.left().getKey(), left.left().getValue(), left.left().left(), left.left().right());
            Node<K, V> rightChild = new BlackNode<>(getKey(), getValue(), left.right(), right);
            return new RedNode<>(left.getKey(), left.getValue(), leftChild, rightChild);

        } else if (spin == Rotate.LR) {
            Node<K, V> leftChild = new BlackNode<>(left.getKey(), left.getValue(), left.left(), left.right().left());
            Node<K, V> rightChild = new BlackNode<>(getKey(), getValue(), left.right().right(), right);
            return new RedNode<>(left.right().getKey(), left.right().getValue(), leftChild, rightChild);

        }

        spin = right.checkRotate(Rotate.R);
        if (spin == Rotate.L) {
            Node<K, V> leftChild = new BlackNode<>(getKey(), getValue(), left, right.left());
            Node<K, V> rightChild = new BlackNode<>(right.right().getKey(), right.right().getValue(), right.right().left(), right.right().right());
            return new RedNode<>(right.getKey(), right.getValue(), leftChild, rightChild);

        } else if (spin == Rotate.RL) {
            Node<K, V> leftChild = new BlackNode<>(getKey(), getValue(), left, right.left().left());
            Node<K, V> rightChild = new BlackNode<>(right.getKey(), right.getValue(), right.left().right(), right.right());
            return new RedNode<>(right.left().getKey(), right.left().getValue(), leftChild, rightChild);

        }

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
    @SuppressWarnings("unchecked")
    public rebuildNode replaceNode(Node<K, V> parent, Comparator ctr) {
        Node<K, V> newNode;
        if (!this.left().isNotEmpty() && !this.right().isNotEmpty()) { //自身を削除する
            return deleteNode();//黒が1つ減るので木のバランスを取る
        } else if (this.left().isNotEmpty() && !this.right().isNotEmpty()) { //左の部分木を昇格させる
            newNode = createNode(left().getKey(), left().getValue(), left().left(), left().right());
            if (!this.left().isRed()) //昇格させる木のrootが黒だったらバランスを取る
                return new rebuildNode(true, newNode);
            return new rebuildNode(false, newNode);
        } else if (!this.left().isNotEmpty() && this.right().isNotEmpty()) { //右の部分木を昇格させる
            newNode = createNode(right().getKey(), right().getValue(), right().left(), right().right());
            if (!this.right().isRed()) //昇格させる木のrootが黒だったらバランスを取る
                return new rebuildNode(true, newNode);
            return new rebuildNode(false, newNode);
        } else {//子ノードが左右にある場合 二回目はここには入らない
            //左の部分木の最大の値を持つNodeと自身を置き換える
            Node<K, V> cur = this.left();
            while (cur.right().isNotEmpty()) { //左の部分期の最大値を持つNodeを取得する
                cur = cur.right();
            }
            if (this.left().right().isNotEmpty()) { //左の部分木が右の子を持っているか
                rebuildNode<K, V> leftSubTreeNodeRebuildNode = this.left().deleteSubTreeMaxNode(null, ctr, Rotate.L);//最大値を削除した左の部分木を返す。rootはthisと同じ。
                if (leftSubTreeNodeRebuildNode.rebuild()) {
                    Node<K, V> leftSubTreeNode = leftSubTreeNodeRebuildNode.getNode();
                    Node<K, V> newParent = createNode(cur.getKey(), cur.getValue(), leftSubTreeNode, this.right());
                    return leftSubTreeNode.deleteBalance(newParent, ctr);
                }
                Node<K, V> leftSubTreeNode = leftSubTreeNodeRebuildNode.getNode();
                newNode = createNode(cur.getKey(), cur.getValue(), leftSubTreeNode, this.right()); //rootをcurと入れ替えることでNodeの削除は完了する
                return new rebuildNode(false, newNode);
            } else {
                rebuildNode<K, V> leftSubTreeNodeRebuildNode = this.left().replaceNode(this, ctr);//右の子がいなかった場合、左の子を昇格させるだけで良い。
                if (leftSubTreeNodeRebuildNode.rebuild()) {
                    Node<K, V> node = leftSubTreeNodeRebuildNode.getNode();
                    Node<K, V> newParent = createNode(this.left().getKey(), this.left().getValue(), node, this.right());
                    return node.deleteBalance(newParent, ctr);
                }
                Node<K,V> leftSubTreeNode = leftSubTreeNodeRebuildNode.getNode();
                newNode = createNode(this.left().getKey(), this.left().getValue(), leftSubTreeNode, this.right());
                return new rebuildNode(false, newNode);
            }
        }
    }
}
