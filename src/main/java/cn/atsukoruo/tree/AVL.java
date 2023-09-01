package cn.atsukoruo.tree;

public class AVL<T extends Comparable<T>>
    extends BinarySearchTree<T> {

    /**
     * @param node 指定节点
     * @return 获取指定节点中高度最高的孩子。若node为空，那么返回null。若孩子高度相等，优先返回左孩子
     */
    BinaryTreeNode<T> getTallerChild(BinaryTreeNode<T> node) {
        if (node == null)
            return null;
        int leftHeight = node.leftChild == null ? -1 : node.leftChild.height;
        int rightHeight = node.rightChild == null ? -1 : node.rightChild.height;

        return  (leftHeight < rightHeight) ? node.rightChild : node.leftChild;
    }

    /**
     * 判断是否为理想平衡
     * @return 理想平衡返回true
     */
    static boolean isBalanced(BinaryTreeNode<?> node) {
        return node.leftChild.height == node.rightChild.height;
    }

    /**
     * @return 获得该节点的AVL平衡因子
     */
    static int getBalancedFactor(BinaryTreeNode<?> node) {
        int leftHeight = node.leftChild == null ? -1 : node.leftChild.height;
        int rightHeight = node.rightChild == null ? -1 : node.rightChild.height;
        return leftHeight - rightHeight;
    }

    /**
     * 判断是否为AVL平衡
     * @return AVL平衡返回true
     */
    static boolean isAVLBalanced(BinaryTreeNode<?> node) {
        int factor = getBalancedFactor(node);
        return -2 < factor && factor < 2;
    }


    /**
     * 在树中插入一个元素
     * @param element 待插入的元素
     * @return 返回新插入的节点
     */
    @Override
    public BinaryTreeNode<T> insert (T element) {
        if (isEmpty()) {
            root = new BinaryTreeNode<>(element, null);
            size++;
            return root;
        }

        BinaryTreeNode<T> node = search(root, element);
        if (node != null)           //要求元素互异
            return node;

        size++;
        //设置待插入节点
        node = new BinaryTreeNode<>(element, hot);

        if (element.compareTo(hot.data) < 0) {
            hot.leftChild = node;
        } else {
            hot.rightChild = node;
        }

        /**
         * 这段代码是有问题的
         *  temp = element.compareTo(hot.data) < 0 ？ hot.leftChild ： hot.rightChild
         *  temp = node;
         *  这仅仅是将temp指向其他对象而已，并没有修改hot.lifeChild引用。
         *
         *  只有当temp用于读操作时，上述代码才有意义。例子请见BinarySearchTree中的removeAt1方法
         */

        //开始修复失衡情况
        for (BinaryTreeNode<T> p = hot; p != null; p = p.parent) {
            if (!isAVLBalanced(p)) {
                BinaryTreeNode<T> pp = p.parent;
                //rotateAt方法会正确修正子树的高度
                node = rotateAt(getTallerChild(getTallerChild(p)));
                if (pp == null) {
                    root = node;
                }
                //这里调整后的节点node与pp比较，与原先失衡节点p与pp比较是一样的。因为p与node都是在pp的同一颗子树下，而且有顺序性。
                else if (node.data.compareTo(pp.data) < 0) {
                    pp.leftChild = node;
                } else {
                    pp.rightChild = node;
                }
                break;          //p复衡后，整个树也就平衡了
            } else {
                //对于出现失衡的情况，整个树的高度势必不会增加
                //而对于插入后未出现失衡的情况，整个树的高度可能增加。
                //这个分支正是处理这种情况的
                updateHeight(p);
            }
        }
        return node;
    }

    /**
     * @param element 待删除的元素
     * @return 删除成功返回true
     */
    @Override
    public boolean remove(T element) {
        BinaryTreeNode<T> node = search(root, element);
        if (node == null) {
            return false;
        }
        removeAt(node);
        size--;
        updateHeight(hot);
        for (BinaryTreeNode<T> p = hot; p != null;) {
            BinaryTreeNode<T> pp = p.parent;
            if (!isAVLBalanced(p)) {     //节点p失衡
                //rotateAt方法会正确修正子树的高度
                node = rotateAt(getTallerChild(getTallerChild(p)));
                if (pp == null) {
                    root = node;
                }
                else if (node.data.compareTo(pp.data) < 0) {
                    pp.leftChild = node;
                } else {
                    pp.rightChild = node;
                }
            }
            p = pp;             //旋转后所得到的等价二叉树的根节点并不一定是p了，所以要从这里修正
            updateHeight(p);    //修正下一个要考察节点的高度，因为它的子树已经发生变化了
        }
        return true;
    }
}
