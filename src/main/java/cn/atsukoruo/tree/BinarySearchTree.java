package cn.atsukoruo.tree;

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
        int result = 0;
        while (node != null
            && (result = node.data.compareTo(e)) != 0) {
            hot = node;
            node = result < 0 ? node.rightChild : node.leftChild;
        }
        return node;
    }

    /**
     * 在树中查找指定元素
     * @param e 待查找的元素
     * @return 若查找成功返回匹配节点，否则返回null
     */
    public boolean search(T e) {
        return search(root, e) != null;
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
            size = 1;
            return root = new BinaryTreeNode<>(e, null);
        }

        BinaryTreeNode<T> x = search(root, e);
        if (x != null)          //这里约定在二叉搜索树中没有重复元素
            return x;

        x = new BinaryTreeNode<>(e, hot);
        if (e.compareTo(hot.data) < 0) {
            hot.leftChild = x;
        } else {
            hot.rightChild = x;
        }
        size += 1;
        updateHeightAbove(hot);
        return x;
    }

    /**
     * 删除数据为e的节点
     * @param e 待删除的元素
     * @return 如果未找到该节点，那么返回false。否则返回true
     */
    public boolean remove(T e) {
        BinaryTreeNode<T> x = search(root, e);
        if (x == null) {        //该节点并不存在
            return false;
        }
        removeAt(x);            //删除该节点
        size -= 1;
        updateHeightAbove(hot); //更新被删除节点的祖父的高度
        return true;
    }

    /**
     * 删除指定节点，并设置hot为实际上被删除节点的父节点
     * 它不会更新size、但是会正确设置root、以及父节点的孩子
     * @param x 要被被删除的节点
     * @return 返回被实际上被删除节点接替者（不是前驱与后继）
     */
    protected BinaryTreeNode<T> removeAt(BinaryTreeNode<T> x) {
        if (x.leftChild == null || x.rightChild == null) {      //处理单边情况
            hot = x.parent;
            return removeAt1(x);
        } else {            //双边情况
            //这里getPrevNode以及getSucceedNode都是可以的，使用Prev是因为可视化网站使用的是前驱
            BinaryTreeNode<T> temp = x.getPrevNode();
            hot = temp.parent;
            x.data = temp.data;
            BinaryTreeNode<T> succeedNode = removeAt1(temp);
            return succeedNode;
        }
    }


    /**
     * 删除指定节点
     * @param x 被删除的节点,要求该节点至多只有一个孩子
     * @return 返回接替者（被删除节点的左孩子或者右孩子），其父亲已经设置完成
     */
    private BinaryTreeNode<T> removeAt1(BinaryTreeNode<T> x) {
        BinaryTreeNode<T> child = x.leftChild == null ? x.rightChild : x.leftChild;
        if (child != null) {
            child.parent = x.parent;
        }
        if (x.parent == null) {
            root = child;
        } else if (BinaryTreeNode.isLeftChild(x)) {
            x.parent.leftChild = child;
        } else {
            x.parent.rightChild = child;
        }
        return child;
    }

    /**
     * 给定七个节点，并将这七个节点组装成一颗树, 同时会修改a、b、c三个节点的高度
     *                  b
     *                /   \
     *               a     c
     *             /  \   /  \
     *            T0  T1 T2  T3
     * @return 返回这个树的根节点, 调用者必须重新设置根节点的父亲的孩子情况
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
     * 视情况对节点v实施zig-zig、zig-zag、zag-zag、zag-zig旋转, 该方法会正确修正子树的高度
     * @param v 待旋转的节点, 确保其父亲以及祖父存在
     * @return 旋转后的等价二叉子树的根节点，此时调用者必须重新设置根节点的父亲的孩子情况
     */
    protected  BinaryTreeNode<T> rotateAt(BinaryTreeNode<T> v) {
        BinaryTreeNode<T> p = v.parent;
        BinaryTreeNode<T> g = p.parent;
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
    /**
     * 节点q作为p的左孩子插入
     */
    void attachAsLeftChild(BinaryTreeNode<T> p, BinaryTreeNode<T> q) {
        p.leftChild = q;
        if (q != null) q.parent = p;
    }

    /**
     * 节点q作为p的右孩子插入
     */
    void attachAsRightChild(BinaryTreeNode<T> p, BinaryTreeNode<T> q) {
        p.rightChild = q;
        if (q != null) q.parent = p;
    }
}
