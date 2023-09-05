package cn.atsukoruo.graph;

public class MutableInteger {
    private int value;

    public MutableInteger() {}
    public MutableInteger(int value) {
        this.value = value;
    }

    public int value() { return value; }
    public void value(int value) {
        this.value = value;
    }

    public int getAndAdd(int delta) {
        int x = value;
        value += delta;
        return x;
    }

    public MutableInteger add(int value) {
        this.value += value;
        return this;
    }

    public MutableInteger add(MutableInteger mutableInteger) {
        this.value += mutableInteger.value;
        return this;
    }
}
