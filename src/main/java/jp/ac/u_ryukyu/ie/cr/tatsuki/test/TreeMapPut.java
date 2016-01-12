package jp.ac.u_ryukyu.ie.cr.tatsuki.test;


import jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.TreeMap;
import org.junit.Test;

import java.util.Optional;

public class TreeMapPut {

    @Test
    public void TreeMapPutTest() {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int count = 100; count > -10; count--) {
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















