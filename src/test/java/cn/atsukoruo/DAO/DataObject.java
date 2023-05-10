package cn.atsukoruo.DAO;

/**
 * 专门为测试提供的数据类型。并不支持并发测试
 */
public class DataObject {
    private static int counter = 0;
    public final int id = counter++;

    public int data;

    @Override public String toString() {
        return "{%d: %d}".formatted(id, data);
    }

    /**
     * 计数器置零
     */
    static public void reset() {
        counter = 0;
    }
}
