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
        for (int count = 1; count < 200; count++) {
            map = map.put(count, count);
        }

        ArrayList<Integer> list = new ArrayList();
        for (int i = 1; i < 200; i++) {
            list.add(i);
        }


       // test(map);

        Collections.shuffle(list);
        for (Integer num : list) {
            System.out.println(num);
            TreeMap newMap = map.delete(num);
            map = newMap;
            map.checkBlackCount();
        }
        for (Integer num : list) {
            System.out.println(num);
        }

        System.out.println("end");
    }

    public static void test(TreeMap map) {
        TreeMap neMap = map.delete(11);
        neMap.checkBlackCount();
        neMap = neMap.delete(2);
        neMap.checkBlackCount();
        neMap = neMap.delete(8);
        neMap.checkBlackCount();
        neMap = neMap.delete(6);
        neMap.checkBlackCount();
        neMap = neMap.delete(5);
        neMap.checkBlackCount();
        neMap = neMap.delete(3);
        neMap.checkBlackCount();
        neMap = neMap.delete(12);
        neMap.checkBlackCount();
        neMap = neMap.delete(16);
        neMap.checkBlackCount();
        neMap = neMap.delete(13);
        neMap.checkBlackCount();
        neMap = neMap.delete(17);
        neMap.checkBlackCount();
        neMap = neMap.delete(19);
        neMap.checkBlackCount();
        neMap = neMap.delete(4);
        neMap.checkBlackCount();
        neMap = neMap.delete(7);
        neMap.checkBlackCount();
        neMap = neMap.delete(1);
        neMap.checkBlackCount();
        neMap = neMap.delete(10);
        neMap.checkBlackCount();
        neMap = neMap.delete(14);
        neMap.checkBlackCount();
        neMap = neMap.delete(9);
        neMap.checkBlackCount();
        neMap = neMap.delete(15);
        neMap.checkBlackCount();
        neMap = neMap.delete(18);
        neMap.checkBlackCount();


    }
}
