package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

/**
 * Created by e115731 on 15/04/17.
 */
public class RotateParent extends Exception {
    Node parent ;
    public RotateParent(Node node) {
        this.parent = node;
    }

    public Node getParent(){
        return parent;
    }
}
