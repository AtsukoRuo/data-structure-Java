package cn.atsukoruo.tree;

import cn.atsukoruo.tree.BinaryTree;
import cn.atsukoruo.tree.BinaryTreeNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

public class BinaryTreeTester {
    BinaryTree<Character> tree = new BinaryTree<>();
    BinaryTreeNode<Character> i;
    BinaryTreeNode<Character> d;
    BinaryTreeNode<Character> l;
    BinaryTreeNode<Character> c;
    BinaryTreeNode<Character> h;
    BinaryTreeNode<Character> a;
    BinaryTreeNode<Character> b;
    BinaryTreeNode<Character> f;
    BinaryTreeNode<Character> e;
    BinaryTreeNode<Character> g;
    BinaryTreeNode<Character> k;
    BinaryTreeNode<Character> j;
    BinaryTreeNode<Character> n;
    BinaryTreeNode<Character> m;
    BinaryTreeNode<Character> p;
    BinaryTreeNode<Character> o;

    @BeforeEach
    public void init() {
        i = tree.insertAsRoot('i');
        d = tree.insertAsLeft(i, 'd');
        l = tree.insertAsRight(i, 'l');
        c = tree.insertAsLeft(d, 'c');
        h = tree.insertAsRight(d, 'h');
        a = tree.insertAsLeft(c, 'a');
        b = tree.insertAsRight(a, 'b');
        f = tree.insertAsLeft(h, 'f');
        e = tree.insertAsLeft(f, 'e');
        g = tree.insertAsRight(f, 'g');
        k = tree.insertAsLeft(l, 'k');
        j = tree.insertAsLeft(k, 'j');
        n = tree.insertAsRight(l, 'n');
        m = tree.insertAsLeft(n, 'm');
        p = tree.insertAsRight(n, 'p');
        o = tree.insertAsLeft(p, 'o');
    }
    Consumer<Character> consumer = (data) -> {
        System.out.print(data + " -> ");
    };

    @Test
    public void travelPreTest() {
        System.out.println("----------Travel Pre Test----------");
        tree.travelPre(consumer);
        System.out.print("\n");
        BinaryTreeNode.travelPreIteration(tree.getRoot(), consumer);
        System.out.print("\n");
        BinaryTreeNode.travelPreIteration1(tree.getRoot(), consumer);
        System.out.print("\n");
    }

    @Test
    public void travelInTest() {
        System.out.println("----------Travel In  Test----------");
        tree.travelIn(consumer);
        System.out.print("\n");
        BinaryTreeNode.travelInIteration(tree.getRoot(), consumer);
        System.out.print("\n");


    }
    @Test
    public void travelPostTest() {
        System.out.println("----------Travel Post Test----------");
        tree.travelPost(consumer);
        System.out.print("\n");
        BinaryTreeNode.travelPostIteration(tree.getRoot(), consumer);
        System.out.print("\n");
    }

    @Test
    public void sizeTest() {
        Assertions.assertEquals(16, tree.size);
        Assertions.assertEquals(3, tree.size(c));
        Assertions.assertEquals(7, tree.size(l));
    }

    @Test
    public void printTest() {
        System.out.println("----------print Test----------");
        tree.print();
    }

    @Test
    public void heightTest() {
        Assertions.assertEquals(0, o.height);
        Assertions.assertEquals(0, m.height);
        Assertions.assertEquals(0, j.height);
        Assertions.assertEquals(0, g.height);
        Assertions.assertEquals(0, e.height);
        Assertions.assertEquals(0, b.height);
        Assertions.assertEquals(1, p.height);
        Assertions.assertEquals(1, k.height);
        Assertions.assertEquals(1, f.height);
        Assertions.assertEquals(1, a.height);
        Assertions.assertEquals(2, c.height);
        Assertions.assertEquals(2, h.height);
        Assertions.assertEquals(2, n.height);
        Assertions.assertEquals(3, d.height);
        Assertions.assertEquals(3, l.height);
        Assertions.assertEquals(4, i.height);
    }

    @Test
    public void removeTest() {
        tree.remove(d);
        tree.print();
        Assertions.assertEquals(8, tree.size);
        tree.remove(n);
        tree.remove(k);
        tree.print();
        Assertions.assertEquals(1, tree.root.height);
    }

    @Test
    public void copyTest() {
        System.out.println("----------copy Test----------");
        BinaryTree<Character> newTree = new BinaryTree<>(tree);
        newTree.print();
        System.out.println("------------------------------");
        newTree.remove(newTree.root.leftChild);
        newTree.print();
        System.out.println("------------------------------");
        tree.print();
    }

}
