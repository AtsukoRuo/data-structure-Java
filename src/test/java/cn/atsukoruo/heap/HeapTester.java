package cn.atsukoruo.heap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeapTester {

    @Test
    public void insertTest1() {
        CompleteBinaryHeap<Integer> heap = new CompleteBinaryHeap<>();

        for (int i = 0; i < 10; i++) {
            heap.insert(i);
        }
        Assertions.assertEquals(9, (int) heap.getMax());
    }


    @Test
    public void removeTest1() {
        CompleteBinaryHeap<Integer> heap = new CompleteBinaryHeap<>();

        for (int i = 0; i < 100; i++) {
            heap.insert(i);
        }

        int max = 99;
        for (int i = 0; i < 50; i++) {
            Assertions.assertEquals(max--, heap.delMax());
        }
        Assertions.assertEquals(max, heap.getMax());
    }

    @Test
    public void removeTest2() {
        CompleteBinaryHeap<Integer> heap = new CompleteBinaryHeap<>();
        Assertions.assertEquals(null, heap.delMax());
    }

    @Test
    public void removeTest3() {
        CompleteBinaryHeap<Integer> heap = new CompleteBinaryHeap<>();
        heap.insert(1);
        Assertions.assertEquals(1, heap.delMax());
    }
}
