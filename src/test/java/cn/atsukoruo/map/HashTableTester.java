package cn.atsukoruo.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Vector;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HashTableTester {

    static final int DEFAULT_CAPACITY = 47;
    @Test
    public void insertTest() {
        HashTable<HashTestObject, Object> hashTable = new HashTable<>(
                DEFAULT_CAPACITY,
                0.7,
                HashTestObject.linearProbeHash,
                HashTestObject.getNewCapacity
        );
        hashTable.put(new HashTestObject(100), 1);
        Assertions.assertTrue(hashTable.get(new HashTestObject(100)) != null);
    }

    @Test
    public void insertTest2() {
        Vector<Integer> vector = new Vector<>();
    }


    private class HashTestObject {
        int value;
        public HashTestObject(int value) {
            this.value = value;
        }
        static BiFunction<Integer, Integer, Integer> linearProbeHash = (hashcode, i) -> {
            return hashcode + i;
        };
        static Function<Integer, Integer> getNewCapacity = (capacity) -> { return capacity * 2; };
        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            return value == ((HashTestObject)obj).value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}


