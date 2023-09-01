package cn.atsukoruo.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Random;

public class BinarySearchTreeTester {
    @Test
    public void insertTest() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(10);
    }

    @Test
    public void multipleInsertTest() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        for (int i = 0; i < 10; i++) {
            tree.insert(i);
        }
        tree.print();
    }

    @Test
    public void searchTest() {
        final int COUNT = 10;
        int[] randomNumber = new int[COUNT];
        Random randomGenerator = new Random(System.currentTimeMillis());
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        for (int i = 0; i < COUNT; i++) {
            randomNumber[i] = randomGenerator.nextInt();
            tree.insert(randomNumber[i]);
        }

        for (int i = 0; i < COUNT; i++) {
            Assertions.assertTrue(tree.search(randomNumber[i]));
        }
    }

    @Test
    public void removeTest() {
        System.out.println("-----------------remove Test 1-------------------");
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(10);
        tree.insert(100);
        tree.insert(50);
        tree.insert(121);
        tree.print();
        System.out.printf("%n");
        Assertions.assertTrue(tree.remove(100));
        tree.print();
    }

    @Test
    public void remove2Test() {
        System.out.println("-----------------remove Test 2-------------------");
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(100);
        tree.insert(50);
        tree.insert(200);
        tree.insert(300);
        tree.insert(250);
        tree.insert(350);
        tree.insert(275);
        tree.insert(260);
        tree.insert(280);
        tree.insert(150);
        tree.insert(140);
        tree.insert(143);
        tree.insert(141);
        tree.insert(144);
        tree.print();
        System.out.printf("%n");
        Assertions.assertTrue(tree.remove(100));
        tree.print();
    }

    @Test
    public void remove3Test() {
        System.out.println("-----------------remove Test 3-------------------");
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(100);
        tree.remove(100);
        tree.insert(1);
        tree.print();
    }
}
