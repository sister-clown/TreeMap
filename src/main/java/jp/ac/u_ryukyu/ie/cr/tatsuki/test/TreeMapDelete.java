package jp.ac.u_ryukyu.ie.cr.tatsuki.test;

import jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.TreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

/**
 * Created by e115731 on 15/04/04.
 */
public class TreeMapDelete {
    public static void main(String args[]) {
        TreeMap<Integer, Integer> map = new TreeMap();
        for (int count = 1; count < 2000; count++) {
            map = map.put(count, count);
        }

        ArrayList<Integer> list = new ArrayList();
        for (int i = 1; i < 2000; i++) {
            list.add(i);
        }

        Collections.shuffle(list);
        for (Integer num : list) {
            System.out.println(num);
            TreeMap newMap = map.delete(num);
            map = newMap;
            map.checkBlackCount();
        }
        for (Integer num : list) {
         //   System.out.println(num);
        }

        System.out.println("end");
    }

}
