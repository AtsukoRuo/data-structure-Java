package cn.atsukoruo.list;

import cn.atsukoruo.heap.CompleteBinaryHeap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class SortTester {
    Integer[][] inputs = new Integer[5][];


    @BeforeEach
    public void init() {
        int index = 0;
        inputs[index++] = new Integer[] {1, 2 ,3 , 4, 5, 6, 7, 8};
        inputs[index++] = new Integer[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        inputs[index++] = new Integer[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        inputs[index++]= new Integer[] { 2, 2, 2, 2, 1, 1, 1, 6, 6, 6, 6, 0, 0, 0 ,0};
        inputs[index++] = new Integer[] {7, 1, 6, 3, 87, 10, 2, 6, 1, 2, 6, 8, 3};
    }


    @Test
    public void bubbleSort() {
        for (var e : inputs) {
            System.out.println("bubbleSortTest : " + Vector.bubbleSort(Vector.of(e)));
        }
    }

    @Test
    public void mergeSortV() {
        for (var e : inputs) {
            System.out.println("mergeSortTest : " + Vector.mergeSort(Vector.of(e)));
        }
    }

    @Test
    public void insertSort() {
        for (var e : inputs) {
            System.out.println("insertSortTest :" + List.insertSort(List.of(e)));
        }
    }

    @Test
    public void selectionSort() {
        for (var e : inputs) {
            System.out.println("mergeSortTest :" + List.mergeSort(List.of(e)));
        }
    }

    @Test
    public void heapSort() {
        for (var e : inputs) {
            Vector<Integer> vector = Vector.of(e);
            CompleteBinaryHeap.heapSort(vector);
            System.out.println("heapSortTest : " + vector.toString());
        }
    }
}
