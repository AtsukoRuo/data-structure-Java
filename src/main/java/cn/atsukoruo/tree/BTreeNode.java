package cn.atsukoruo.tree;

import cn.atsukoruo.list.Vector;


public class BTreeNode<T extends Comparable<T>> {

    BTreeNode<T> parent;
    Vector<BTreeNode<T>> children;              //分支    第i个分支，对应第i、i-1个关键码
    Vector<T> keys;                             //关键码， 第i个关键码对应第i、i+1个分支

    /**
     * 初始化时，分支与关键码个数都是0。
     */
    BTreeNode() {
        parent = null;
        children = new Vector<>();
        keys = new Vector<>();
    }

    public void print() {
        System.out.printf("%s: {%n    key: ", super.toString());
        for (T data : keys) {
            System.out.printf("%s, ", data);
        }
        System.out.printf("%n    children: ");
        for (BTreeNode temp : children) {
            if (temp == null) continue;
            System.out.printf("%s, ", temp.toString());
        }
        System.out.printf("%n} %n");
    }
}
