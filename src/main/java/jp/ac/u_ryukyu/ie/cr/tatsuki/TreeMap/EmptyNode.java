package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

import java.util.Optional;

import static jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.Rotate.*;

/**
 * Created by e115731 on 15/03/25.
 */
public class EmptyNode<K, V> extends Node<K, V> {

    public EmptyNode() {
        super(null, null);
    }

    @Override
    protected boolean exitNode() {
        return false;
    }


    @Override
    public Node<K, V> createNode(K key, V value, Node<K, V> left, Node<K, V> right) {
        return new RedNode<K, V>(key, value, new EmptyNode<K, V>(), new EmptyNode<K, V>());
    }


    public Node<K, V> put(K k, V value) {
        return new RedNode(k, value, new EmptyNode<K, V>(), new EmptyNode<K, V>());
    }

    @Override
    public Node replaceNode(Node<K, V> parent) { // not use method
        return this;
    }

    @Override
    protected Node deleteNode() { //not use method
        return this;
    }

    @Override
    Node insBalance() {
        return this;
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
    public Optional<V> get(K key) {
        return Optional.ofNullable((V) getValue());
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
    DeleteRebuildFlag RebuildDelete(Rotate side) { //not use method
        return null;
    }

    @Override
    DeleteRebuildFlag firstChildRebuildDelete(Rotate side) {
        return DeleteRebuildFlag.allBlack;
    }

    @Override
    boolean secondChildRebuildDelete() {
        return true;
    }

}
