package cn.atsukoruo.map;

public class Entry<K extends Comparable<K>, V>
    implements Comparable<Entry<K, V>> {
    K key;
    V value;

    public Entry() {}
    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int compareTo(Entry<K, V> entry) {
        return key.compareTo(entry.key);
    }

    public boolean equals(Entry<K, V> entry) {
        return compareTo(entry) == 0;
    }

}
