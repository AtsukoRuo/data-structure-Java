package cn.atsukoruo.map;

public abstract class Map<K, V> {

    abstract public boolean put(K key, V value);

    abstract public V get(K key);

    abstract public boolean remove(K key);
}
