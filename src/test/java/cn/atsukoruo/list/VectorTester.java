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

    Vector<Integer> vector;

    @BeforeEach
    public void init() {
        vector = new Vector<>();
    }

    //测试单次插入是否成功
    @Test
    public void insert1() {
        vector.insert(12);
        System.out.println(vector.toString());
    }

    static final private int NUM_INSERT = 100_0000;
    @Test
    public void insert2() throws Exception {
        for (int i = 0; i < NUM_INSERT; i++) {
            vector.insert(i);
        }
        //这里不用getSequenceVector，因为我们还要测试多次扩容
        for (int i = 0; i < NUM_INSERT; i++) {
            assertEquals(vector.get(i), i);
        }
        assertEquals(vector.size(), NUM_INSERT);


        Class<Vector> vectorClass = (Class<Vector>)Vector.class;
        Field field;
        try {
            field = vectorClass.getDeclaredField("capacity");
        } catch (NoSuchFieldException e) {
            return ;
        }
        field.setAccessible(true);
        Object value = field.get(vector);
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
}
