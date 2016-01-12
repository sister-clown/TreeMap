package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

/**
 * Generic Exception not support
 **/
public class RotateParent extends Exception {

    Node parent;
    public RotateParent(Node node) {
        this.parent = node;
    }


    public Node getParent() {
        return parent;
    }
}