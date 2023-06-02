package cn.atsukoruo.tree;

import cn.atsukoruo.list.Vector;
import cn.atsukoruo.util.Tool;

final public class BTree<T extends Comparable<T>> {
    int size;       //关键码总数
    final int order;      //阶数
    BTreeNode<T> root;
    BTreeNode<T> hot;       //命中节点的父节点

    /**
     * 处理上溢情况
     * @param t
     */
    private void solveOverflow(BTreeNode<T> t) {
        if (order >= t.children.size()) //递归基
            return;

        int s = order / 2;       //选取中轴点

        // v作为分裂后的右孩子
        // 注意有一个空孩子，会在之后替换掉它
        BTreeNode<T> v = new BTreeNode<>();


        // 将[s + 1, order - 1]处的关键码以及对应的左孩子复制到节点v中
        // 同时将节点t中的[s + 1, order - 1]的关键码以及对应的孩子删除掉。
        // 那么循环结束时，节点t作为分裂后的左孩子
        for (int j = 0; j < order - s - 1; j++) {
            v.children.insert(t.children.remove(s + 1), j);
            v.keys.insert(t.keys.remove(s + 1), j);
        }

        // 孩子比关键码多一
        // 之前有个冗余的空孩子，在此处替换
        v.children.set(v.children.remove(s + 1), order - s - 1);

        if (v.children.get(0) != null) {        //如果孩子不为空
            for (int i = 0; i < v.children.size(); i++) {
                v.children.get(i).parent = v;   //让这些孩子指向新的父亲——节点v
            }
        }

        BTreeNode<T> p = t.parent;
        if (p == null) {        //这种对应情况c，是一种特殊情况，此时全树的高度会增加1
            p = new BTreeNode<>();
            p.children.set(v, 0);       //p初始化时有个空孩子，在此处替换掉它
            v.parent = p;
            size += 1;
        }

        //p[r] <= t中所有的关键码 < p[r + 1]
        int r = Vector.search(v.keys, t.keys.get(0));
        p.keys.insert(t.keys.remove(s),r + 1);
        p.children.insert(v, r + 2);
        v.parent = p;
        solveOverflow(p);

    }

    /**
     * 处理下溢情况
     * @param t
     */
    private void solveUnderflow(BTreeNode<T> v) {
        if ((order + 1) / 2 <= v.children.size())
            return;     //递归基，当前节点并未发生下溢
        BTreeNode<T> p = v.parent;
        if (p == null) {            //到达根节点
            if (v.keys.size() != 0 && v.children.get(0) != null) {

            }
            return;
        }
    }

    BTree(int order) {
        this.order = order;
        size = 0;
        //这可以处理向空树中插入一个元素的退化情况
        //这里孩子设置为null是合理的。因为null相当于外部节点
        root = new BTreeNode<>();
    }

    int order() { return order; }
    int size() { return size; }
    BTreeNode<T> root() { return root; }
    boolean isEmpty() { return root == null ? true : false; }


    BTreeNode<T> search(T element) {
        BTreeNode<T> v = root;
        hot = null;
        while (v != null) {
            int rank = Vector.search(v.keys, element);
            if (0 <= rank
                && element.compareTo(v.keys.get(rank)) == 0) {
                return v;
            }
            hot = v;


            //此时keys[rank] <= element < keys[rank + 1]
            //那么指向keys[rank]右孩子，也就是keys[rank + 1]左孩子。
            //这对rank = 0 | rank = size - 1的边界情况也成立
            v = v.children.get(rank + 1);
        }
        return null;
    }

    boolean insert(T element) {
        BTreeNode<T> v = search(element);
        if (v != null)
            return false;
        int rank = Vector.search(hot.keys, element);
        hot.keys.insert(element, rank + 1);
        hot.children.insert(null, rank + 2);
        size++;
        solveOverflow(hot);
        return true;
    }

    boolean remove(T element) {
        BTreeNode<T> v = search(element);
        if (v == null)
            return false;
        int r = Vector.search(v.keys, element);
        if (v.children.get(0) != null) {        //v不是叶节点
            BTreeNode<T> u = v.children.get(r + 1);
            while (u.children.get(0) != null)       //找v中关键码为element的后继
                u = u.children.get(0);
            v.keys.set(u.keys.get(0), r);
            v = u;
        }
        v.keys.remove(0);
        v.children.remove(0);       //此时v的孩子都为null，删哪个都可以
        size -= 1;
        solveUnderflow(v);
        return true;
    }

}
