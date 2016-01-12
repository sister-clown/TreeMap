package jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap;


public class rebuildNode<K,V> {
    private final Boolean rebuild;
    private final Node<K,V> node;

    public rebuildNode(Boolean l, Node<K, V> node){
        this.rebuild = l;
        this.node = node;
    }

    public boolean rebuild(){
        return rebuild;
    }

    public Node<K,V> getNode(){
        return node;
    }

    public Boolean notEmpty(){
        return node != null;
    }
}
