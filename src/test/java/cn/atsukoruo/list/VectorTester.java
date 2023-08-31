package cn.atsukoruo.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VectorTester {


    //测试单次插入是否成功
    @Test
    public void insert1() {
        Vector<Integer> vector = new Vector<>();
        vector.insert(12);
        vector.insert(23);
    }

    static final private int NUM_INSERT = 100_0000;
    @Test
    public void insert2() throws Exception {
        Vector<Integer> vector = new Vector<>();
        for (int i = 0; i < NUM_INSERT; i++) {
            vector.insert(i);
        }
        //这里不用getSequenceVector，因为我们还要测试多次扩容
        for (int i = 0; i < NUM_INSERT; i++) {
            assertEquals(vector.get(i), i);
        }
        assertEquals(vector.size(), NUM_INSERT);


        int value = getVectorCapacity(vector);
        assertTrue((int)value <= vector.size() * 2
                && vector.size() <= (int) value);
    }


    @Test
    public void deleteSingleElement() {
        Vector<Integer> vector = getSequenceVector(1000);
        vector.remove(0);
        assertEquals(1, vector.get(0));
        assertEquals(999, vector.size());
    }

    @Test
    public void deleteElements() {
        Vector<Integer> vector = getSequenceVector(1000);
        vector.remove(100, 999);
        assertEquals(999, vector.get(100));

        vector = getSequenceVector(1000);
        vector.remove(99, 110);
        assertEquals(110, vector.get(99));
        assertEquals(989, vector.size());

        vector = getSequenceVector(1000);
        vector.remove(100, 110);
        assertEquals(999, vector.get(989));
    }

    @Test
    public void shrinkVector() {
        Vector<Integer> vector = getSequenceVector(1000_000);
        for (int i = 0; i < 999; i++) {
            vector.remove(vector.size() - 1000, vector.size());
        }

    }
    @Test
    public void findTest() {
        Vector<Integer> vector = getSequenceVector(1000);
        assertEquals(199 ,vector.find(100, 200, 300));
        assertEquals(100, vector.find(100, 100, 300));
        assertEquals(100, vector.find(100, 90, 101));
        assertEquals(98, vector.find(100000, 99 ,100));
        assertEquals(90, vector.find(90));
        assertEquals(-1, vector.find(10000));
        assertEquals(99, vector.find(200, 100, 200));
        assertEquals(199, vector.find(199, 100, 200));
    }

    @Test
    public void binarySearchTest() {
        Vector<Integer> vector = getSequenceVector(1000);
        assertEquals(-1 ,Vector.binarySearch(vector,100,200, 300));
        assertEquals(100, Vector.binarySearch(vector,100, 100, 300));
        assertEquals(100, Vector.binarySearch(vector,100, 90, 101));
        assertEquals(-1, Vector.binarySearch(vector,100000, 99 ,100));
        assertEquals(90, Vector.binarySearch(vector,90));
        assertEquals(-1, Vector.binarySearch(vector,10000));
        assertEquals(-1, Vector.binarySearch(vector,200, 100, 200));
        assertEquals(199,Vector.binarySearch(vector,199, 100, 200));
    }
    @Test
    public void unsortTest() {
        Vector<Integer> vector = getSequenceVector(10);
        vector.unsort();
        System.out.println("unsort test : " + vector);
    }

    @Test
    public void deduplicateTest() {
        Vector<Integer> vector = new Vector<>();
        for (int i = 0; i < 10; i++) {
            vector.insert(1);
        }
        vector.insert(2);
        for (int i = 0; i < 10; i++) {
            vector.insert(3);
        }
        vector.deduplicate();
        System.out.println("deduplicate test : " + vector);
        assertEquals(3, vector.size());

        Vector<Integer> vector1 = new Vector<>();
        vector1.insert(1);
        vector1.deduplicate();
        assertEquals(1, vector1.size());
    }
    @Test
    public void removeAllTest() {
        Vector<Integer> vector = getSequenceVector(100_0000);
        vector.remove(0, vector.size());
        assertEquals(0, vector.size());
    }

    @Test
    public void uniquifyTest() {
        Object o = Integer.valueOf(1);
        Comparable comparable = (Comparable) o;
        Vector<Integer> vector = new Vector<>();
        for (int i = 0; i < 10; i++) {
            vector.insert(1);
        }
        vector.insert(2);
        for (int i = 0; i < 10; i++) {
            vector.insert(3);
        }

        Vector.uniquify(vector);
        System.out.println("deduplicate test : " + vector);
        assertEquals(3, vector.size());

        Vector<Integer> vector1 = new Vector<>();
        vector1.insert(1);
        Vector.uniquify(vector1);
        assertEquals(1, vector1.size());
    }

    @Test
    public void search() {
        Vector<Integer> vector = getSequenceVector(1000);
        assertEquals(199 ,Vector.search(vector, 100, 200, 300));
        assertEquals(100, Vector.search(vector,100, 100, 300));
        assertEquals(100, Vector.search(vector,100, 90, 101));
        assertEquals(99, Vector.search(vector,100000, 99 ,100));
        assertEquals(90, Vector.search(vector,90));
        assertEquals(999, Vector.search(vector,10000));
    }

    @Test
    public void ofArrayTest() {
        Integer[] integers = new Integer[] {3, 5, 1, 2, 3};
        Vector<Integer> vector = Vector.of(integers);
        System.out.println("ofArrayTest : " + vector);
        assertEquals(integers.length, vector.size());
    }


    private int getVectorCapacity(Vector v) {
        Class<Vector> vectorClass = (Class<Vector>)Vector.class;
        Field field;
        try {
            field = vectorClass.getDeclaredField("capacity");
            field.setAccessible(true);
            Object value = field.get(v);
            return (int)value;
        } catch (Exception e) {
            return 0;
        }
    }

    static final Random rand = new Random(47);
    private Vector<Integer> getRandomVector(int size) {
        Vector ret = new Vector(size);
        for (int i = 0; i < size; i++) {
            ret.insert(rand.nextInt());
        }
        return ret;
    }

    private Vector<Integer> getSequenceVector(int size) {
        Vector<Integer> ret = new Vector<>(size);
        for (int i = 0; i < size; i++) {
            ret.insert(i);
        }
        return ret;
    }

}
