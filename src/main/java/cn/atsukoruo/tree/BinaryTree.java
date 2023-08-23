package cn.atsukoruo.tree;

import cn.atsukoruo.list.Stack;
import cn.atsukoruo.list.Vector;

import java.util.function.Consumer;

public class BinaryTree<T extends Comparable<T>> {
    protected int size;         //节点的个数
    protected BinaryTreeNode<T> root;

    /**
     * 更新当前节点的高度
     * @param node 要修改的节点
     * @return 返回修改后的高度
     */
    protected int updateHeight(BinaryTreeNode<T> node) {
        int leftHeight = node.leftChild == null ? -1 : node.leftChild.height;
        int rightHeight = node.rightChild == null ? -1 : node.rightChild.height;
        return node.height = 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * 更新当前节点以及祖先节点的高度
     * @param node 要修改的节点
     */
    protected void updateHeightAbove(BinaryTreeNode<T> node) {
        while (node != null) {
            int oldHeight = node.height;
            updateHeight(node);

            //只有在树极度不平衡下，这种剪枝所带来的收益是最多的。
            if (node.height == oldHeight)
                break;
            node = node.parent;
        }
    }

    public BinaryTree() { }

    /**
     * 必须确保size与树的规模相对应，否则调用BinaryTree(BinaryTreeNode root)，动态计算树的大小
     * @param root 指定树根
     * @param size 指定大小
     */
    public BinaryTree(BinaryTreeNode<T> root, int size) {
        this.root = root;
        this.size = size;
    }
    public BinaryTree(BinaryTreeNode<T> root) {
        this.root = root;
        this.size = size(root);
    }
    public BinaryTree(BinaryTree<T> tree) {
        this.root = copyNode(tree.root);
    }

    private BinaryTreeNode<T> copyNode(BinaryTreeNode<T> node) {
        if (node == null) return null;
        BinaryTreeNode<T> newNode = new BinaryTreeNode<>(
                node.data,
                null,
                copyNode(node.leftChild),
                copyNode(node.rightChild),
                node.height,
                node.color
        );
        if (newNode.leftChild != null) newNode.leftChild.parent = newNode;
        if (newNode.rightChild != null) newNode.rightChild.parent = newNode;
        return newNode;
    }
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public BinaryTreeNode<T> getRoot() { return root; }

    /**
     * 当作根节点插入到空树中。如果当前树非空，那么会重置整棵树
     * @param data 待插入的数据
     * @return 返回根节点
     */
    public BinaryTreeNode<T> insertAsRoot(T data) {
        size = 1;
        return root = new BinaryTreeNode<>(data, null);
    }

    /**
     * 作为指定节点的左节点进行插入，适用于左节点为null的情况。
     * 如果指定节点存在非空左子树，那么会用新的节点代替该左子树。
     * @param x 指定节点
     * @param data 要插入的数据
     * @return 新的节点
     */
    public BinaryTreeNode<T> insertAsLeft(BinaryTreeNode<T> x, T data) {
        size += 1;
        x.insertAsLeft(data);
        updateHeightAbove(x);
        return x.leftChild;
    }

    /**
     * 作为指定节点的右节点进行插入，适用于右节点为null的情况。
     * 如果指定节点存在非空右子树，那么会用新的节点代替该右子树。
     * @param x 指定节点
     * @param data 要插入的数据
     * @return 新的节点
     */
    public BinaryTreeNode<T> insertAsRight(BinaryTreeNode<T> x, T data) {
        size += 1;
        x.insertAsRight((data));
        updateHeightAbove(x);
        return x.rightChild;
    }


    /**
     * 该树作为指定节点的左节点进行插入，适用于左节点为null的情况。
     * 如果指定节点存在非空左子树，那么会用要插入的子树代替该左子树。
     * @param x 指定节点
     * @param tree 要插入的子树
     * @return 返回x
     */
    public BinaryTreeNode<T> attachAsLeftTree(BinaryTreeNode<T> x, BinaryTree<T> tree) {
        //该树不为空树的情况下
        if ((x.leftChild = tree.root) != null) {
            x.leftChild.parent = x;
        }
        size += tree.size;
        updateHeightAbove(x);
        return x;
    }

    /**
     * 该树作为指定节点的右节点进行插入，适用于右节点为null的情况。
     * 如果指定节点存在非空右子树，那么会用要插入的子树代替该右子树。
     * @param x 指定节点
     * @param tree 要插入的子树
     * @return 返回x
     */
    public BinaryTreeNode<T> attachAsRightTree(BinaryTreeNode<T> x, BinaryTree<T> tree) {
        if ((x.rightChild = tree.root) != null) {
            x.rightChild.parent = x;
        }
        size += tree.size;
        updateHeightAbove(x);
        return x;
    }

    /**
     * 删除以指定节点为根的子树
     * @param x 待删除的节点
     * @return 返回删除节点的个数
     */
    public int remove(BinaryTreeNode<T> x) {
        int n = 0;
        if (BinaryTreeNode.isRoot(x)) {
            root = null;
            n = size;
            size = 0;
        } else {
            if (BinaryTreeNode.isLeftChild(x)) {
                x.parent.leftChild = null;      //在Java中，这一步相当于释放了子节点的内存资源
            } else {
                x.parent.rightChild = null;
            }
            updateHeightAbove(x.parent);
            n = size(x);
            size -= n;
        }
        return n;
    }
    /**
     * 返回以指定节点作为根的子树的规模
     * @param x 指定节点
     * @return 以指定节点作为根的子树的规模
     */
    public int size(BinaryTreeNode<T> x) {
        if (x == null) return 0;
        return 1 + size(x.leftChild) + size(x.rightChild);
    }

    /**
     * 将子树x从当前树中摘除，并将其封装为一颗独立的子树返回
     * @param x 被指定的节点
     * @return 返回以指定节点作为根的树
     */
    public BinaryTree<T> secede(BinaryTreeNode<T> x) {
        if (BinaryTreeNode.isRoot(x)) {
            return this;
        }

        if (BinaryTreeNode.isLeftChild(x)) {
            x.parent.leftChild = null;
        } else {
            x.parent.rightChild = null;
        }

        int n = size(x);
        this.size -= size;
        updateHeightAbove(x.parent);
        return new BinaryTree<>(x, n);
    }

    public void travelLevel(Consumer<T> consumer) {
        BinaryTreeNode.travelLevel(root, consumer);
    }

    public void travelPre(Consumer<T> consumer) {
        BinaryTreeNode.travelPre(root, consumer);
    }

    public void travelIn(Consumer<T> consumer) {
        BinaryTreeNode.travelIn(root, consumer);
    }

    public void travelPost(Consumer<T> consumer) {
        BinaryTreeNode.travelPost(root, consumer);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != BinaryTree.class)
            return false;
        return root == ((BinaryTree<?>)obj).root;
    }

    public void print() {
        int size = (int)Math.pow(2, root.height + 1);
        Vector<BinaryTreeNode<T>> nodes = new Vector<>(size, size);
        Stack<BinaryTreeNode<T>> stack = new Stack<>();
        BinaryTreeNode<T> node = root;
        root.id = (int)Math.pow(2, root.height);
        while (true) {
            while (node != null) {
                nodes.set(node, node.id);
                int diff = (int)Math.pow(2, node.height - 1);
                if (node.rightChild != null) {
                    node.rightChild.id = node.id - diff;
                    stack.push(node.rightChild);
                }
                if (node.leftChild != null) {
                    node.leftChild.id = node.id + diff;
                }
                node = node.leftChild;
            }
            if (stack.empty()) break;
            node = stack.pop();
        }

        for (var n : nodes) {
            if (n != null) {
                int depth = n.getDepth();
                for (int i = 0; i < depth; i++) {
                    System.out.print("      ");
                }
                if (!BinaryTreeNode.isRoot(n)) {
                    System.out.print(BinaryTreeNode.isLeftChild(n) ? "\\  " : "/  ");
                }
                System.out.print(n.data);
            }
            System.out.println(" ");
        }
    }
}



