package jp.ac.u_ryukyu.ie.cr.tatsuki.test;

import jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.TreeMap;

import java.util.Optional;

/**
 * Created by e115731 on 15/03/24.
 */
public class TreeMapTest {
    public static void main(String args[]) {
        TreeMap<Integer, Integer> map = new TreeMap();
        TreeMap<Integer, Integer> map1 = map.put(0,0);
        TreeMap<Integer, Integer> map2 = map1.put(1,1);
        TreeMap<Integer, Integer> map3 = map2.put(2,2);
        TreeMap<Integer, Integer> map4 = map3.put(3,3);
        TreeMap<Integer, Integer> map5 = map4.put(4,4);
        for (int count = 100; count > 0; count--) {
            map = map.put(count, count);
            map.checkDepth();
            System.out.println("-------------------------------------------");
        }

        for (int count = 100; count > -10; count--) {

            Optional<Integer> op = map.get(count);
            if (op.isPresent())
                System.out.println(op.get());
        }

        System.out.println("end");
    }
}















