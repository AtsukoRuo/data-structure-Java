package cn.atsukoruo.tree;

public class RBTree<T extends Comparable<T>> extends BinarySearchTree<T> {
    static <T extends Object & Comparable<T>>
    boolean whetherUpdateBlackHeight(BinaryTreeNode<T> node) {
        //需要更新高度的情况有三种
        //1.左右孩子的黑高不相等
        //2.作为红节点，黑高度与孩子不相等
        //3.作为黑节点，黑高度不等于孩子黑高 + 1
        int leftHeight = node.leftChild == null ? -1 : node.leftChild.height;
        int rightHeight = node.rightChild == null ? -1 : node.rightChild.height;
        return !(leftHeight == rightHeight
                && node.height == leftHeight +  (BinaryTreeNode.isRed(node) ? 0 : 1));
    }

    @Override
    protected int updateHeight( BinaryTreeNode<T> node) {
        node.height = Math.max(
                node.leftChild == null ? -1 :  node.leftChild.height,
                node.rightChild == null ? -1 : node.rightChild.height
        );
        return BinaryTreeNode.isBlack(node) ? node.height++ : node.height;
    }

    @Override
    public BinaryTreeNode<T> insert(T e) {
        if (isEmpty()) {
            size = 1;
            return root = new BinaryTreeNode<>(e, null, null, null, 0, RBColor.BLACK);
        }

        BinaryTreeNode<T> x = search(root, e);
        if (x != null)          //这里约定在二叉搜索树中没有重复元素
            return x;

        x = new BinaryTreeNode<>(e, hot, null, null, -1, RBColor.RED);
        if (e.compareTo(hot.data) < 0) {
            hot.leftChild = x;
        } else {
            hot.rightChild = x;
        }
        size += 1;
        solveDoubleRed(x);
        return x;
    }


    void solveDoubleRed(BinaryTreeNode<T> x) {
        //x必为红节点

        if (BinaryTreeNode.isRoot(x)) {         //递归到根节点，全树的黑高 + 1
            x.color = RBColor.BLACK;
            x.height++;
            return;
        }

        BinaryTreeNode<T> p = x.parent;         //x不为根节点，其父亲必然存在
        if (BinaryTreeNode.isBlack(p)) {        //此时没有双红缺陷现象，进行双红修复
            return;
        }

        BinaryTreeNode<T> g = p.parent;         //此时p为红色，那么其父亲一定存在
        BinaryTreeNode<T> u = BinaryTreeNode.getUncle(x);
        if (BinaryTreeNode.isBlack(u)) {        //叔叔为黑节点，对应情况一
            BinaryTreeNode<T> gg = g.parent;
            BinaryTreeNode<T> r;
            if (!BinaryTreeNode.isRoot(p) ?
                    BinaryTreeNode.isLeftChild(p) == BinaryTreeNode.isLeftChild(x)
                    : BinaryTreeNode.isLeftChild(x)) {       //p x同侧
                if (BinaryTreeNode.isLeftChild(x)) {            //zig旋转
                    attachAsLeftChild(g, p.rightChild);
                    attachAsRightChild(p, g);
                } else {                                        //zag旋转
                    attachAsRightChild(g, p.leftChild);
                    attachAsLeftChild(p, g);
                }
                r = p;
                p.color = RBColor.BLACK;
                g.color = RBColor.RED;
                p.height += 1;
                g.height -= 1;
            } else {                            //p x异侧
                r = rotateAt(x);
                x.color = RBColor.BLACK;
                g.color = RBColor.RED;
                g.height -= 1;
                x.height += 1;
            }
            updateHeight(x);
            r.parent = gg;
            if (gg == null) {
                root = r;
            } else {
                if (gg.data.compareTo(r.data) > 0) {
                    gg.leftChild = r;
                } else {
                    gg.rightChild = r;
                }
            }
        } else {                                //对应情况二
            p.color = u.color = RBColor.BLACK;
            g.color = RBColor.RED;
            p.height += 1;
            u.height += 1;
            solveDoubleRed(g);
        }
    }
    @Override
    public boolean remove(T e) {
        BinaryTreeNode<T> x = search(root, e);
        if (x == null)          //目标不存在
            return false;
        BinaryTreeNode<T> r = removeAt(x);
        if ((--size) == 0)          //空树，直接结束
            return true;

        //现在至少有一个元素
        if (hot == null) {      //若实际上删除的是根节点
            root.color = RBColor.BLACK;
            updateHeight(root);
            return true;
        }

        if (!whetherUpdateBlackHeight(hot)) {       //也就是说，实际被删除的节点对黑高无影响
            return true;
        }

        if (BinaryTreeNode.isRed(r)) {
            //此时r为红节点，那么它是内部节点，非null
            //重新染色就可以修正
            r.color = RBColor.BLACK;
            r.height++;
            return true;
        }

        solveDoubleBlack(r);
        return true;
    }
    void solveDoubleBlack(BinaryTreeNode<T> x) {
        BinaryTreeNode<T> p = x == null ? hot : x.parent;       //节点r的父亲
        if (p == null) {
            return;
        }
        BinaryTreeNode<T> s = (x == p.leftChild) ? p.rightChild : p.leftChild;
        if (BinaryTreeNode.isBlack(s)) {
            BinaryTreeNode<T> t = null;
            if (BinaryTreeNode.isRed(s.leftChild)) t = s.leftChild;
            else if (BinaryTreeNode.isRed(s.rightChild)) t = s.rightChild;
            if (t != null) {        //BB-1情况
                BinaryTreeNode<T> g = p.parent;
                if (BinaryTreeNode.isLeftChild(t) == BinaryTreeNode.isLeftChild(s)) {       //同侧
                    if (BinaryTreeNode.isLeftChild(t)) {
                        attachAsLeftChild(p, s.rightChild);
                        attachAsRightChild(s, p);
                    } else {
                        attachAsRightChild(p, s.leftChild);
                        attachAsLeftChild(s, p);
                    }
                    s.color = p.color;
                    p.color = RBColor.BLACK;
                    t.color = RBColor.BLACK;
                    s.parent = g;
                    if (g == null) {
                        root = s;
                    } else {
                        if (g.data.compareTo(s.data) > 0) {
                            g.leftChild = s;
                        } else {
                            g.rightChild = s;
                        }
                    }
                    updateHeight(t);
                    updateHeight(p);
                    updateHeight(s);
                } else {        //异侧

                    rotateAt(t);
                    t.parent = g;
                    if (g == null) {
                        root = t;
                    } else {
                        if (g.data.compareTo(t.data) > 0) {
                            g.leftChild = t;
                        } else {
                            g.rightChild = t;
                        }
                    }
                    t.color = p.color;
                    p.color = RBColor.BLACK;
                    updateHeight(p);
                    updateHeight(t);
                }
                return;
            } else {
                if (BinaryTreeNode.isRed(p)) {          //BB-2-R情况
                    p.color = RBColor.BLACK;
                    s.color = RBColor.RED;
                    s.height -= 1;
                    return;
                } else {                                //BB-2-B情况
                    s.color = RBColor.RED;
                    p.height -= 1;
                    s.height -= 1;
                    solveDoubleBlack(p);
                }
            }
        } else {                    //BB-3情况
            BinaryTreeNode<T> g = p.parent;
            if (BinaryTreeNode.isLeftChild(s)) {
                attachAsLeftChild(p, s.rightChild);
                attachAsRightChild(s, p);
            } else {
                attachAsRightChild(p, s.leftChild);
                attachAsLeftChild(s, p);
            }
            s.color = RBColor.BLACK;
            p.color = RBColor.RED;
            s.parent = g;
            if (g == null) {
                root = s;
            } else {
                if (g.data.compareTo(s.data) > 0) {
                    g.leftChild = s;
                } else {
                    g.rightChild = s;
                }
            }
            updateHeight(p);
            updateHeight(s);
            solveDoubleBlack(x);
        }
    }
}
