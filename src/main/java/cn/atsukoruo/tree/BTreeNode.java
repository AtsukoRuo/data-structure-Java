package cn.atsukoruo.tree;

import cn.atsukoruo.list.Vector;


public class BTreeNode<T extends Comparable<T>> {

    BTreeNode<T> parent;
    Vector<BTreeNode<T>> children;
    Vector<T> keys;

    BTreeNode() {
        parent = null;
        children.insert(null, 0);
        //分支总比关键码多一，这对于空节点来说也成立
    }

    BTreeNode(T element) {
        this(element, null, null);
    }

    BTreeNode(T element, BTreeNode<T> child0, BTreeNode<T> child1) {
        parent = null;
        children.insert(child0, 0);
        children.insert(child1, 1);
        keys.insert(element, 0);
        if (child0 != null) child0.parent = this;
        if (child1 != null) child1.parent = this;
    }

}
