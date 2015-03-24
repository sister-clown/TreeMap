package jp.ac.u_ryukyu.ie.cr.tatsuki.test;

import jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.TreeMap;

/**
 * Created by e115731 on 15/03/24.
 */
public class TreeMapTest {
    public static void main(String args[]) {
        TreeMap<Integer, Integer> map = new TreeMap();
        for (int count = 0; count < 1000000; count++) {
            map = map.put(count, count);
        }

        System.out.println(map.get(999));
        System.out.println("end");
    }
}
