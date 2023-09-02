package cn.atsukoruo.tree;

import cn.atsukoruo.list.Queue;
import cn.atsukoruo.list.Vector;
import cn.atsukoruo.util.Tool;

final public class BTree<T extends Comparable<T>> {
    final int order;      //阶数
    BTreeNode<T> root;
    BTreeNode<T> hot;       //命中节点的父节点

    /**
     * 处理上溢情况
     * @param t 上溢节点
     */
    private void solveOverflow(BTreeNode<T> t) {
        if (t.children.size() <= order) //递归基
            return;

        final int s = order / 2;       //选取中轴点

        // v作为分裂后的右孩子
        // 上溢节点t作为左孩子
        BTreeNode<T> v = new BTreeNode<>();

        // 将[s + 1, order - 1]处的关键码以及对应的左孩子复制到节点v中
        // 同时将节点t中的[s + 1, order - 1]的关键码以及对应的孩子删除掉。
        // 那么循环结束时，节点t作为分裂后的左孩子
        for (int j = 0; j < order - s - 1; j++) {
            //调用remove效率低下，以后有时间再优化吧
            v.children.insert(t.children.remove(s + 1));
            v.keys.insert(t.keys.remove(s + 1));
        }

        // 分支比关键码多一
        v.children.insert(t.children.remove(s + 1));

        if (v.children.get(0) != null) {        //如果孩子不为空
            for (BTreeNode<T> node : v.children) {
                node.parent = v;    //让这些孩子指向新的父亲——节点v
            }
        }

        BTreeNode<T> p = t.parent;
        if (p == null) {        //这种对应情况c，是一种特殊情况，此时全树的高度会增加1
            root = p = new BTreeNode<>();
            p.children.insert(t);
            t.parent = p;
        }

        int r = Vector.search(p.keys, t.keys.get(0));
        p.keys.insert(t.keys.remove(s),r + 1);
        p.children.insert(v, r + 2);
        v.parent = p;
        solveOverflow(p);
    }

    /**
     * 处理下溢情况
     * @param v 下溢的节点
     */
    private void solveUnderflow(BTreeNode<T> v) {
        if (v.children.size() >= (order + 1) / 2) {     //递归基
            //这里[(order + 1) / 2]就是(order / 2)向上取整
            return;
        }
        BTreeNode<T> p = v.parent;
        if (p == null) {
            //下溢传播到根节点处
            if (v.keys.size() == 0) {
                root = v.children.get(0);
                root.parent = null;
            }
            return;
        }

        //通过分支寻找孩子
        int rank = p.children.find(v);
        if (rank != 0) {                                //有左孩子
            BTreeNode<T> leftNode = p.children.get(rank - 1);
            if (p.keys.size() >= (order + 1) / 2) {     //左孩子可以借出一个关键码
                //这里不得不感叹C++重载运算符是多么的方便
                //这是教科书上的代码，技巧性太强，可读性差。
                v.keys.insert(p.keys.get(rank - 1), 0);
                p.keys.set(leftNode.keys.remove(leftNode.keys.size() - 1), rank - 1);
                v.children.insert(leftNode.children.remove(leftNode.children.size() - 1),0);
                BTreeNode<T> child = v.children.get(0);
                if (child != null)
                    child.parent = v;
                return;
            }
        }
        if (rank != p.children.size() - 1) {            //有右孩子
            BTreeNode<T> rightNode = p.children.get(rank + 1);
            if (p.keys.size() >= (order + 1) / 2) {     //右孩子可以借出一个关键码
                //这是自己的实现方式，比较直观，可读性比教科书上的好。性能上，与教科书上的代码是相同的，毕竟汇编代码都一样。
                T x = rightNode.keys.remove(0);
                BTreeNode<T> child = rightNode.children.remove(0);

                T y = p.keys.get(rank);
                p.keys.set(x, rank);

                v.keys.insert(y);
                v.children.insert(child);

                if (child != null)
                    child.parent = v;
                return;
            }
        }
        if (rank != 0) {        //左右兄弟节点（如果有的话）都不能借出
            //一定有左孩子
            BTreeNode<T> leftNode = p.children.get(rank - 1);

            T y = p.keys.remove(rank - 1);
            p.children.remove(rank);

            leftNode.keys.insert(y);
            while (v.keys.size() != 0) {
                leftNode.keys.insert(v.keys.remove(0));
            }

            while (v.children.size() != 0) {
                BTreeNode child = v.children.remove(0);
                if (child != null)
                    child.parent = leftNode;
                leftNode.children.insert(child);
            }
        } else {
            //一定有右孩子
            BTreeNode<T> rightNode = p.children.get(rank + 1);
            T y = p.keys.remove(rank);
            p.children.remove(rank);

            rightNode.keys.insert(y, 0);
            while (v.keys.size() != 0) {
                rightNode.keys.insert(v.keys.remove(v.keys.size() - 1),0);
            }

            while (v.children.size() != 0) {
                BTreeNode<T> child = v.children.remove(v.children.size() - 1);
                if (child != null)
                    child.parent = rightNode;
                rightNode.children.insert(child ,0);
            }
        }
        solveUnderflow(p);
    }

    BTree(int order) {
        this.order = order;
    }

    int order() { return order; }
    BTreeNode<T> root() { return root; }
    boolean isEmpty() { return root == null ? true : false; }


    public boolean search(T element) {
        return search(root, element) != null;
    }

    /**
     * 查找指定关键码所在的节点
     * @param node 子树的根节点
     * @param element 要查找的数据
     * @return 若未查找到返回null，否则返回关键码所在的节点
     */
    BTreeNode<T> search(BTreeNode<T> node, T element) {
        BTreeNode<T> v = node;
        hot = null;
        while (v != null) {
            hot = v;
            int rank = Vector.search(v.keys, element);
            if (0 <= rank && element.compareTo(v.keys.get(rank)) == 0) {
                break;
            }
            v = v.children.get(rank + 1);
        }
        return v;
    }

    boolean insert(T element) {
        BTreeNode<T> v = search(root, element);
        if (v != null)
            return false;
        if (hot == null) {
            root = new BTreeNode<>();
            root.keys.insert(element);
            root.children.insert(null,0);
            root.children.insert(null, 0);
            return true;
        }
        int rank = Vector.search(hot.keys, element);
        hot.keys.insert(element, rank + 1);
        hot.children.insert(null, rank + 2);        //这里保证了叶子节点的分支全是null
        solveOverflow(hot);
        return true;
    }

    boolean remove(T element) {
        BTreeNode<T> v = search(root, element);
        if (v == null)
            return false;
        int r = Vector.search(v.keys, element);
        if (v.children.get(0) != null) {                //v不是叶节点
            BTreeNode<T> u = v.children.get(r + 1);
            while (u.children.get(0) != null)           //找v中关键码为element的后继
                u = u.children.get(0);
            v.keys.set(u.keys.get(0), r);               //关键码覆盖
            v = u;                                      //将v设置为后继节点
        }
        v.keys.remove(0);
        v.children.remove(0);
        solveUnderflow(v);
        return true;
    }

    public void print() {
        //多叉树的遍历类似于图，广度优先遍历是最容易实现的
        Queue<BTreeNode<T>> queue = new Queue<>();
        queue.enqueue(root);
        while (!queue.empty()) {
            BTreeNode<T> node = queue.dequeue();
            node.print();
            for (BTreeNode temp : node.children) {
                if (temp == null) continue;
                queue.enqueue(temp);
            }
        }
        System.out.printf("%n%n%n");
    }
}
