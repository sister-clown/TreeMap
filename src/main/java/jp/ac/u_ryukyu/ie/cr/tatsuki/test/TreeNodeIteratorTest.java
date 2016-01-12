package jp.ac.u_ryukyu.ie.cr.tatsuki.test;


import jp.ac.u_ryukyu.ie.cr.tatsuki.TreeMap.TreeMap;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Iterator;

public class TreeNodeIteratorTest {
    @Test
    public void getKeyTest() {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int attributeMaxCount = 10; attributeMaxCount < 1000; attributeMaxCount = attributeMaxCount + 10) {
            for (int count = 0; count < attributeMaxCount; count++) { //insertData
                map = map.put(count, count);
            }
            Iterator<Integer> it = map.keys();
            int iteratorCount = 0;
            while (it.hasNext()) { // count return iterator Attribute Count
                iteratorCount++;
                System.out.println(it.next());
            }
            Assert.assertTrue(iteratorCount == attributeMaxCount); // check
        }

    }
}
