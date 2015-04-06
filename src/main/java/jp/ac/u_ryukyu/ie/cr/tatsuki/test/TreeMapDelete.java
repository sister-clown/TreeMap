package jp.ac.u_ryukyu.ie.cr.tatsuki.test;

import jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.TreeMap;

import java.util.Optional;
import java.util.Random;

/**
 * Created by e115731 on 15/04/04.
 */
public class TreeMapDelete {
    public static void main(String args[]) {
        TreeMap<Integer, Integer> map = new TreeMap();
        for (int count = 1; count < 50; count++) {
            map = map.put(count, count);
        }
        for (int count = 1; count < 50; count++) {
            Random ran = new Random();
            int num = ran.nextInt(50);
            TreeMap newMap = map.delete(34);
            map = newMap;
        }

        System.out.println("end");
    }
}
