package jp.ac.u_ryukyu.ie.cr.tatsuki.test;

import jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.RotateParent;
import jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.TreeMap;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by e115731 on 15/04/04.
 */
public class TreeMapDelete {

    @Test
    public static void main(String args[]) throws RotateParent {
        TreeMap<Integer, Integer> map = new TreeMap();
        for (int count = 1; count < 1000; count++) {
            map = map.put(count, count);
            map.checkDepth();
        }

        ArrayList<Integer> list = new ArrayList();
        for (int i = 1; i < 1000; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        for (Integer num : list) {
            System.out.println(num);
            TreeMap newMap = map.delete(num);
            map = newMap;
            map.checkDepth();
        }
        System.out.println("end");
    }
}
