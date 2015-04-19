package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;

/**
 * Created by e115731 on 15/04/18.
 */
public class Tuple {

    Node node;
    boolean rebuildFlag;

    public Tuple(Node node, boolean rebuildFlag){
        this.node = node;
        this.rebuildFlag = rebuildFlag;
    }

    public Node getNode(){
        return node;
    }

    public boolean getRebuildFlag(){
        return rebuildFlag;
    }


}
