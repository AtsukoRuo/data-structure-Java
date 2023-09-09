package cn.atsukoruo.heap;

public interface Heap<T> {

    void insert(T data);
    T getMax();
    T delMax();
}