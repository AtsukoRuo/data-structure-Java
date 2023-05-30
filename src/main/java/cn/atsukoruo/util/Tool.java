package cn.atsukoruo.util;

public class Tool {
    public static <T> void swap(T lhs, T rhs) {
        T temp = lhs;
        lhs = rhs;
        rhs = temp;
    }
}
