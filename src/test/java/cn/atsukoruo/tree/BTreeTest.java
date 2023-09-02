package cn.atsukoruo.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class BTreeTest {
    @Test
    public void insert() {
        BTree<Integer> tree = new BTree<>(3);
        int[] data = new int[] {
            19, 36, 41, 51, 53, 77, 89,
                75, 79, 84, 97
        };
        for (int temp : data) {
            tree.insert(temp);
        }
        tree.print();

        data = new int[] {
            23, 29, 45, 87, 20, 21
        };
        for (int temp : data) {
            tree.insert(temp);
        }
        tree.print();
    }

    @Test
    public void searchTest() {
        final int COUNT = 10000;
        int[] data = new int[COUNT];
        BTree<Integer> tree = new BTree<>(5);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < COUNT; i++) {
            data[i] = random.nextInt();
            tree.insert(data[i]);
        }

        for (int i = 0; i < COUNT; i++) {
            Assertions.assertTrue(tree.search(data[i]));
        }
    }

    @Test
    public void removeTest() {
        BTree<Integer> tree = new BTree<>(3);
        int[] data = new int[] {
                19, 36, 41, 51, 53, 77, 89,
                75, 79, 84, 97, 23, 29, 45, 87, 20, 21
        };
        for (int temp : data) {
            tree.insert(temp);
        }
        tree.remove(21);
        tree.print();
    }
}
