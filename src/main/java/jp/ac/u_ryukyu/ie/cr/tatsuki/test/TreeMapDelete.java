package jp.ac.u_ryukyu.ie.cr.tatsuki.test;

import jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.TreeMap;

import java.util.Optional;

/**
 * Created by e115731 on 15/04/04.
 */
public class TreeMapDelete {
    public static void main(String args[]) {
        TreeMap<Integer, Integer> map = new TreeMap();
        for (int count = 1; count < 15; count = count + 2) {
            map = map.put(count + 1, count + 1);
            map = map.put(count, count);
        }
        TreeMap<Integer, Integer> deleteMap = map.delete(13);

        System.out.println("end");
    }
}
