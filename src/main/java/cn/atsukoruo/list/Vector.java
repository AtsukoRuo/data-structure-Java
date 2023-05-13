package cn.atsukoruo.list;

import cn.atsukoruo.util.Fibonacci;

import java.util.Random;
import java.util.function.Consumer;

/**
 * 本类一开始是为无序向量设计的。但是后面又有实现有序向量的需求。
 * 最佳方法是设计一个接口，让无序向量与有序向量分别实现这个接口
 * 但是这样编码工作量巨大，本类仅仅是为了学习数据结构而练手的，不应该考虑这么多软件工程上的设计
 * 因此对于有序向量的方法，一律用static方法 + 自限定类型<T extends Comparable<T>>来实现的
 */
final public class Vector<T> {
    private static final int DEFAULT_CAPACITY = 47;  //默认初始容量
    private int size;       //当前有效元素的数量
    private int capacity;   //容量
    T[] data;       //数据

    final static private Random rand = new Random(System.currentTimeMillis());    //随机器，用于置乱算法中

    @SuppressWarnings("unchecked")
    public Vector() {
        capacity = DEFAULT_CAPACITY;
        data = (T[])new Object[capacity];
    }

    @SuppressWarnings("unchecked")
    public Vector(int capacity) {
        this.capacity = capacity;
        data = (T[])new Object[capacity];
    }

    /**
     * 获得一个容量为capacity，规模为size的Vector
     * @param capacity 指定容量，并且要满足 capacity >= size;
     * @param size 指定规模
     */
    public Vector(int capacity, int size) {
        this.capacity = capacity;
        this.size = size;
        data = (T[])new Object[size];
    }

    /**
     * 复制数组[left, right)中的元素
     * @param data 要被复制的数组
     */
    @SuppressWarnings("unchecked")
    private void copyFrom(T[] data, int left, int right) {
        if (left < 0 || right < 0 || left >= right) {
            return;
        }
        size = 0;
        this.data = (T[])new Object[(right - left) / 2];
        while (left < right) this.data[size++] = data[left++];
    }

    /**
     * 在装载因子 = 1时，对容量进行两倍的扩容
     */
    @SuppressWarnings("unchecked")
    private void expand() {
        Object[] oldData = data;
        capacity = Math.max(DEFAULT_CAPACITY, capacity * 2);

        data = (T[])new Object[capacity];
        System.arraycopy(oldData,0, data, 0, size);
    }

    /**
     * 当满足当前容量大于默认容量的两倍并且装载因子小于25%时
     * 进行收缩，容量进行减半
     */
    private void shrink() {
        if (capacity < DEFAULT_CAPACITY * 2
        || size * 4 > capacity)
            return;
        Object[] oldData = data;

        capacity /= 2;
        data = (T[])new Object[capacity];
        System.arraycopy(oldData, 0, data, 0, size);
    }

    /**
     * 获取rank为index的元素
     * @param index 要获取元素的rank
     * @return rank为index的元素
     */

    public T get(int index) {
        return data[index];
    }

    /**
     * 置乱整个向量
     */
    public void unsort() {
        unsort(0, size);
    }

    /**
     * 置乱向量[left, right)中的元素
     * @param left 待置乱区间的左端点
     * @param right 待置乱区间的左端点
     */
    public void unsort(int left, int right) {
        for (int i = right - 1; i > left; i--) {
            swap(this, i, rand.nextInt(i) + left);
        }
    }

    /**
     * 辅助函数，交换data数组中的两个元素
     * @param lhs 待交换元素的rank
     * @param rhs 待交换元素的rank
     */
    static private <U> void swap(Vector<U> vector, int lhs, int rhs) {
        U temp = vector.data[lhs];
        vector.data[lhs] = vector.data[rhs];
        vector.data[rhs] = temp;
    }

    /**
     * 在[left, right)中查找指定的元素，并返回其rank（如果成功的话）
     * @param element   待查找元素
     * @param left 待删除区间的左端
     * @param right 待查找区间的右端
     * @return 如果查找成功，返回元素的rank，如果有多个匹配的元素，那么返回最小的rank
     * 查找失败返回 -1
     */
    public int find(T element, int left, int right) {
        for (int index = left; index < right; index++) {
            if (data[index].equals(element)) return index;
        }
        return -1;
    }

    /**
     * 在整个向量中查找指定的元素，并返回其rank（如果成功的话）
     * @param element 待查找元素
     * @return 如果查找成功，返回元素的rank，如果有多个匹配的元素，那么返回最大的rank
     * 查找失败返回 left - 1
     */
    public int find(T element) {
        return find(element, 0, size);
    }

    /**
     * 在指定的rank处插入指定元素
     * @param element 待插入的元素
     * @param rank 插入的位置，要满足0 <= rank < size。
     * @return 返回参数rank
     */
    public int insert(T element, int rank) {
        expand();
        size += 1;
        System.arraycopy(data, rank, data, rank + 1, size - 1 - (rank + 1) + 1);
        data[rank] = element;
        return rank;
    }

    /**
     * 作为末元素插入
     * @param element 待插入的元素
     * @return 新插入元素的秩
     */
    public int insert(T element) {
        //虽然这里size不满足insert(T element, int rank)对于rank的要求
        //但是可以保证语义
        return insert(element, size);
    }

    /**
     * 删除向量区间[left, right)中的元素
     * @param left 待删除区间的左端点
     * @param right 待删除区间的右端点
     * @return 返回删除元素的个数, 即right - left
     */
    public int remove(int left, int right) {
        int length = (size - 1) - right + 1;
        size -= length;
        System.arraycopy(data, left, data, right, length);
        shrink();
        return length;
    }

    /**
     * 删除指定rank处的元素
     * @param rank 要被删除元素的rank
     * @return 返回删除的元素。
     */
    public T remove(int rank) {
        @SuppressWarnings("unchecked")
        T element = (T)data[rank];
        remove(rank, rank + 1);
        return element;
    }

    /**
     * 向量唯一化
     * @return 删除元素的个数
     */
    @SuppressWarnings("unchecked")
    public int deduplicate() {
        int deletedNum = 0;
        for (int index = 1; index < size; index++) {
            if (find((T)data[index], 0, index) != -1) {
                remove(index, index + 1);
            } else {
                deletedNum += 1;
            }
        }
        size -= deletedNum;
        return deletedNum;
    }

    /**
     * 对所有元素实施统一操作
     * @param consumer 对所有元素的操作
     */
    @SuppressWarnings("unchecked")
    public void traverse(Consumer<T> consumer) {
        for (int index = 0; index < size; index++) {
            consumer.accept((T)data[index]);
        }
    }

    /**
     * 返回向量中逆序相邻元素对的总数，data[i + 1] >= data[i]为一个逆序对
     * @param <T> 泛型参数必须实现Comparable接口
     * @param vector 待检测的数组
     * @return 向量中逆序相邻元素对的总数
     * 向量有序当且仅当返回0
     */
    public static <T extends Comparable<T>> int disordered(Vector<T> vector) {
        int num = 0;
        for (int i = 1; i < vector.size; i++) {
            if (vector.data[i - 1].compareTo(vector.data[i]) != -1) {
                num++;
            }
        }
        return num;
    }

    /**
     * 对于有序向量的删除重复元素的高效实现，复杂度为O(n)<br>
     * 要求实现comparable接口，不必覆写equal方法
     * @param vector 待操作的数组
     * @return 删除多少个元素
     */
    public static <T extends Comparable<T>> int uniquify(Vector<T> vector) {
        // 双指针法
        int first = 0;
        int second = 0;
        while (++second < vector.size) {
            if (vector.data[first].compareTo(vector.data[second]) != 0) {
                vector.data[++first] = vector.data[second];
            }
        }
        vector.size = first + 1;
        vector.shrink();
        return (second - 1) - first;
    }


    /**
     * 在有序向量[left, right)中查找是否存在目标元素
     * @param vector 有序向量
     * @param element 带查找的元素
     * @param left 区间左端点
     * @param right 区间右端点
     * @return 查找失败时，返回-1。若命中多个元素时，不能保证返回的rank是最大的
     */
    public static <T extends Comparable<T>>
    int binarySearch(Vector<T> vector, T element, int left, int right) {
        while (left < right) {
            int mid = (left + right) / 2;
            int result = vector.data[mid].compareTo(element);
            if (result < 0) {
                right = mid;
            } else if (result > 0) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 在有序向量[left, right)中查找是否存在目标元素<br>
     * 相比较binarySearch，该方法内部实现只判断了一次。最好情况下效率降低。
     * 但是作为补偿，在最坏情况下的表现有所提高。
     * @param vector 有序向量
     * @param element 带查找的元素
     * @param left 区间左端点
     * @param right 区间右端点
     * @return 查找失败时，返回-1。若命中多个元素时，不能保证返回的rank是最大的
     */
    public static <T extends Comparable<T>>
    int binarySearchB(Vector<T> vector, T element, int left, int right) {
        while (1 < right - left) {
            int mid = (left + right) / 2;
            if (vector.data[mid].compareTo(element) == -1) {
                right = mid;
            } else {
                left = mid;
            }
        }
        return vector.data[left].compareTo(element) == 0 ? left : -1;
    }

    /**
     * 在有序向量[left, right)中查找是否存在目标元素<br>
     * 相比较binarySearch，该方法内部实现只判断了一次。最好情况下效率降低。
     * 但是作为补偿，在最坏情况下的表现有所提高。<br>
     * @param vector 有序向量
     * @param element 带查找的元素
     * @param left 区间左端点
     * @param right 区间右端点
     * @return 有多个命中元素时，总能保证返回秩最大者；查找失败时，能够返回失败癿位置
     */
    public static <T extends Comparable<T>>
    int binarySearchC(Vector<T> vector, T element, int left, int right) {
        while (left < right) {
            int mid = (left + right) / 2;
            if (vector.data[mid].compareTo(element) == -1) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left - 1;
    }

    /**
     * 在有序向量[left, right)中查找是否存在目标元素，使用Fibonacci搜索算法
     * @param vector 有序向量
     * @param element 带查找的元素
     * @param left 区间左端点
     * @param right 区间右端点
     * @return 查找失败时，返回-1。若命中多个元素时，不能保证返回的rank是最大的
     */
    public static <T extends Comparable<T>>
    int fibonacciSearch(Vector<T> vector, T element, int left, int right) {
        Fibonacci fibonacci = new Fibonacci((right - 1) - left + 1);
        while (left < right) {
            while (right - left < fibonacci.getCachedItem()) fibonacci.prev();
            int mid = left + (int)fibonacci.getCachedItem();
            int result = vector.data[mid].compareTo(element);
            if (result < 0) {
                right = mid;
            } else if (result > 0) {
                left = mid + 1;
            } else  {
                return mid;
            }
        }
        return -1;
    }

    /**
     * 使用冒泡排序对向量的[left,right)中的元素有序化
     * @param vector 待排序的向量
     * @param left 区间左端点
     * @param right 区间右端点
     */

    public static <T extends Comparable<T>>
    void bubbleSort(Vector<T> vector, int left, int right) {
        while (++left < right) {
            boolean sorted = true;
            int rank = left;
            while (rank < right) {
                if (vector.data[rank - 1].compareTo(vector.data[rank]) > 0) {
                    swap(vector, rank - 1, rank);
                    sorted = false;
                }
                rank += 1;
            }
            if (sorted) break;
        }
    }

    /**
     * 使用冒泡排序对向量的[left,right)中的元素有序化
     * @param vector 待排序的向量
     * @param left 区间左端点
     * @param right 区间右端点
     */
    public static <T extends Comparable<T>>
    void mergeSort(Vector<T> vector, int left, int right) {
        if (right - left < 2) return;
        int mid = (left + right) / 2;
        mergeSort(vector, left, mid);
        mergeSort(vector, mid, right);
        merge(vector, left, right);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>>
    void merge(Vector<T> vector, int left, int right) {
        int mid = (left + right) / 2;
        T[] tempArray = (T[])new Object[(right - 1) - left + 1];
        int leftIndex = left;
        int rightIndex = mid;
        int index = 0;
        while (leftIndex < mid && rightIndex < right) {
            T leftElement = vector.data[leftIndex];
            T rightElement = vector.data[rightIndex];
            if (leftElement.compareTo(rightElement) <= 0) {
                tempArray[index++] = leftElement;
                leftIndex += 1;
            } else {
                tempArray[index++] = rightElement;
                rightIndex += 1;
            }
        }
        while (leftIndex < mid)
            tempArray[index++] = vector.data[leftIndex++];
        while (rightIndex < mid)
            tempArray[index++] = vector.data[rightIndex++];
        for (int rank = left; rank < right; rank++) {
            vector.data[rank] = tempArray[rank - left];
        }
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        for (int index = 0; index < size; index++) {
            stringBuilder.append(data[index].toString());
            if (index != size - 1) stringBuilder.append(", ");
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
