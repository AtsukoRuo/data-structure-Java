package cn.atsukoruo.tree;

import cn.atsukoruo.util.Tool;

import java.util.ArrayList;
import java.util.List;

public  class BinarySearchTree<T extends Comparable<T>>
    extends BinaryTree<T> {

    protected BinaryTreeNode<T> hot;      //被命中节点的父亲

    /**
     * 在以指定节点为根的子树中查找指定元素。
     * 该方法会设置hot成员。如果node为空，那么hot也为空。其中hot为命中节点的父节点
     * @param e 待查找的元素
     * @param node 指定搜索子树的根节点
     * @return 若查找成功返回匹配节点，否则返回null
     */
    protected BinaryTreeNode<T> search(BinaryTreeNode<T> node, T e) {
        hot = null;
        while (node != null) {
            hot = node.parent;
            if (node.data.compareTo(e) < 0) {
                node = node.rightChild;
            } else if (node.data.compareTo(e) > 0) {
                node = node.leftChild;
            } else {
                return node;
            }
        }
        return null;
    }

    /**
     * 在树中查找指定元素
     * @param e 待查找的元素
     * @return 若查找成功返回匹配节点，否则返回null
     */
    public BinaryTreeNode<T> search(T e) {
        return search(root, e);
    }

    /**
     * 在树中插入一个元素
     * @param e 待插入的元素
     * @return 返回新插入的节点
     */
    public BinaryTreeNode<T> insert(T e) {
        //在教科书中使用了C++ 指针 + 引用的特性，这可以很方便修改父类的指针，
        //但是在Java中却是不行的，实属遗憾。造成这一问题的实质就是在Java中并没有二级指针！
        if (isEmpty()) {
            root = new BinaryTreeNode<>(e, null);
        }

        BinaryTreeNode<T> x = search(e);
        if (x != null)
            return x;

        x = new BinaryTreeNode<>(e, hot);
        size += 1;
        if (e.compareTo(hot.data) < 0) {
            hot.leftChild = x;
        } else {
            hot.rightChild = x;
        }
        updateHeightAbove(x);
        return x;
    }

    /**
     * 删除数据为e的节点
     * @param e 待删除的元素
     * @return 如果未找到该节点，那么返回false。否则返回true
     */
    public boolean remove(T e) {
        BinaryTreeNode<T> x = search(e);
        if (x == null) {        //该节点并不存在
            return false;
        }
        removeAt(x);            //删除该节点
        size -= 1;
        updateHeightAbove(hot); //更新被删除节点的祖父的高度
        return true;
    }

    /**
     * 删除指定节点，并设置hot为被删除节点的父节点
     * @param x 被删除的节点
     * @return 返回实际被删除节点接替者
     */
    protected BinaryTreeNode<T> removeAt(BinaryTreeNode<T> x) {
        if (x.leftChild == null || x.rightChild == null) {      //处理单边情况
            removeAt1(x);
            hot = x.parent;
            return x.leftChild == null ? x.rightChild : x.leftChild;
        } else {            //双边情况
            BinaryTreeNode<T> succeedNode = x.getSucceedNode();
            Tool.swap(succeedNode.data, x.data);
            removeAt1(succeedNode);
            hot = succeedNode.parent;
            return succeedNode;
        }
    }


    /**
     * 删除指定节点，该节点至多只有一个孩子
     * @param x 被删除的节点
     */
    private void removeAt1(BinaryTreeNode<T> x) {
        BinaryTreeNode<T> child = x.leftChild == null ? x.rightChild : x.leftChild;
        if (x.parent == null) {
            root = child;
        } else if (BinaryTreeNode.isLeftChild(x)) {
            x.parent.leftChild = child;
        } else {
            x.parent.rightChild = child;
        }

        /* 感谢GPT帮我优化下述代码
        if (x.leftChild == null) {
            if (x.parent == null) {
                root = x.rightChild;
                return;
            } else {
                if (BinaryTreeNode.isLeftChild(x)) {
                    x.parent.leftChild = x.rightChild;
                } else {
                    x.parent.rightChild = x.rightChild;
                }
            }

        } else {
            if (x.parent == null) {
                root = x.leftChild;
                return;
            } else {
                if (BinaryTreeNode.isLeftChild(x)) {
                    x.parent.leftChild = x.leftChild;
                } else {
                    x.parent.rightChild = x.leftChild;
                }
            }
        }
         */
    }

    /**
     * 给定七个节点，并将这七个节点组装成一颗树, 同时会修改a、b、c三个节点的高度
     *                  b
     *                /   \
     *               a     c
     *             /  \   /  \
     *            T0  T1 T2  T3
     * @return 返回这个树的根节点
     */
    protected  BinaryTreeNode<T> connect34(
        BinaryTreeNode<T> a, BinaryTreeNode<T> b, BinaryTreeNode<T> c,
        BinaryTreeNode<T> T0, BinaryTreeNode<T> T1, BinaryTreeNode<T> T2, BinaryTreeNode<T> T3
    ) {
        a.leftChild = T0;
        if (T0 != null) T0.parent = a;
        a.rightChild = T1;
        if (T1 != null) T1.parent = a;
        updateHeight(a);

        c.leftChild = T2;
        if (T2 != null) T2.parent = c;
        c.rightChild = T3;
        if (T3 != null) T3.parent = c;
        updateHeight(c);

        b.leftChild = a;
        a.parent = b;
        b.rightChild = c;
        c.parent = b;
        updateHeight(b);

        return b;
    }

    /**
     * 对节点v实施zig-zig、或zig-zag旋转
     * @param v 待旋转的节点
     * @return 旋转后的等价二叉子树的根节点
     */
    protected  BinaryTreeNode<T> rotateAt(BinaryTreeNode<T> v) {
        BinaryTreeNode<T> p = v.parent;
        BinaryTreeNode<T> g = v.parent;

        if (BinaryTreeNode.isLeftChild(p)) {
            if (BinaryTreeNode.isLeftChild(v)) {
                p.parent = g.parent;
                return connect34(v, p, g, v.leftChild, v.rightChild, p.rightChild, g.rightChild);
            } else {
                v.parent = g.parent;
                return connect34(p, v, g, p.leftChild, v.leftChild, v.rightChild, g.rightChild);
            }
        } else {
            if (BinaryTreeNode.isRightChild(v)) {
                p.parent = g.parent;
                return connect34(g,p,v,g.leftChild,p.leftChild,v.leftChild,v.rightChild);
            } else {
                v.parent = g.parent;
                return connect34(g,v,p,g.leftChild,v.leftChild,v.rightChild,p.rightChild);
            }
        }
    }
}
