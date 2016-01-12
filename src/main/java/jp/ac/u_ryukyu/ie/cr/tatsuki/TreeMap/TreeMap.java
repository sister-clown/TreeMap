package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;


import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Stack;


public class TreeMap<K, V> {
    final Node<K, V> root;
    final Comparator comparator;

    public TreeMap() {
        this.root = new EmptyNode<>();
        this.comparator = new DefaultComparator<K>();
    }

    public TreeMap(Comparator comparator) {
        this.root = new EmptyNode<>();
        this.comparator = comparator;
    }

    public TreeMap(Node<K, V> root, Comparator comparator) {
        this.root = root;
        this.comparator = comparator;
    }

    public Node getRoot() {
        return root;
    }

    public Optional<V> get(final K key) {
        return root.get(key, this.comparator);
    }

    public TreeMap<K, V> put(K key, V value) {
        if (isEmpty()) {
            Node<K, V> newRoot = new BlackNode<>(key, value, new EmptyNode<K,V>(), new EmptyNode<K,V>());
            return new TreeMap<>(newRoot, this.comparator);
        }
        Node<K, V> newEntry = root.put(key, value, this.comparator);
        Node<K, V> newRoot = new BlackNode<>(newEntry.getKey(), newEntry.getValue(), newEntry.left(), newEntry.right());
        return new TreeMap<>(newRoot, this.comparator);
    }


    public boolean isEmpty() {
        return !root.isNotEmpty();
    }

    public TreeMap<K, V> delete(K key) {
        if (key == null)
            return this;
        rebuildNode rootRebuildNode = root.delete(key, null, comparator, Rotate.N);
        if (!rootRebuildNode.notEmpty())
            return this; // not key
        Node root = rootRebuildNode.getNode();
        if (!root.isNotEmpty())
            return new TreeMap<>(new EmptyNode<>(), this.comparator);
        BlackNode newRoot = new BlackNode<>(root.getKey(), root.getValue(),root.left(),root.right());
        return new TreeMap<>(newRoot, this.comparator);
    }

    public Iterator<K> keys() {
        return new Iterator<K>() {
            Stack<Node<K, V>> nodeStack = new Stack<>();
            Node<K, V> currentNode = root;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public K next() {
                K key = currentNode.getKey();

                if (currentNode.left().isNotEmpty()) {
                    nodeStack.push(currentNode);
                    currentNode = currentNode.left();
                    return key;
                } else if (currentNode.right().isNotEmpty()) {
                    currentNode = currentNode.right();
                    return key;
                } else if (nodeStack.isEmpty()) {
                    currentNode = null;
                    return key;
                }

                do {
                    currentNode = nodeStack.pop().right();
                    if (currentNode.isNotEmpty())
                        return key;

                } while (!nodeStack.isEmpty());
                return key;
            }
        };
    }

    @Test
    public void checkDepth() {
        root.checkDepth(0, 0);
        System.out.println("-----------------------------------");
    }
}
