package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import java.util.*;

/**
 * Created by e115731 on 15/03/23.
 */
public abstract class Node<K, V> {

    protected  final K key;
    protected  final V value;
    protected  final Node<K, V> right;
    protected  final Node<K, V> left;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public Node(K key, V value,final Node<K, V> left, final Node<K, V> right) {
        this.key = key;
        this.value = value;
        this.right = right;
        this.left = left;
    }

    public Node<K, V> left() {
        return left;
    }

    public int compare(final K parentKey) {
        return (parentKey.hashCode() - getKey().hashCode());
    }

    public Node<K, V> right() {
        return right;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }


    public Optional<V> get(final K key) {
        Node<K, V> cur = this;
        while (cur.isNotEmpty()) {
            final int result = cur.compare(key);
            if (result > 0)
                cur = cur.right();
            else if (result < 0)
                cur = cur.left();
            else if (result == 0)
                return Optional.ofNullable(cur.getValue());
        }
        return Optional.ofNullable(null);
    }

    public Node<K, V> put(K k, V value) {
        int result = compare(k);
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

    public Tuple delete(K key, Node<K, V> parent) {
        if (this.isNotEmpty()) {
            int result = compare(key);
            if (result > 0) {
                Tuple node = right.delete(key, this);
                if (parent == null || node == null)
                    return node;
                return node.getNode().deleteBalance(parent, node.getRebuildFlag());
            } else if (result < 0) {
                Tuple node = left.delete(key, this);
                if (parent == null || node == null)
                    return node;
                return node.getNode().deleteBalance(parent, node.getRebuildFlag());
            } else if (result == 0) {
                Tuple node = replaceNode(parent); // equals
                if (parent == null || node == null)
                    return node;
                return node.getNode().deleteBalance(parent, node.getRebuildFlag());
            }
        }
        return null; // no key
    }


    public Tuple deleteSubTreeMaxNode(Node<K, V> parent) {
        Tuple node;
        if (right().isNotEmpty()) {//最大値のノードが取得できるまで潜る
            node = right().deleteSubTreeMaxNode(this);
            if (parent == null)
                return node;
            return node.getNode().deleteBalance(parent, node.getRebuildFlag());
        }
        node = this.replaceNode(parent);
        if (parent == null)
            return node;
        return node.getNode().deleteBalance(parent, node.getRebuildFlag());
    }

    public Tuple deleteBalance(Node<K, V> parent, boolean flag) {
        if (flag && !isRed()) {
            if (0 > compare(parent.getKey())) { //自身がどちらの子かを調べる
                boolean rightChild = parent.left().right().isRed();
                boolean leftChild = parent.left().left().isRed();

                if (!parent.isRed()) { //親が黒
                    if (!parent.left().isRed()) { //左の子が黒
                        if (!rightChild && !leftChild)
                            return rebuildThree(parent, Rotate.R);
                        if (rightChild)
                            return rebuildfive(parent, Rotate.R);
                        else if (leftChild)
                            return rebuildsix(parent, Rotate.R);
                    } else { //左の子が赤
                        return rebuildTwo(parent, Rotate.R);
                    }
                } else { //親が赤
                    if (!rightChild && !leftChild)
                        return rebuildFour(parent, Rotate.R);
                    if (rightChild)
                        return rebuildfive(parent, Rotate.R);
                    else if (leftChild)
                        return rebuildsix(parent, Rotate.R);
                }
            } else {
                boolean rightChild = parent.right().right().isRed();
                boolean leftChild = parent.right().left().isRed();

                if (!parent.isRed()) { //親が黒
                    if (!parent.right().isRed()) { //左の子が黒
                        if (!rightChild && !leftChild)
                            return rebuildThree(parent, Rotate.L);
                        if (rightChild)
                            return rebuildsix(parent, Rotate.L);
                        else if (leftChild)
                            return rebuildfive(parent, Rotate.L);
                    } else { //左の子が赤
                        return rebuildTwo(parent, Rotate.L);
                    }
                } else { //親が赤
                    if (!rightChild && !leftChild)
                        return rebuildFour(parent, Rotate.L);
                    if (rightChild)
                        return rebuildsix(parent, Rotate.L);
                    else if (leftChild)
                        return rebuildfive(parent, Rotate.L);
                }
            }
        }
        Node newParent = null;
        if (0 > (compare(parent.getKey())))
            newParent = parent.createNode(parent.getKey(), parent.getValue(), parent.left(), this);
        else
            newParent = parent.createNode(parent.getKey(), parent.getValue(), this, parent.right());
        return new Tuple(newParent, false);
    }

    protected Tuple rebuildTwo(Node<K, V> parent, Rotate side) { // case2
        if (side == Rotate.L) { // rotate Left
            Node<K, V> leftSubTreeRoot = parent.right().createNode(parent.getKey(), parent.getValue(), this, parent.right().left()); // check
            Tuple leftNode = this.deleteBalance(leftSubTreeRoot, true);
            Node<K, V> rightNode = parent.right().right();
            Node newParent = parent.createNode(parent.right().getKey(), parent.right().getValue(), leftNode.getNode(), rightNode);
            return new Tuple(newParent, false);
        } else {  // rotate Right
            Node<K, V> rightSubTreeRoot = parent.left().createNode(parent.getKey(), parent.getValue(), parent.left().right(), this);
            Tuple rightNode = this.deleteBalance(rightSubTreeRoot, true);
            Node<K, V> leftNode = parent.left().left();
            Node newParent = parent.createNode(parent.left().getKey(), parent.left().getValue(), leftNode, rightNode.getNode());
            return new Tuple(newParent, false);
        }

    }

    protected Tuple rebuildThree(Node<K, V> parent, Rotate side) { // case3 再起
        if (side == Rotate.L) {
            Node<K, V> rightNode;
            if (parent.right().isNotEmpty())
                rightNode = new RedNode<K, V>(parent.right().getKey(), parent.right().getValue(), parent.right().left(), parent.right().right()); // check
            else
                rightNode = new EmptyNode<>();
            Node<K, V> newParent = parent.createNode(parent.getKey(), parent.getValue(), this, rightNode);
            return new Tuple(newParent, true);
        } else {
            Node<K, V> leftNode;
            if (parent.left().isNotEmpty())
                leftNode = new RedNode<K, V>(parent.left().getKey(), parent.left().getValue(), parent.left().left(), parent.left().right()); // check
            else
                leftNode = new EmptyNode<>();
            Node<K, V> newParent = parent.createNode(parent.getKey(), parent.getValue(), leftNode, this);
            return new Tuple(newParent, true);
        }
    }

    protected Tuple rebuildFour(Node<K, V> parent, Rotate side) { //case 4
        if (side == Rotate.R) {
            Node<K, V> leftNode = new RedNode<K, V>(parent.left().getKey(), parent.left().getValue(), parent.left().left(), parent.left().right());
            Node newParent = new BlackNode<K, V>(parent.getKey(), parent.getValue(), leftNode, this);
            return new Tuple(newParent, false);
        } else {
            Node<K, V> rightNode = new RedNode<K, V>(parent.right().getKey(), parent.right().getValue(), parent.right().left(), parent.right().right());
            Node newParent = new BlackNode<K, V>(parent.getKey(), parent.getValue(), this, rightNode);
            return new Tuple(newParent, false);
        }
    }

    protected Tuple rebuildfive(Node<K, V> parent, Rotate side) { //case5
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

    protected Tuple rebuildsix(Node<K, V> parent, Rotate side) { //case6
        if (side == Rotate.L) { // rotate Left
            Node<K, V> leftChild = parent.right().createNode(parent.getKey(), parent.getValue(), this, parent.right().left()); //check
            Node<K, V> rightChild = new BlackNode<K, V>(parent.right().right().getKey(), parent.right().right().getValue(), parent.right().right().left(), parent.right().right().right());
            Node newParent = parent.createNode(parent.right().getKey(), parent.right().getValue(), leftChild, rightChild);
            return new Tuple(newParent, false);
        } else {  // rotate Right
            Node<K, V> leftChild = new BlackNode<K, V>(parent.left().left().getKey(), parent.left().left().getValue(), parent.left().left().left(), parent.left().left().right());
            Node<K, V> rightChild = parent.left().createNode(parent.getKey(), parent.getValue(), parent.left().right(), this);
            Node newParent = parent.createNode(parent.left().getKey(), parent.left().getValue(), leftChild, rightChild);
            return new Tuple(newParent, false);
        }
    }


    protected abstract boolean isNotEmpty();

    public abstract Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right);

    abstract Node<K, V> insBalance();

    abstract Rotate checkRotate(Rotate side);

    abstract boolean isRed();

    public abstract Tuple replaceNode(Node<K, V> parent);

    protected abstract Tuple deleteNode();

    protected abstract int checkBlackCount(int count); // test method
}
