package cn.atsukoruo.util;


import java.math.BigInteger;

final public class Fibonacci {
    final static int NUM = 90;
    final static long[] cachedItem = new long[NUM + 1];
    static {
        cachedItem[0] = 1;
        cachedItem[1] = 1;
        for (int i = 2; i <= NUM; i++) {
            cachedItem[i] = cachedItem[i - 1] + cachedItem[i - 2];
            //在92时就会抛出异常
            if (cachedItem[i] < 0) {
                throw new ArithmeticException("At " + i);
            }
        }
    }
    static public long getCachedItem(int index) {
        return cachedItem[index];
    }



    final private BigInteger[] item;
    private int index;
    private int num;
    /**
     * 内部使用BigInteger实现<br>
     * 如果只获取前90项，推荐使用getCachedFibonacci()<br>
     * 内部维护一个指针，默认指向最后一项。<br>
     * Fib(0) = 1, Fib(1) = 1, Fib(2) = 2 ...
     * @param num 指定最大项为Fib[num]
     */
    public Fibonacci(int num) {
        this.num = num;
        index = num;
        item = new BigInteger[num + 1];
        item[0] = new BigInteger("1");
        item[1] = new BigInteger("1");
        for (int i = 2; i <= num; i++) {
            item[i] = new BigInteger("0");
            item[i] = item[i].add(item[i - 1]).add(item[i - 2]);
        }
    }


    /**
     * 返回指定的项
     * @param index 要获取项的自变量
     * @return 返回Fib(index);
     */
    public BigInteger getItem(int index) {
        return item == null ? null : item[index];
    }

    /**
     * 返回指针所指的项
     * @return 返回指针所指的项
     */
    public BigInteger get() {
        return item == null ? null : item[index];
    }

    /**
     * 返回指针所指的项
     * @return 返回指针所指的项
     */
    public long getCachedItem() {
        return cachedItem[index];
    }
    /**
     * 指针减一
     */
    public Fibonacci prev() {
        if (index != 0) index -= 1;
        return this;
    }
    /**
     * 指针加一
     */
    public Fibonacci next() {
        if (index != num) index += 1;
        return this;
    }

    public static void main(String[] args) {
        System.out.println(Fibonacci.getCachedItem(90));
        System.out.println(new Fibonacci(91).prev().get().toString());
    }
}
