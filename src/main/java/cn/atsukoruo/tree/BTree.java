package cn.atsukoruo.tree;

import cn.atsukoruo.list.Vector;

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

    }

    /**
     * 处理下溢情况
     * @param t
     */
    private void solveUnderflow(BTreeNode<T> t) {

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
        return false;
    }

}
