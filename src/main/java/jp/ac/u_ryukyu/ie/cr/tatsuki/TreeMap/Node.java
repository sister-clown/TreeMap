package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import org.junit.Test;

import java.util.Comparator;
import java.util.Optional;


public abstract class Node<K, V> {

    protected K key;
    protected V value;
    protected Node<K, V> right;
    protected Node<K, V> left;

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

    @SuppressWarnings("unchecked")
    public int compare(K compareKey, Comparator ctr) {
        return ctr.compare(compareKey, this.getKey());
    }

    public Optional<V> get(K key, Comparator ctr) {
        Node<K, V> cur = this;
        while (cur.isNotEmpty()) {
            int result = cur.compare(key, ctr);
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


    public Node<K, V> put(K k, V value, Comparator ctr) {
        if (!isNotEmpty()) {
            return createNode(k, value, left, right);
        }
        int result = compare(k, ctr);
        if (result > 0) {
            Node<K, V> node = right.put(k, value, ctr);
            node = createNode(key, this.value, left, node);
            return node.insBalance();
        } else if (result < 0) {
            Node<K, V> node = left.put(k, value, ctr);
            return createNode(key, this.value, node, right).insBalance();
        }
        return createNode(key, value, left, right); // equals

    }

    @SuppressWarnings("unchecked")
    public rebuildNode delete(K key, Node<K, V> parent, Comparator ctr, Rotate side) {
        if (this.isNotEmpty()) {
            rebuildNode rebuildNode;
            int result = compare(key, ctr);
            if (result > 0) {
                rebuildNode = right.delete(key, this, ctr, Rotate.R);
            } else if (result < 0) {
                rebuildNode = left.delete(key, this, ctr, Rotate.L);
            } else { //Equal
                rebuildNode = replaceNode(parent, ctr);
            }
            if (parent == null)
                return rebuildNode;
            Node<K, V> node = rebuildNode.getNode();
            if (rebuildNode.rebuild()) {
                return node.deleteBalance(parent, ctr);
            }

            Node<K, V> newParent;
            if (side == Rotate.L)
                newParent = parent.createNode(parent.getKey(), parent.getValue(), node, parent.right());
            else
                newParent = parent.createNode(parent.getKey(), parent.getValue(), parent.left(), node);

            return new rebuildNode<>(false, newParent);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public rebuildNode deleteSubTreeMaxNode(Node<K, V> parent, Comparator ctr, Rotate side) {
        rebuildNode rebuildNode;
        Node<K, V> node;
        if (right().isNotEmpty()) {//最大値のノードが取得できるまで潜る
            rebuildNode = right().deleteSubTreeMaxNode(this, ctr, Rotate.R);
        } else {
            rebuildNode = this.replaceNode(parent, ctr);
        }
        if (parent == null)
            return rebuildNode;

        if (rebuildNode.rebuild()) {
            node = rebuildNode.getNode();
            return node.deleteBalance(parent, ctr);
        }
        if (side == Rotate.R)
            node = parent.createNode(parent.getKey(), parent.getValue(), parent.left(), rebuildNode.getNode());
        else
            node = parent.createNode(parent.getKey(), parent.getValue(), rebuildNode.getNode(), parent.right());
        return new rebuildNode<>(false, node);
    }

    public rebuildNode<K, V> deleteBalance(Node<K, V> parent, Comparator ctr) {
        Node<K, V> newNode;
        if (!isRed()) {
            if (0 > compare(parent.getKey(), ctr)) { //自身がどちらの子かを調べる
                boolean rightChild = parent.left().right().isRed();
                boolean leftChild = parent.left().left().isRed();

                if (!parent.isRed()) { //親が黒
                    if (!parent.left().isRed()) { //左の子が黒
                        if (!rightChild && !leftChild) {
                            newNode = rebuildThree(parent, Rotate.R);
                            return new rebuildNode<>(true, newNode);
                        }
                        if (rightChild) {
                            newNode = rebuildfive(parent, Rotate.R);
                            return new rebuildNode<>(false, newNode);
                        } else {
                            newNode = rebuildsix(parent, Rotate.R);
                            return new rebuildNode<>(false, newNode);
                        }
                    } else { //左の子が赤
                        newNode = rebuildTwo(parent, ctr, Rotate.R);
                        return new rebuildNode<>(false, newNode);
                    }
                } else { //親が赤
                    if (!rightChild && !leftChild) {
                        newNode = rebuildFour(parent, Rotate.R);
                        return new rebuildNode<>(false, newNode);
                    }
                    if (rightChild) {
                        newNode = rebuildfive(parent, Rotate.R);
                        return new rebuildNode<>(false, newNode);
                    } else {
                        newNode = rebuildsix(parent, Rotate.R);
                        return new rebuildNode<>(false, newNode);
                    }
                }
            } else {
                boolean rightChild = parent.right().right().isRed();
                boolean leftChild = parent.right().left().isRed();

                if (!parent.isRed()) { //親が黒
                    if (!parent.right().isRed()) { //左の子が黒
                        if (!rightChild && !leftChild) {
                            newNode = rebuildThree(parent, Rotate.L);
                            return new rebuildNode<>(true, newNode);
                        }
                        if (rightChild) {
                            newNode = rebuildsix(parent, Rotate.L);
                            return new rebuildNode<>(false, newNode);
                        } else {
                            newNode = rebuildfive(parent, Rotate.L);
                            return new rebuildNode<>(false, newNode);
                        }
                    } else { //左の子が赤
                        newNode = rebuildTwo(parent, ctr, Rotate.L);
                        return new rebuildNode<>(false, newNode);
                    }
                } else { //親が赤
                    if (!rightChild && !leftChild) {
                        newNode = rebuildFour(parent, Rotate.L);
                        return new rebuildNode<>(false, newNode);
                    }
                    if (rightChild) {
                        newNode = rebuildsix(parent, Rotate.L);
                        return new rebuildNode<>(false, newNode);
                    } else {
                        newNode = rebuildfive(parent, Rotate.L);
                        return new rebuildNode<>(false, newNode);
                    }
                }
            }
        }
        if (0 > (compare(parent.getKey(), ctr))) {
            newNode = parent.createNode(parent.getKey(), parent.getValue(), parent.left(), this);
            return new rebuildNode<>(false, newNode);
        } else {
            newNode = parent.createNode(parent.getKey(), parent.getValue(), this, parent.right());
            return new rebuildNode<>(false, newNode);
        }
    }

    protected Node<K, V> rebuildTwo(Node<K, V> parent, Comparator ctr, Rotate side) { // case2
        if (side == Rotate.L) { // rotate Left
            Node<K, V> node = parent.right();
            Node<K, V> leftSubTreeRoot = node.createNode(parent.getKey(), parent.getValue(), this, node.left()); // check
            rebuildNode<K, V> rebuildNode = new rebuildNode<>(false, leftSubTreeRoot);
            rebuildNode<K, V> leftNodeRebuildNode = this.deleteBalance(rebuildNode.getNode(), ctr);
            Node<K, V> rightNode = node.right();
            return parent.createNode(node.getKey(), node.getValue(), leftNodeRebuildNode.getNode(), rightNode);
        } else {  // rotate Right
            Node<K, V> node = parent.left();
            Node<K, V> rightSubTreeRoot = node.createNode(parent.getKey(), parent.getValue(), node.right(), this);
            rebuildNode<K, V> rightSubTreeRebuildNode = new rebuildNode<>(false, rightSubTreeRoot);
            rebuildNode<K, V> rightNodeRebuildNode = this.deleteBalance(rightSubTreeRebuildNode.getNode(), ctr);
            Node<K, V> leftNode = node.left();
            return parent.createNode(node.getKey(), node.getValue(), leftNode, rightNodeRebuildNode.getNode());
        }
    }

    protected Node<K, V> rebuildThree(Node<K, V> parent, Rotate side) { // case3 再帰
        if (side == Rotate.L) {
            Node<K, V> rightNode;
            if (parent.right().isNotEmpty())
                rightNode = new RedNode<>(parent.right().getKey(), parent.right().getValue(), parent.right().left(), parent.right().right()); // check
            else
                rightNode = new EmptyNode<>();
            return parent.createNode(parent.getKey(), parent.getValue(), this, rightNode);
        } else {
            Node<K, V> leftNode;
            if (parent.left().isNotEmpty())
                leftNode = new RedNode<>(parent.left().getKey(), parent.left().getValue(), parent.left().left(), parent.left().right()); // check
            else
                leftNode = new EmptyNode<>();
            return parent.createNode(parent.getKey(), parent.getValue(), leftNode, this);
        }
    }

    protected Node<K, V> rebuildFour(Node<K, V> parent, Rotate side) { //case 4
        if (side == Rotate.R) {
            Node<K, V> leftNode = new RedNode<>(parent.left().getKey(), parent.left().getValue(), parent.left().left(), parent.left().right());
            return new BlackNode<>(parent.getKey(), parent.getValue(), leftNode, this);
        } else {
            Node<K, V> rightNode = new RedNode<>(parent.right().getKey(), parent.right().getValue(), parent.right().left(), parent.right().right());
            return new BlackNode<>(parent.getKey(), parent.getValue(), this, rightNode);
        }
    }

    protected Node<K, V> rebuildfive(Node<K, V> parent, Rotate side) { //case5
        if (side == Rotate.R) { // rotate Left
            Node<K, V> leftChild = new RedNode<>(parent.left().getKey(), parent.left().getValue(), parent.left().left(), parent.left().right().left());
            Node<K, V> rightChild = parent.left().right().right();
            Node<K, V> leftSubTreeRoot = new BlackNode<>(parent.left().right().getKey(), parent.left().right().getValue(), leftChild, rightChild);
            Node<K, V> newParent = parent.createNode(parent.getKey(), parent.getValue(), leftSubTreeRoot, this);
            return this.rebuildsix(newParent, Rotate.R);
        } else {  // rotate Right 修正済み
            Node<K, V> leftChild = parent.right().left().left();
            Node<K, V> rightChild = new RedNode<>(parent.right().getKey(), parent.right().getValue(), parent.right().left().right(), parent.right().right());
            Node<K, V> rightSubTreeRoot = new BlackNode<>(parent.right().left().getKey(), parent.right().left().getValue(), leftChild, rightChild);
            Node<K, V> newParent = parent.createNode(parent.getKey(), parent.getValue(), this, rightSubTreeRoot);
            return this.rebuildsix(newParent, Rotate.L);
        }
    }

    protected Node<K, V> rebuildsix(Node<K, V> parent, Rotate side) { //case6
        if (side == Rotate.L) { // rotate Left
            Node<K, V> leftChild = parent.right().createNode(parent.getKey(), parent.getValue(), this, parent.right().left()); //check
            Node<K, V> rightChild = new BlackNode<>(parent.right().right().getKey(), parent.right().right().getValue(), parent.right().right().left(), parent.right().right().right());
            return parent.createNode(parent.right().getKey(), parent.right().getValue(), leftChild, rightChild);
        } else {  // rotate Right
            Node<K, V> leftChild = new BlackNode<>(parent.left().left().getKey(), parent.left().left().getValue(), parent.left().left().left(), parent.left().left().right());
            Node<K, V> rightChild = parent.left().createNode(parent.getKey(), parent.getValue(), parent.left().right(), this);
            return parent.createNode(parent.left().getKey(), parent.left().getValue(), leftChild, rightChild);
        }
    }


    protected abstract boolean isNotEmpty();

    public abstract Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right);

    abstract Node<K, V> insBalance();

    abstract Rotate checkRotate(Rotate side);

    abstract boolean isRed();

    public abstract rebuildNode replaceNode(Node<K, V> parent, Comparator ctr);

    protected abstract rebuildNode deleteNode();

    @Test
    // test method
    protected abstract int checkDepth(int count, int minCount);
}
