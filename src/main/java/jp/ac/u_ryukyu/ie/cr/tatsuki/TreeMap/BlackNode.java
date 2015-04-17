package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import static jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.Rotate.*;


public class BlackNode<K, V> extends Node<K, V> {


    public BlackNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        super(key, value, left, right);
    }


    @Override
    public Node deleteNode() {
        EmptyNode<K, V> emptyNode = new EmptyNode<K, V>(key);
        emptyNode.setRebuildFlag(true);
        return emptyNode;
    }

    @Override
    protected int checkBlackCount(int count) { // test method
        count++;
        left().checkBlackCount(count);
        right().checkBlackCount(count);
        count--;
        return count;
    }



    @Override
    protected boolean isNotEmpty() {
        return true;
    }

    @Override
    public Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        return new BlackNode<K, V>(key, value, left, right);
    }


    @Override
    Node insBalance() {
        Rotate spin = left.checkRotate(L);

        if (spin == R) {
            Node<K, V> leftChild = new BlackNode<K, V>(left.left().getKey(), left.left().getValue(), left.left().left(), left.left().right());
            Node<K, V> rightChild = new BlackNode<K, V>(getKey(), getValue(), left.right(), right);
            return new RedNode<K, V>(left.getKey(), left.getValue(), leftChild, rightChild);

        } else if (spin == LR) {
            Node<K, V> leftChild = new BlackNode<K, V>(left.getKey(), left.getValue(), left.left(), left.right().left());
            Node<K, V> rightChild = new BlackNode<K, V>(getKey(), getValue(), left.right().right(), right);
            return new RedNode<K, V>(left.right().getKey(), left.right().getValue(), leftChild, rightChild);

        }

        spin = right.checkRotate(R);
        if (spin == L) {
            Node<K, V> leftChild = new BlackNode<K, V>(getKey(), getValue(), left, right.left());
            Node<K, V> rightChild = new BlackNode<K, V>(right.right().getKey(), (V) right.right().getValue(), right.right().left(), right.right().right());
            return new RedNode<K, V>(right.getKey(), right.getValue(), leftChild, rightChild);

        } else if (spin == RL) {
            Node leftChild = new BlackNode(getKey(), getValue(), left, right.left().left());
            Node rightChild = new BlackNode(right.getKey(), right.getValue(), right.left().right(), right.right());
            return new RedNode(right.left().getKey(), right.left().getValue(), leftChild, rightChild);

        }

        return this;
    }


    @Override
    Rotate checkRotate(Rotate side) {
        return N;
    }

    @Override
    boolean isRed() {
        return false;
    }



    /**
     * @param parent
     * @return
     */
    @Override
    public Node replaceNode(Node<K, V> parent) {

        Node<K, V> newNode = null;
        if (!this.left().isNotEmpty() && !this.right().isNotEmpty()) { //自身を削除する
            return deleteNode();//黒が1つ減るので木のバランスを取る

        } else if (this.left().isNotEmpty() && !this.right().isNotEmpty()) { //左の部分木を昇格させる
            newNode = createNode(left().getKey(), left().getValue(), left().left(), left().right());
            if (!this.left().isRed()) //昇格させる木のrootが黒だったらバランスを取る
                newNode.setRebuildFlag(true);
            return newNode;

        } else if (!this.left().isNotEmpty() && this.right().isNotEmpty()) { //右の部分木を昇格させる
            newNode = createNode(right().getKey(), right().getValue(), right().left(), right().right());
            if (!this.right().isRed()) //昇格させる木のrootが黒だったらバランスを取る
                newNode.setRebuildFlag(true);
            return newNode;

        } else {//子ノードが左右にある場合
            //左の部分木の最大の値を持つNodeと自身を置き換える
            Node<K, V> cur = this.left();

            while (cur.right().isNotEmpty()) { //左の部分期の最大値を持つNodeを取得する
                cur = cur.right();
            }


            if (this.left().right().isNotEmpty()) { //左の部分木が右の子を持っているか
                Node<K, V> leftSubTreeNode = this.left().deleteSubTreeMaxNode(null);//最大値を削除した左の部分木を返す。rootはthisと同じ。
                Node<K, V> newParent = createNode(cur.getKey(), cur.getValue(), leftSubTreeNode, this.right()); //rootをcurと入れ替えることでNodeの削除は完了する
                newNode = leftSubTreeNode.deleteBalance(newParent);

                return newNode;

            } else {
                Node<K, V> leftSubTreeNode = this.left().replaceNode(this);//右の子がいなかった場合、左の子を昇格させるだけで良い。
                Node newParent = createNode(this.left().getKey(), this.left().getValue(), leftSubTreeNode, this.right());
                return leftSubTreeNode.deleteBalance(newParent);

            }
        }

    }

}
