package cn.atsukoruo.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
        System.out.println(vector.toString());
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
        System.out.println((int)value);
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

    @Test
    public void deleteSingleElement() {
        Vector<Integer> vector = getSequenceVector(1000);
        vector.remove(0);
        System.out.println(vector.get(0));
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
    }

    @Test
    public void shrinkVector() {
        Vector<Integer> vector = getSequenceVector(1000_000);
        for (int i = 0; i < 999; i++) {
            vector.remove(vector.size() - 1000, vector.size());
        }
        System.out.println("shrink : " + vector.size() + " " + getVectorCapacity(vector));
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

    @Test
    public void deduplicateVector() {

    }
}
