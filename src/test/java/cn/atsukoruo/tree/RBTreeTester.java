package cn.atsukoruo.tree;

import cn.atsukoruo.list.Vector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;


public class RBTreeTester {
    @Test
    public void insertTest() {
        RBTree<Integer> tree = new RBTree<>();
        int[] data = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int e : data) {
            tree.insert(e);
        }
        tree.print();
    }

    @Test
    public void insertTest2() {
        RBTree<Integer> tree = new RBTree<>();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 20; i++) {
            tree.insert(random.nextInt(100));
        }
        tree.print();
    }
    @Test
    public void insertTest3() {
        RBTree<Integer> tree = new RBTree<>();
        Random random = new Random(System.currentTimeMillis());
        final int COUNT = 100_0000;
        for (int i = 0; i < COUNT; i++) {
            tree.insert(random.nextInt());
        }
        tree.travelLevel((node) -> {
            Assertions.assertTrue(!RBTree.whetherUpdateBlackHeight(node));
        });
    }

    @Test
    public void removeTest() {
        RBTree<Integer> tree = new RBTree<>();
        tree.insert(1);
        tree.remove(1);
        tree.insert(1);
        tree.print();
    }

    @Test
    public void remove2Test() {
        RBTree<Integer> tree = new RBTree<>();
        final int COUNT = 1_0000;
        Random random = new Random(System.currentTimeMillis());
        Vector<Integer> vector = new Vector<>();
        for (int i = 0; i < COUNT; i++) {
            int data = random.nextInt(Integer.MAX_VALUE);
            tree.insert(data);
            if (i % 11 == 0) {
                vector.insert(data);
            }
        }
        vector.unsort();
        for (int i : vector) {
            tree.remove(i);
        }

//        for (int i : vector) {
//            Assertions.assertTrue(!tree.search(i));
//        }
        tree.travelLevel((node) -> {
            int leftHeight = node.leftChild == null ? -1 : node.leftChild.height;
            int rightHeight = node.rightChild == null ? -1 : node.rightChild.height;
            System.out.printf("{%d %d}%n",leftHeight, rightHeight);
            Assertions.assertTrue(!RBTree.whetherUpdateBlackHeight(node));
        });
    }
}
