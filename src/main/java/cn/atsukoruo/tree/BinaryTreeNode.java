package cn.atsukoruo.tree;

import cn.atsukoruo.list.Queue;
import cn.atsukoruo.list.Stack;
import java.util.function.Consumer;
public class BinaryTreeNode<T extends Comparable<T>>
    implements Comparable<BinaryTreeNode<T>> {
    T data;                         //数据域
    BinaryTreeNode<T> parent;
    BinaryTreeNode<T> leftChild;
    BinaryTreeNode<T> rightChild;
    int height;                     //在不同的树中，高度的定义是不同的
    RBColor color;

    public T getData() {
        return data;
    }

    public int getHeight() {
        return height;
    }

    public RBColor getColor() {
        return color;
    }

    public int getDepth() {
        int cnt = -1;
        BinaryTreeNode<T> node = this;
        while (node != null) {
            cnt += 1;
            node = node.parent;
        }
        return cnt;
    }
    public int id;                     //用于debug打印树

    public BinaryTreeNode() {
        color = RBColor.RED;
    }
    
    public BinaryTreeNode(T data, BinaryTreeNode<T> parent, BinaryTreeNode<T> leftChild,
                          BinaryTreeNode<T> rightChild, int height, RBColor color) {
        this.data = data;
        this.parent = parent;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.height = height;
        this.color = color;

    }


    private BinaryTreeNode<T> copyNode(BinaryTreeNode<T> node, BinaryTreeNode<T> parent) {
        if (node == null) return null;
        return new BinaryTreeNode<>(
                node.data,
                parent,
                copyNode(node.leftChild, this),
                copyNode(node.rightChild, this),
                node.height,
                node.color
        );
    }

    public BinaryTreeNode(T data, BinaryTreeNode<T> parent) {
        this(data, parent, null, null, 0, RBColor.RED);
    }

    public BinaryTreeNode(T data, BinaryTreeNode<T> parent, BinaryTreeNode<T> leftChild, BinaryTreeNode<T> rightChild) {
        this(data, parent, leftChild, rightChild, 0, RBColor.RED);
    }

    @Override
    public int compareTo(BinaryTreeNode<T> node) {
        return this.data.compareTo(node.data);
    }

    /**
     * 两个节点相等，当且仅当它们的数据域相等
     * @param obj
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == null || BinaryTreeNode.class != obj.getClass())
            return false;
        return this.data.compareTo(((BinaryTreeNode<T>)obj).data) == 0;
    }

    /**
     * 判断该节点是否为root
     */
    public static <T extends Comparable<T>>
    boolean isRoot(BinaryTreeNode<T> node) {
        return node.parent == null;
    }

    /**
     * 判断该节点是否为其父节点的左孩子，特别地，对于根节点返回false
     */
    public static <T extends Comparable<T>>
    boolean isLeftChild(BinaryTreeNode<T> node) {
        return !isRoot(node) && node.parent.leftChild == node;
    }

    /**
     * 判断该节点是否为其父节点的有孩子，特别地，对于根节点返回false
     */
    public static <T extends Comparable<T>>
    boolean isRightChild(BinaryTreeNode<T> node) {
        return !isRoot(node) && node.parent.rightChild == node;
    }

    /**
     * 是否有父亲
     */
    public static <T extends Comparable<T>>
    boolean hasParent(BinaryTreeNode<T> node) {
        return !isRoot(node);
    }

    /**
     * 是否有左孩子
     */
    public static <T extends Comparable<T>>
    boolean hasLeftChild(BinaryTreeNode<T> node) {
        return node.leftChild != null;
    }

    /**
     * 是否有右孩子
     */
    public static <T extends Comparable<T>>
    boolean hasRightChild(BinaryTreeNode<T> node) {
        return node.rightChild != null;
    }

    /**
     * 是否至少有一个孩子
     */
    public static <T extends Comparable<T>>
    boolean hasChild(BinaryTreeNode<T> node) {
        return hasLeftChild(node) || hasRightChild(node);
    }

    /**
     * 是否左右孩子都有
     */
    public static <T extends Comparable<T>>
    boolean hasBothChild(BinaryTreeNode<T> node) {
        return hasLeftChild(node) && hasRightChild(node);
    }


    /**
     * 是否为叶子
     */
    public static <T extends Comparable<T>>
    boolean isLeaf(BinaryTreeNode<T> node) {
        return !hasChild(node);
    }

    /**
     * 获得兄弟节点
     */
    public static <T extends Comparable<T>>
    BinaryTreeNode<T> getSibling(BinaryTreeNode<T> node) {
        if (isRoot(node)) return null;
        return isLeftChild(node) ? node.parent.rightChild : node.parent.leftChild;
    }

    /**
     * 获得叔叔节点
     */
    public static <T extends Comparable<T>>
    BinaryTreeNode<T> getUncle(BinaryTreeNode<T> node) {
        if (isRoot(node) || isRoot(node.parent)) return null;
        return isLeftChild(node.parent) ? node.parent.parent.rightChild : node.parent.parent.leftChild;
    }


    /**
     * 将data作为当前节点的左孩子插入到二叉树中
     */
    public BinaryTreeNode<T> insertAsLeft(T data) {
        return leftChild = new BinaryTreeNode<>(data, this);
    }

    /**
     * 将data作为当前节点的右孩子插入到二叉树中
     */
    public BinaryTreeNode<T> insertAsRight(T data) {
        return rightChild = new BinaryTreeNode<>(data, this);
    }

    public static <T extends Comparable<T>>
    void travelPre(BinaryTreeNode<T> node, Consumer<T> consumer) {
        if (node == null) return;
        consumer.accept(node.data);
        travelPre(node.leftChild, consumer);
        travelPre(node.rightChild, consumer);
    }
    public static <T extends Comparable<T>>
    void travelPreIteration1(BinaryTreeNode<T> node, Consumer<T> consumer) {
        Stack<BinaryTreeNode<T>> nodes = new Stack<>();
        if (node != null)
            nodes.push(node);
        while (!nodes.empty()) {
            BinaryTreeNode<T> x = nodes.pop();
            consumer.accept(x.data);
            if (hasRightChild(x))
                nodes.push(x.rightChild);
            if (hasLeftChild(x))
                nodes.push(x.leftChild);
        }
    }
    public static <T extends Comparable<T>>
    void travelPreIteration(BinaryTreeNode<T> node, Consumer<T> consumer) {
        Stack<BinaryTreeNode<T>> rightNodes = new Stack<>();
        while (true) {
            while (node != null) {
                consumer.accept(node.data);
                if (node.rightChild != null)
                    rightNodes.push(node.rightChild);
                node = node.leftChild;
            }
            if (rightNodes.empty()) break;
            node = rightNodes.pop();
        }
    }

    public static <T extends Comparable<T>>
    void travelIn(BinaryTreeNode<T> node, Consumer<T> consumer) {
        if (node == null) return;
        travelIn(node.leftChild, consumer);
        consumer.accept(node.data);
        travelIn(node.rightChild, consumer);
    }

    public static <T extends Comparable<T>>
    void travelInIteration(BinaryTreeNode<T> node, Consumer<T> consumer) {
        Stack<BinaryTreeNode<T>> leftNodes = new Stack<>();
        while (true) {
            while (node != null) {
                leftNodes.push(node);
                node = node.leftChild;
            }
            if (leftNodes.empty())
                break;
            node = leftNodes.pop();
            consumer.accept(node.data);
            node = node.rightChild;
        }
    }


    private static <T extends Comparable<T>>
    void gotoHLVFL(Stack<BinaryTreeNode<T>> stack) {
        BinaryTreeNode<T> node;
        while ((node = stack.top()) != null) {
            if (node.leftChild != null) {           //尽可能往左
                if (node.rightChild != null)        //如果有右孩子，优先入栈
                    stack.push(node.rightChild);
                stack.push(node.leftChild);
            } else {                                //实在不得已才往右
                stack.push(node.rightChild);
            }
        }
        stack.pop();        //弹出空节点
    }

    public static <T extends Comparable<T>>
    void travelPostIteration(BinaryTreeNode<T> x, Consumer<T> consumer) {
        Stack<BinaryTreeNode<T>> nodes = new Stack<>();
        if (x != null) nodes.push(x);
        while (!nodes.empty()) {
            if (x.parent != nodes.top())        //此时x必为右孩子
                gotoHLVFL(nodes);
            x = nodes.pop();
            consumer.accept(x.data);
        }
    }

    public static <T extends Comparable<T>>
    void travelPost(BinaryTreeNode<T> node, Consumer<T> consumer) {
        if (node == null) return;
        travelPost(node.leftChild, consumer);
        travelPost(node.rightChild, consumer);
        consumer.accept(node.data);
    }

    public static <T extends Comparable<T>>
    void travelLevel(BinaryTreeNode<T> node, Consumer<T> consumer) {
        Queue<BinaryTreeNode<T>> queue = new Queue<>();
        queue.enqueue(node);
        while (!queue.empty()) {
            node = queue.dequeue();
            consumer.accept(node.data);
            if (node.leftChild != null)
                queue.enqueue(node.leftChild);
            if (node.rightChild != null)
                queue.enqueue(node.rightChild);
        }
    }

    public BinaryTreeNode<T> getSucceedNode() {
        BinaryTreeNode<T> x = this;
        if (this.rightChild != null) {
            x = this.rightChild;
            while (x.leftChild != null)
                x = x.leftChild;
        } else {
            while (!isLeftChild(x))
                x = x.parent;
            x = x.parent;
        }
        return x;
    }
}

enum RBColor {
    RED, BLACK;
}
