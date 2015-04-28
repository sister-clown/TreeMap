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

    public void setRebuildFlag(boolean rebuildFlag) {
        this.rebuildFlag = rebuildFlag;
    }

    public Node<K, V> left() {
        return left;
    }

    public int compare(K parentKey) {
        return (parentKey.hashCode() - getKey().hashCode());
    }

    public Optional<V> get(K key) {
        Node<K, V> cur = this;

        while (cur.isNotEmpty()) {
            int result = compare(key);
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

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
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

    public Node<K, V> delete(K key, Node<K, V> parent, Rotate side) throws RotateParent {
        if (this.isNotEmpty()) {
            int result = compare(key);

            try {
                Node<K, V> node = null;
                if (result > 0) {
                    node = right.delete(key, this, Rotate.R);
                    if (node == null)
                        return null;
                    if (parent == null)
                        return node;
                } else if (result < 0) {
                    node = left.delete(key, this, Rotate.L);
                    if (node == null)
                        return null;
                    if (parent == null)
                        return node;
                } else if (result == 0) {
                    node = replaceNode(parent);
                    if (parent == null)// equals
                        return node;
                    if (side == Rotate.R)
                        return parent.createNode(parent.getKey(), parent.getValue(), parent.left(), node);
                    else
                        return parent.createNode(parent.getKey(), parent.getValue(), node, parent.right());
                }
                if (side == Rotate.L)
                    return parent.createNode(parent.getKey(), parent.getValue(), node, parent.right());
                else
                    return parent.createNode(parent.getKey(), parent.getValue(), parent.left(), node);
            } catch (RotateParent e) {
                Node node = e.getParent();
                if (parent != null)
                    return node.deleteBalance(parent);
                return node;
            }
        }
        return null; // no key
    }


    public Node<K, V> deleteSubTreeMaxNode(Node<K, V> parent, Rotate side) throws RotateParent {
        Node<K, V> node;
        try {
            if (right().isNotEmpty()) {//最大値のノードが取得できるまで潜る
                node = right().deleteSubTreeMaxNode(this, Rotate.R);
              } else {
                node = this.replaceNode(parent);
            }
        } catch (RotateParent e) {
            node = e.getParent();
            if (parent == null)
                throw e;
            return node.deleteBalance(parent);
        }
        if (parent == null)
            return node;
        if (side == Rotate.R)
            return parent.createNode(parent.getKey(), parent.getValue(), parent.left(), node);
        else
            return parent.createNode(parent.getKey(), parent.getValue(), node, parent.right());

    }

    public Node deleteBalance(Node<K, V> parent) throws RotateParent {
        if (!isRed()) {
            if (0 > compare(parent.getKey())) { //自身がどちらの子かを調べる
                boolean rightChild = parent.left().right().isRed();
                boolean leftChild = parent.left().left().isRed();

                if (!parent.isRed()) { //親が黒
                    if (!parent.left().isRed()) { //左の子が黒
                        if (!rightChild && !leftChild)
                            throw new RotateParent(rebuildThree(parent, Rotate.R));
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
                            throw new RotateParent(rebuildThree(parent, Rotate.L));
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
        if (0 > (compare(parent.getKey())))
            return parent.createNode(parent.getKey(), parent.getValue(), parent.left(), this);
        else
            return parent.createNode(parent.getKey(), parent.getValue(), this, parent.right());
    }

    protected Node<K, V> rebuildTwo(Node<K, V> parent, Rotate side) throws RotateParent { // case2
        if (side == Rotate.L) { // rotate Left
            Node<K, V> node = parent.right();
            Node<K, V> leftSubTreeRoot = node.createNode(parent.getKey(), parent.getValue(), this, node.left()); // check
            Node<K, V> leftNode = this.deleteBalance(leftSubTreeRoot);
            Node<K, V> rightNode = node.right();
            return parent.createNode(node.getKey(), node.getValue(), leftNode, rightNode);
        } else {  // rotate Right
            Node<K, V> node = parent.left();
            Node<K, V> rightSubTreeRoot = node.createNode(parent.getKey(), parent.getValue(), node.right(), this);
            Node<K, V> rightNode = this.deleteBalance(rightSubTreeRoot);
            Node<K, V> leftNode = node.left();
            return parent.createNode(node.getKey(), node.getValue(), leftNode, rightNode);
        }
    }

    protected Node rebuildThree(Node<K, V> parent, Rotate side) { // case3 再帰
        if (side == Rotate.L) {
            Node<K, V> rightNode;
            if (parent.right().isNotEmpty())
                rightNode = new RedNode<K, V>(parent.right().getKey(), parent.right().getValue(), parent.right().left(), parent.right().right()); // check
            else
                rightNode = new EmptyNode<>();
            return parent.createNode(parent.getKey(), parent.getValue(), this, rightNode);
        } else {
            Node<K, V> leftNode;
            if (parent.left().isNotEmpty())
                leftNode = new RedNode<K, V>(parent.left().getKey(), parent.left().getValue(), parent.left().left(), parent.left().right()); // check
            else
                leftNode = new EmptyNode<>();
            return parent.createNode(parent.getKey(), parent.getValue(), leftNode, this);
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


    protected abstract boolean isNotEmpty();

    public abstract Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right);

    abstract Node<K, V> insBalance();

    abstract Rotate checkRotate(Rotate side);

    abstract boolean isRed();

    public abstract Node replaceNode(Node<K, V> parent) throws RotateParent;

    protected abstract Node deleteNode() throws RotateParent;

    protected abstract int checkBlackCount(int count); // test method
}
