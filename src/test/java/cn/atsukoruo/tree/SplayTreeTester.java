package cn.atsukoruo.tree;

import org.junit.jupiter.api.Test;

public class SplayTreeTester {
    @Test
    public void insertTester() {
        System.out.println("-------------insertTester1-------------");
        SplayTree<Integer> tree = new SplayTree<>();
        tree.insert(1);
        tree.insert(2);
        tree.insert(3);
        tree.insert(4);
        tree.insert(5);
        tree.insert(6);
        tree.insert(7);
        tree.search(1);
        tree.print();
    }

    @Test
    public void insert2Tester() {
        System.out.println("-------------insertTester2-------------");
        SplayTree<Integer> tree = new SplayTree<>();
        int[] data = new int[] {1, 3, 4, 5, 6, 7, 8, 9, 0, 2};
        for (int i : data) {
            tree.insert(i);
        }
        tree.print();
    }

    @Test
    public void removeTester() {
        System.out.println("-------------removeTester2-------------");
        SplayTree<Integer> tree = new SplayTree<>();
        tree.insert(1);
        tree.insert(2);
        tree.insert(3);
        tree.insert(4);
        tree.insert(5);
        tree.insert(6);
        tree.insert(7);
        tree.remove(1);
        tree.print();
    }
}
