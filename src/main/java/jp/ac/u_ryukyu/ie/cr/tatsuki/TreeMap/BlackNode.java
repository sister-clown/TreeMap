package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import static jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.Rotate.*;


public class BlackNode<K, V> extends Node<K, V> {


    public BlackNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        super(key, value, left, right);
    }


    @Override
    public Node deleteNode() {
        EmptyNode<K, V> emptyNode = new EmptyNode<K, V>();
        return emptyNode;
    }

    @Override
    public Node deleteBalance(Node<K, V> parent) {
        if (rebuildFlag) {
            Rotate editNodeSide;
            if (0 > (parent.getKey().hashCode() - this.getKey().hashCode()))
                editNodeSide = Rotate.R;
            else
                editNodeSide = Rotate.L;

            DeleteRebuildFlag flag = parent.RebuildDelete(editNodeSide);


            switch (flag) {
                case two:
                    return rebuildTwo(parent, editNodeSide);
                case three:
                    return rebuildThree(parent, editNodeSide);
                case four:
                    return rebuildFour(parent, editNodeSide);
                case five:
                    return rebuildfive(parent, editNodeSide);
                case six:
                    return rebuildsix(parent, editNodeSide);
            }
        }
        return this;
    }





    @Override
    protected boolean exitNode() {
        return true;
    }

    @Override
    public Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        return new BlackNode<K, V>(key, value, left, right);
    }


    @Override
    Node insBalance() {
        Rotate spin = left.firstCheckColor(L);

        if (spin == R) {
            Node<K, V> leftChild = new BlackNode<K, V>(left.left().getKey(), left.left().getValue(), left.left().left(), left.left().right());
            Node<K, V> rightChild = new BlackNode<K, V>(getKey(), getValue(), left.right(), right);
            return new RedNode<K, V>(left.getKey(), left.getValue(), leftChild, rightChild);

        } else if (spin == LR) {
            Node<K, V> leftChild = new BlackNode<K, V>(left.getKey(), left.getValue(), left.left(), left.right().left());
            Node<K, V> rightChild = new BlackNode<K, V>(getKey(), getValue(), left.right().right(), right);
            return new RedNode<K, V>(left.right().getKey(), left.right().getValue(), leftChild, rightChild);

        }

        spin = right.firstCheckColor(R);
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
    Rotate firstCheckColor(Rotate side) {
        return N;
    }

    @Override
    boolean secondCheckColor() {
        return false;
    }

    @Override
    DeleteRebuildFlag RebuildDelete(Rotate side) {

        DeleteRebuildFlag flag;
        if (side == Rotate.R) {
            flag = this.left().firstChildRebuildDelete(side);
        } else {
            flag = this.right().firstChildRebuildDelete(side);
        }

        if (flag == DeleteRebuildFlag.allBlack)
            return DeleteRebuildFlag.three;

        return flag;
    }

    @Override
    DeleteRebuildFlag firstChildRebuildDelete(Rotate side) {

        boolean rightChild = this.right().secondChildRebuildDelete();
        boolean leftChild = this.left().secondChildRebuildDelete();

        if (rightChild && leftChild)
            return DeleteRebuildFlag.allBlack;

        if (side == Rotate.R) {
            if (rightChild)
                return DeleteRebuildFlag.six;
            else
                return DeleteRebuildFlag.five;
        }

        if (side == Rotate.L) {
            if (leftChild)
                return DeleteRebuildFlag.six;
            else
                return DeleteRebuildFlag.five;
        }

        return null;
    }

    @Override
    boolean secondChildRebuildDelete() {
        return true;
    }

    @Override
    public Node replaceNode(Node<K, V> parent) {

        Node<K, V> newNode = null;
        if (!this.left().exitNode() && !this.right().exitNode()) { //自身を削除する
            return deleteNode().deleteBalance(parent);//黒が1つ減るので木のバランスを取る

        } else if (this.left().exitNode() && !this.right().exitNode()) { //左の部分木を昇格させる
            newNode = createNode(left().getKey(), left().getValue(), left().left(), left().right());
            if (this.left().secondChildRebuildDelete())
                return newNode.deleteBalance(parent);
            else
                return newNode;

        } else if (!this.left().exitNode() && this.right().exitNode()) { //右の部分木を昇格させる
            newNode = createNode(right().getKey(), right().getValue(), right().left(), right().right());
            if (this.right().secondChildRebuildDelete())
                return newNode.deleteBalance(parent);
            else
                return newNode;

        } else {//子ノードが左右にある場合
            //左の部分木の最大の値を持つNodeと自身を置き換える
            Node<K, V> cur = this.left();

            while (cur.right().exitNode()) {
                cur = cur.right();
            }

            Node<K, V> leftSubTreeNode = new EmptyNode<>();

            if (this.left().right().exitNode()) {
                leftSubTreeNode = this.left().deleteSubTreeMaxNode(this);
            } else {
                leftSubTreeNode = this.left().replaceNode(this);
            }


            newNode = createNode(cur.getKey(), cur.getValue(), leftSubTreeNode, right());
            if (cur.secondChildRebuildDelete())
                return newNode.deleteBalance(parent);
            else
                return newNode;
        }

    }
}
