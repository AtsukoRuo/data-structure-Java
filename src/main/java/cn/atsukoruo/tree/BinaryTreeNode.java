package cn.atsukoruo.tree;

import cn.atsukoruo.list.Queue;
import cn.atsukoruo.list.Stack;
import java.util.function.Consumer;
record A(Integer i) {

}
public class BinaryTreeNode<T extends Comparable<T>>
    implements Comparable<BinaryTreeNode<T>> {
    T data;         //数据域
    BinaryTreeNode<T> parent;
    BinaryTreeNode<T> leftChild;
    BinaryTreeNode<T> rightChild;
    int height;         //在不同的树中，高度的定义是不同的
    RBColor color;

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

    public BinaryTreeNode(T data, BinaryTreeNode<T> parent) {
        this.data = data;
        this.parent = parent;
        color = RBColor.RED;
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


    public static <T extends Comparable<T>>
    boolean isRoot(BinaryTreeNode<T> node) {
        return node.parent == null;
    }

    public static <T extends Comparable<T>>
    boolean isLeftChild(BinaryTreeNode<T> node) {
        return !isRoot(node) && node.parent.leftChild == node;
    }

    public static <T extends Comparable<T>>
    boolean isRightChild(BinaryTreeNode<T> node) {
        return !isRoot(node) && node.parent.rightChild == node;
    }

    public static <T extends Comparable<T>>
    boolean hasRoot(BinaryTreeNode<T> node) {
        return !isRoot(node);
    }

    public static <T extends Comparable<T>>
    boolean hasLeftChild(BinaryTreeNode<T> node) {
        return node.leftChild != null;
    }

    public static <T extends Comparable<T>>
    boolean hasRightChild(BinaryTreeNode<T> node) {
        return node.rightChild != null;
    }

    public static <T extends Comparable<T>>
    boolean hasChild(BinaryTreeNode<T> node) {
        return hasLeftChild(node) || hasRightChild(node);
    }

    public static <T extends Comparable<T>>
    boolean hasBothChild(BinaryTreeNode<T> node) {
        return hasLeftChild(node) && hasRightChild(node);
    }

    public static <T extends Comparable<T>>
    boolean isLeaf(BinaryTreeNode<T> node) {
        return !hasChild(node);
    }

    public static <T extends Comparable<T>>
    BinaryTreeNode<T> getSibling(BinaryTreeNode<T> node) {
        if (isRoot(node)) return null;
        return isLeftChild(node) ? node.parent.rightChild : node.parent.leftChild;
    }

    public static <T extends Comparable<T>>
    BinaryTreeNode<T> getUncle(BinaryTreeNode<T> node) {
        if (isRoot(node) || isRoot(node.parent)) return null;
        return isLeftChild(node.parent) ? node.parent.parent.rightChild : node.parent.parent.leftChild;
    }

    public BinaryTreeNode<T> insertAsLeft(T data) {
        return leftChild = new BinaryTreeNode<>(data, this);
    }
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
        do {
            consumer.accept(node.data);
            if (node.leftChild != null) {
                node = node.leftChild;
            } else if (node.rightChild != null) {
                node = node.rightChild;
            } else {
                node = node.parent;
            }
        } while (!isRoot(node));
    }

    public static <T extends Comparable<T>>
    void travelPreIteration2(BinaryTreeNode<T> node, Consumer<T> consumer) {
        Stack<BinaryTreeNode<T>> rightNodes = new Stack<>();
        while (true) {
            while (node != null) {
                consumer.accept(node.data);
                if (node.rightChild != null)
                    rightNodes.push(node.rightChild);
                node = node.leftChild;
            }
            if (rightNodes.empty())
                break;
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
    void travelInIteration1(BinaryTreeNode<T> node, Consumer<T> consumer) {
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

    public static <T extends Comparable<T>>
    void travelInIteration2(BinaryTreeNode<T> node, Consumer<T> consumer) {
        boolean hasBacktracked = false;
        while (true) {
            if (!hasBacktracked && node.leftChild != null) {
                node = node.leftChild;
            } else {
                consumer.accept(node.data);
                if (node.rightChild != null) {
                    node = node.rightChild;
                    hasBacktracked = false;
                } else {
                    if ((node = node.getSucceedNode()) == null) break;
                    hasBacktracked = true;
                }
            }
        }
    }

    private static <T extends Comparable<T>>
    void gotoHLVFL(Stack<BinaryTreeNode<T>> stack) {
        BinaryTreeNode<T> node;
        while ((node = stack.top()) != null) {
            if (node.leftChild != null) {
                if (node.rightChild != null)
                    stack.push(node.rightChild);
                stack.push(node.leftChild);
            } else {
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
            if (x != nodes.top())
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
