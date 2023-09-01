package cn.atsukoruo.tree;

import org.junit.jupiter.api.Test;

public class AVLTester {
    @Test
    public void insertTest() {
        AVL<Integer> tree = new AVL<>();
        int[] data = new int[] {
            100, 200, 300, 250, 350, 325,
            270, 280, 281, 282, 283, 284,
            285, 286, 287, 288, 289, 290,
            291, 292, 293, 294
        };

        for (int i : data) {
            tree.insert(i);
        }
        tree.print();
    }

    @Test
    public void removeTest() {
        AVL<Integer> tree = new AVL<>();
        int[] data = new int[] {
                100, 200, 300, 250, 350, 325,
                270, 280, 281, 282, 283, 284,
                285, 286, 287, 288, 289, 290,
                291, 292, 293, 294
        };

        for (int i : data) {
            tree.insert(i);
        }
        tree.remove(325);
        tree.remove(288);
        tree.remove(290);
        tree.remove(289);
        tree.remove(292);
        tree.remove(291);
        tree.remove(294);
        tree.print();
        System.out.printf("%n");
    }
}
