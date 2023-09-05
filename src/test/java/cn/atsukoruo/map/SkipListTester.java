package cn.atsukoruo.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkipListTester {
    @Test
    public void insertTest() {
        System.out.println("---------------insertTest------------------");
        SkipList<Integer, Integer> skipList = new SkipList<>(0.7);
        for (int i = 0; i < 10; i++) {
            skipList.put(i, null);
        }
        skipList.print();
    }

    @Test
    public void searchTest() {
        Random random = new Random(System.currentTimeMillis());
        final int size = 10_0000;
        List<Integer> keys = new ArrayList<>();
        SkipList<Integer, Integer> skipList = new SkipList<>(0.5);
        for (int i = 0; i < size ; i++) {
            int key = random.nextInt();
            skipList.put(key, 0);
            keys.add(key);
        }

        for (Integer key : keys) {
            Assertions.assertEquals(0, (int) skipList.get(key));
        }
    }

    @Test
    public void removeTest() {
        SkipList<Integer, Integer> skipList = new SkipList<>(0.7);
        skipList.put(100, 0);
        skipList.remove(100);
        skipList.print();
    }

    @Test
    public void removeTest2() {
        System.out.println("---------------removeTest2------------------");
        SkipList<Integer, Integer> skipList = new SkipList<>(0.7);
        for (int i = 0; i < 10; i++) {
            skipList.put(i, 0);
        }
        skipList.print();
        System.out.printf("%n");
        skipList.remove(0);
        skipList.print();
    }
}
