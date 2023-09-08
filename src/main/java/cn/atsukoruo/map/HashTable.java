package cn.atsukoruo.map;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 采用开放定址策略的散列表——闭散列
 * BiFunction<Integer, Integer, Integer>表示用户自定义的Hash函数
 * 返回值表示哈希值，而第一个参数表示对象的散列表，第二个参数表示第几次探测，无需考虑表的大小
 *
 *  Function<Integer, Integer>>，返回一个新的容量，参数表示旧的容量。用在重哈希中
 *
 *  K 必须重载hashCode与equal函数
 *
 *  最多会探测capacity次，超出这个限制视为失败
 */
public class HashTable<K, V> extends Map<K, V>{
    private static class LazyMarkingObject<K, V> {
        boolean isDelete = false;
        V value;
        K key;
    }

    int capacity;       //桶的数量
    int size;           //词条的数量
    LazyMarkingObject<K, V>[] buckets;

    double getFactor() {
        return (double)size / (double) capacity;
    }

    BiFunction<Integer, Integer, Integer> hash;                 //哈希函数
    Function<Integer, Integer> getNewCapacity;
    public double limit;                                       //装载因子的上限



    //capacity强烈推荐是素数
    public HashTable(
            int capacity,
            double limit,
            BiFunction<Integer, Integer, Integer> hash,
            Function<Integer, Integer> getNewCapacity
    ) {
        this.capacity = capacity;
        this.limit = limit;
        this.hash = hash;
        this.getNewCapacity = getNewCapacity;
        buckets = new LazyMarkingObject[this.capacity];
        for (int i = 0; i < this.capacity; i++) {
            buckets[i] = new LazyMarkingObject<>();
        }
    }

    private void rehash() {
        int newCapacity = getNewCapacity.apply(this.capacity);
        HashTable<K, V> newHashTable = new HashTable<>(newCapacity, limit, hash, null);
        for (int i = 0; i < this.capacity; i++) {
            LazyMarkingObject<K, V> object = buckets[i];
            if (object.isDelete == false && object.key != null)
                newHashTable.put(object.key, object.value);
        }
        buckets = newHashTable.buckets;
        this.capacity = newCapacity;
    }
    @Override
    public boolean put(K key, V value) {
        if (key == null)
            return false;
        size += 1;
        if (getFactor() >= limit) {
            rehash();
        }

        for (int i = 1; i < this.capacity;i++) {
            int address = hash.apply(key.hashCode(), i) % capacity;
            //System.out.printf("put key %s at %d%n", key, address);
            if (buckets[address].key != null && buckets[address].key.equals(key)) {
                return true;
            }
            if (buckets[address].isDelete || buckets[address].key == null) {
                buckets[address].key = key;
                buckets[address].value = value;
                buckets[address].isDelete = false;
                break;
            }
        }
        return true;
    }

    @Override
    public V get(K key) {
        if(key == null) return null;
        for (int i = 1; i < this.capacity; i++) {
            int address = hash.apply(key.hashCode(), i) % capacity;
            //System.out.printf("get key %s at %d%n", key, address);
            if (buckets[address].isDelete) continue;
            if (buckets[address].key == null) return null;
            if (buckets[address].key.equals(key)) return buckets[address].value;
        }
        return null;
    }

    @Override
    public boolean remove(K key) {
        if (key == null) return false;
        for (int i = 1; i < this.capacity; i++) {
            int address = hash.apply(key.hashCode(), i) % capacity;
            System.out.printf("remove key %s at %d%n", key, address);
            if (buckets[address].key == null) return false;
            if (buckets[address].isDelete) continue;
            if (buckets[address].key.equals(key)) {
                buckets[address].isDelete = true;
                size--;
                return true;
            }
        }
        return false;
    }

}
