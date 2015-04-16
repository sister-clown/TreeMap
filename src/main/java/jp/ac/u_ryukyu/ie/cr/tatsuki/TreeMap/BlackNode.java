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
    public Node deleteBalance(Node<K, V> parent) {
        if (rebuildFlag) {
            Rotate editNodeSide;
            DeleteRebuildFlag flag;
            if (0 > compare(parent)) { //自身がどちらの子かを調べる
                editNodeSide = Rotate.R;//右の子
                flag = parent.right().childRebuildDelete(Rotate.R);

                if (parent.right().isBlack()) {
                    boolean rightChild = this.right().checkColor();
                    boolean leftChild = this.left().checkColor();

                    if (!rightChild && !leftChild)
                        return DeleteRebuildFlag.allBlack;

                        if (rightChild)
                            return DeleteRebuildFlag.five;
                        else
                            return DeleteRebuildFlag.six;

                } else {
                    //red
                }

            } else {
                editNodeSide = Rotate.L;//左の子
                flag = parent.right().childRebuildDelete(Rotate.L);

                if (parent.left().isBlack()) {
                    boolean rightChild = this.right().checkColor();
                    boolean leftChild = this.left().checkColor();

                    if (!rightChild && !leftChild)
                        return DeleteRebuildFlag.allBlack;

                    if (leftChild)
                        return DeleteRebuildFlag.five;
                    else
                        return DeleteRebuildFlag.six;

                } else {
                    //red
                }
            }


            if (flag == DeleteRebuildFlag.allBlack) {
                if (parent.isBlack())
                    return rebuildThree(parent, editNodeSide);
                else
                    return rebuildFour(parent, editNodeSide);
            }

            switch (flag) {
                case two:
                    return rebuildTwo(parent, editNodeSide);
                case five:
                    return rebuildfive(parent, editNodeSide);
                case six:
                    return rebuildsix(parent, editNodeSide);
            }
        }
        if (0 > (compare(parent)))
            return parent.createNode(parent.getKey(), parent.getValue(), parent.left(), this);
        else
            return parent.createNode(parent.getKey(), parent.getValue(), this, parent.right());

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
    boolean checkColor() {
        return false;
    }

    @Override
    DeleteRebuildFlag RebuildDelete(Rotate side) {

        DeleteRebuildFlag flag;
        if (side == Rotate.R) {//どっち側のNodeを編集したか 右側を編集して来たならRが来る
            flag = this.left().childRebuildDelete(side);
        } else {
            flag = this.right().childRebuildDelete(side);
        }

        if (flag == DeleteRebuildFlag.allBlack)
            return DeleteRebuildFlag.three;

        return flag;
    }

    @Override
    DeleteRebuildFlag childRebuildDelete(Rotate side) {

        boolean rightChild = this.right().checkColor();
        boolean leftChild = this.left().checkColor();

        if (!rightChild && !leftChild)
            return DeleteRebuildFlag.allBlack;

        if (side == Rotate.R) {//どっち側のNodeを編集したか 右側を編集して来たならRが来る
            if (rightChild)
                return DeleteRebuildFlag.five;
            else
                return DeleteRebuildFlag.six;
        }

        if (side == Rotate.L) {
            if (leftChild)
                return DeleteRebuildFlag.five;
            else
                return DeleteRebuildFlag.six;
        }

        return null;
    }

    @Override
    public Node replaceNode(Node<K, V> parent) {

        Node<K, V> newNode = null;
        if (!this.left().exitNode() && !this.right().exitNode()) { //自身を削除する
            return deleteNode();//黒が1つ減るので木のバランスを取る

        } else if (this.left().exitNode() && !this.right().exitNode()) { //左の部分木を昇格させる
            newNode = createNode(left().getKey(), left().getValue(), left().left(), left().right());
            if (!this.left().checkColor()) //昇格させる木のrootが黒だったらバランスを取る
                newNode.setRebuildFlag(true);
            return newNode;

        } else if (!this.left().exitNode() && this.right().exitNode()) { //右の部分木を昇格させる
            newNode = createNode(right().getKey(), right().getValue(), right().left(), right().right());
            if (!this.right().checkColor()) //昇格させる木のrootが黒だったらバランスを取る
                newNode.setRebuildFlag(true);
            return newNode;

        } else {//子ノードが左右にある場合
            //左の部分木の最大の値を持つNodeと自身を置き換える
            Node<K, V> cur = this.left();

            while (cur.right().exitNode()) { //左の部分期の最大値を持つNodeを取得する
                cur = cur.right();
            }


            if (this.left().right().exitNode()) { //左の部分木が右の子を持っているか
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
