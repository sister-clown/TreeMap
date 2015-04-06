package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import java.util.*;

/**
 * Created by e115731 on 15/03/23.
 */
public abstract class Node<K, V> {

    protected K key;
    protected V value;
    protected Node<K, V> right;
    protected Node<K, V> left;
    protected boolean rebuildFlag = false;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Node(K key, V value, Node<K, V> left, Node<K, V> right) {
        this.key = key;
        this.value = value;
        this.right = right;
        this.left = left;
    }

    public Node<K, V> left() {
        return left;
    }


    public Optional<V> get(K key) {

        Node<K, V> cur = this;

        while (cur.exitNode()) {
            int result = key.hashCode() - cur.getKey().hashCode();

            if (result > 0)
                cur = cur.right();

            else if (result < 0)
                cur = cur.left();

            else if (result == 0)
                return Optional.ofNullable(cur.getValue());
        }

        return Optional.ofNullable(null);
    }


    public Node<K, V> right() {
        return right;
    }

    public boolean getRebuildFlag() {
        return rebuildFlag;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }


    public Node<K, V> put(K k, V value) {

        int result = k.hashCode() - this.key.hashCode();

        if (result > 0) {
            Node<K, V> node = right.put(k, value);
            node = createNode(key, this.value, left, node);
            return node.insBalance();
        } else if (result < 0) {
            Node node = left.put(k, value);
            return createNode(key, this.value, node, right).insBalance();
        }

        return createNode(key, value, left, right); // equals

    }

    public Node<K, V> delete(K key, Node<K, V> parent) {
        if (this.exitNode()) {
            int result = key.hashCode() - this.key.hashCode();

            if (result > 0) {
                Node<K, V> node = right.delete(key, this);
                if (parent == null || node == null)
                    return node;
                return node.deleteBalance(parent);

            } else if (result < 0) {
                Node node = left.delete(key, this);
                if (parent == null || node == null)
                    return node;
                return node.deleteBalance(parent);

            } else if (result == 0) {
                Node node = replaceNode(parent); // equals
                if (parent == null || node == null)
                    return node;
                return node.deleteBalance(parent);
            }
        }
        return null; // no key
    }


    public abstract Node<K, V> deleteSubTreeMaxNode(Node<K, V> parent) ;

    public void setRebuildFlag(boolean flag) {
        this.rebuildFlag = flag;

    }


    protected Node<K, V> rebuildTwo(Node<K, V> parent, Rotate side) { // case2
        if (side == Rotate.L) { // rotate Left
            Node<K, V> leftSubTreeRoot = parent.right().createNode(parent.getKey(), parent.getValue(), this, parent.right().left()); // check
            Node<K, V> leftNode = this.deleteBalance(leftSubTreeRoot);
            Node<K, V> rightNode = parent.right().right();
            return parent.createNode(parent.right().getKey(), parent.right().getValue(), leftNode, rightNode);

        } else {  // rotate Right
            Node<K, V> rightSubTreeRoot = parent.left().createNode(parent.getKey(), parent.getValue(), parent.left().right(), this);
            Node<K, V> rightNode = this.deleteBalance(rightSubTreeRoot);
            Node<K, V> leftNode = parent.left().left();
            return parent.createNode(parent.left().getKey(), parent.left().getValue(), leftNode, rightNode);

        }

    }

    protected Node rebuildThree(Node<K, V> parent, Rotate side) { // case3 再起
        if (side == Rotate.L) {
            Node<K, V> rightNode;
            if (parent.right().exitNode())
                rightNode = new RedNode<K, V>(parent.right().getKey(), parent.right().getValue(), parent.right().left(), parent.right().right()); // check
            else
                rightNode = new EmptyNode<>();
            Node<K, V> newParent = parent.createNode(parent.getKey(), parent.getValue(), this, rightNode);
            newParent.setRebuildFlag(true);
            return newParent;

        } else {
            Node<K, V> leftNode;
            if (parent.left().exitNode())
                leftNode = new RedNode<K, V>(parent.left().getKey(), parent.left().getValue(), parent.left().left(), parent.left().right()); // check
            else
                leftNode = new EmptyNode<>();
            Node<K, V> newParent = parent.createNode(parent.getKey(), parent.getValue(), leftNode, this);
            newParent.setRebuildFlag(true);
            return newParent;

        }

    }

    protected Node rebuildFour(Node<K, V> parent, Rotate side) { //case 4
        if (side == Rotate.R) {
            Node<K, V> leftNode = new RedNode<K, V>(parent.left().getKey(), parent.left().getValue(), parent.left().left(), parent.left().right());
            return new BlackNode<K, V>(parent.getKey(), parent.getValue(), leftNode, this);

        } else {
            Node<K, V> rightNode = new RedNode<K, V>(parent.right().getKey(), parent.right().getValue(), parent.right().left(), parent.right().right());
            return new BlackNode<K, V>(parent.getKey(), parent.getValue(), this, rightNode);

        }

    }

    protected Node rebuildfive(Node<K, V> parent, Rotate side) { //case5

        if (side == Rotate.R) { // rotate Left

            Node<K, V> leftChild = new RedNode<K, V>(parent.left().getKey(), parent.left().getValue(), parent.left().left(), parent.left().right().left());
            Node<K, V> rightChild = parent.left().right().right();
            Node<K, V> leftSubTreeRoot = new BlackNode<K, V>(parent.left().right().getKey(), parent.left().right().getValue(), leftChild, rightChild);
            Node<K, V> newParent = parent.createNode(parent.getKey(), parent.getValue(), leftSubTreeRoot, this);
            return this.rebuildsix(newParent, Rotate.R);

        } else {  // rotate Right 修正済み
            Node<K, V> leftChild = parent.right().left().left();
            Node<K, V> rightChild = new RedNode<K, V>(parent.right().getKey(), parent.right().getValue(), parent.right().left().right(), parent.right().right());
            Node<K, V> rightSubTreeRoot = new BlackNode<K, V>(parent.right().left().getKey(), parent.right().left().getValue(), leftChild, rightChild);
            Node<K, V> newParent = parent.createNode(parent.getKey(), parent.getValue(), this, rightSubTreeRoot);
            return this.rebuildsix(newParent, Rotate.L);

        }
    }

    protected Node rebuildsix(Node<K, V> parent, Rotate side) { //case6
        if (side == Rotate.L) { // rotate Left
            Node<K, V> leftChild = parent.right().createNode(parent.getKey(), parent.getValue(), this, parent.right().left()); //check
            Node<K, V> rightChild = new BlackNode<K, V>(parent.right().right().getKey(), parent.right().right().getValue(), parent.right().right().left(), parent.right().right().right());
            return parent.createNode(parent.right().getKey(), parent.right().getValue(), leftChild, rightChild);

        } else {  // rotate Right
            Node<K, V> leftChild = new BlackNode<K, V>(parent.left().left().getKey(), parent.left().left().getValue(), parent.left().left().left(), parent.left().left().right());
            Node<K, V> rightChild = parent.left().createNode(parent.getKey(), parent.getValue(), parent.left().right(), this);
            return parent.createNode(parent.left().getKey(), parent.left().getValue(), leftChild, rightChild);

        }

    }




    protected abstract boolean exitNode();

    public abstract Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right);

    abstract Node<K, V> insBalance();

    public abstract Node deleteBalance(Node<K, V> Parent);

    abstract Rotate checkRotate(Rotate side);

    abstract boolean checkColor();

    abstract DeleteRebuildFlag RebuildDelete(Rotate side);

    abstract DeleteRebuildFlag childRebuildDelete(Rotate side);

    public abstract Node replaceNode(Node<K, V> parent);

    protected abstract Node deleteNode();

    protected abstract int checkBlackCount(int count); // test method
}
