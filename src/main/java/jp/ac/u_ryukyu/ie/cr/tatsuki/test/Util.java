package jp.ac.u_ryukyu.ie.cr.tatsuki.test;

import java.util.TreeMap;

/**
 * Created by e115731 on 15/04/03.
 */
public class Util {
    public static void main(String args[]) {
        TreeMap<Integer,Integer> map = new TreeMap<Integer,Integer>();
        map.put(6, 6);
        map.put(5, 5);
        map.put(4, 4);
        map.put(3, 3);
        map.put(2, 2);
        map.put(1, 1);
        map.get(1);
        map.remove(6);
        map.get(1);
        System.out.println("test");
    }
}
