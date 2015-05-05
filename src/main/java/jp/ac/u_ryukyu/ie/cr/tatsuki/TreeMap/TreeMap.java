package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;


import org.junit.Test;

import java.util.Iterator;
import java.util.Optional;
import java.util.Stack;


/**
 * Created by e115731 on 15/03/23.
 */
public class TreeMap<K, V> {
    final Node<K, V> root;
    final int size;

    public TreeMap() {
        this.root = new EmptyNode();
        this.size = 0;
    }


    public Node getRoot() {
        return root;
    }

    public TreeMap(Node<K, V> root, int size) {
        this.root = root;
        this.size = size;
    }

    public Optional<V> get(final K key) {
        return root.get(key);
    }

    public TreeMap put(K key, V value) {

        if (key == null || value == null)  // null check
            throw new NullPointerException();

        if (isEmpty()) {
            Node<K, V> newRoot = new BlackNode(key, value, new EmptyNode(), new EmptyNode());
            return new TreeMap<K, V>(newRoot, size + 1);
        }

        Node<K, V> newEntry = root.put((Comparable<? super K>) key, value);
        Node<K, V> newRoot = new BlackNode(newEntry.getKey(), newEntry.getValue(), newEntry.left(), newEntry.right());
        return new TreeMap(newRoot, 0);
    }


    public boolean isEmpty() {
        return !root.isNotEmpty();
    }


    public TreeMap<K, V> delete(K key) {
        Node node = null;
        try {
            node = root.delete((Comparable<? super K>) key, null, Rotate.N);
        } catch (RotateParent rotateParent) {
        }
        if (node == null)
            return this; // not key
        if (!node.isNotEmpty())
            return new TreeMap(new EmptyNode<>(), 0);
        Node newRoot = new BlackNode(node.getKey(), node.getValue(), node.left(), node.right());
        return new TreeMap(newRoot, 0);
    }

    public Iterator<K> keys() {
        return new Iterator<K>() {
            Stack<Node> nodeStack = new Stack();
            Node currentNode = root;

            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public K next() {
                K key = (K) currentNode.getKey();

                if (currentNode.left().isNotEmpty()) {
                    nodeStack.push(currentNode);
                    currentNode = currentNode.left();
                    return key;
                } else  if (currentNode.right().isNotEmpty()) {
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
