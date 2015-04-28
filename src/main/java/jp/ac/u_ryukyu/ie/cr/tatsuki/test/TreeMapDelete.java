package jp.ac.u_ryukyu.ie.cr.tatsuki.test;

import jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.RotateParent;
import jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.TreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

/**
 * Created by e115731 on 15/04/04.
 */
public class TreeMapDelete {
    public static void main(String args[]) throws RotateParent {
        TreeMap<Integer, Integer> map = new TreeMap();
        for (int count = 1; count < 3000; count++) {
            map = map.put(count, count);
        }

        ArrayList<Integer> list = new ArrayList();
        for (int i = 1; i < 3000; i++) {
            list.add(i);
        }

//        test(map);
        Collections.shuffle(list);
        for (Integer num : list) {
            System.out.println(num);
            TreeMap newMap = map.delete(num);
            map = newMap;
            map.checkBlackCount();
        }

        System.out.println("end");
    }

    public static void test(TreeMap map) throws RotateParent {
        TreeMap newMap = map.delete(13);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(26);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(5);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(3);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(29);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(8);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(22);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(2);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(20);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(11);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(19);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(6);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(25);
        map = newMap;
        map.checkBlackCount();
        newMap = map.delete(12);
        map = newMap;
        map.checkBlackCount();
    }
}
