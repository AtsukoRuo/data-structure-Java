package cn.atsukoruo.heap;

import cn.atsukoruo.list.Vector;

public class CompleteBinaryHeap<T extends Comparable<T>> implements Heap<T>{
    int size = 0;
    Vector<T> vector;

    public CompleteBinaryHeap() {
         vector = new Vector<>();
    }

    /**
     * 引用vector，而不是复制vecotr
     */
    public CompleteBinaryHeap(Vector<T> vector) {
        this.vector = vector;
        this.size = vector.size();
        heapify();
    }

    /**
     * 判断节点i是否在堆中
     * @param size 堆的大小
     */
    static boolean isInHeap(int size, int i) {
        return -1 < i && i < size;
    }

    static int parent(int i) {
        return (i - 1) >> 1;            //相当于i/2向下取整
    }

    /**
     * 返回最后一个节点的父亲，即内部节点
     * @param size 堆的大小
     */
    static int lastInternal(int size) {
        return parent( size - 1);
    }

    static int leftChild(int i) {
        return (i << 1) + 1;
    }

    static int rightChild(int i) {
        return (i + 1) << 1;
    }

    static boolean hasParent(int i) {
        return i != 0;
    }

    static boolean hasLeftChild(int size, int i) {
        return isInHeap(size, leftChild(i));
    }

    static boolean hasRightChild(int size, int i) {
        return isInHeap(size, rightChild(i));
    }

    /**
     * 指定两个节点，取出最大者。如果两者相同，那么返回i
     */
    int getBigger( int i, int j) {
        return vector.get(i).compareTo(vector.get(j)) >= 0 ? i : j;
    }

    /**
     * 节点i、i的左孩子、i的右孩子中最大者
     */
    int properParent(int i) {
        return hasRightChild(size, i) ? (getBigger(i, getBigger(leftChild(i), rightChild(i)))) :
                hasLeftChild(size, i) ? getBigger(i, leftChild(i)) : i;
    }


    @Override
    public void insert(T data) {
        vector.insert(data);
        size += 1;
        percolateUp(size - 1);
    }

    /**
     * 返回堆中最大者，如果堆为空，那么返回null
     */
    @Override
    public T getMax() {
        return size == 0 ? null : vector.get(0);
    }

    /**
     * 删除并返回堆中最大者。如果堆为空，那么返回null
     */
    @Override
    public T delMax() {
        if (size == 0) return null;
        T ret = vector.get(0);

        //vector.set(vector.remove(vector.size() - 1), 0) 是错误的，它无法处理删除最后一个元素的情况
        Vector.swap(vector, 0, vector.size() - 1);
        vector.remove(vector.size() - 1);
        size -= 1;
        percolateDown(0);
        return ret;
    }

    private int percolateDown(int i) {
        int j;
        while (i != (j = properParent(i))) {
            Vector.swap(vector, i, j);
            i = j;
        }
        return i;
    }

    private int percolateUp(int i) {
        while (hasParent(i)) {
            int j = parent(i);
            if (getBigger(i, j) == j)
                break;
            Vector.swap(vector, i, j);
            i = j;
        }
        return i;
    }

    void heapify() {
        for (int i = lastInternal(size); isInHeap(size, i); i--) {
            percolateDown(i);
        }
    }
    public static <T extends Object & Comparable<T>>
    void heapSort(Vector<T> vector) {
        CompleteBinaryHeap<T> heap = new CompleteBinaryHeap<>(vector);
        while (heap.size > 0) {
            Vector.swap(heap.vector, 0, --heap.size);
            heap.percolateDown(0);
        }
    }
}
