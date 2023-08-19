package cn.atsukoruo.list;

import cn.atsukoruo.util.Fibonacci;

import java.awt.color.CMMException;
import java.util.Random;
import java.util.function.Consumer;

/**
 * 本类一开始是为无序向量设计的。但是后面又有实现有序向量的需求。
 * 最佳方法是设计一个接口，让无序向量与有序向量分别实现这个接口
 * 但是这样编码工作量巨大，本类仅仅是为了学习数据结构而练手的，不应该考虑这么多软件工程上的设计
 * 因此对于有序向量的方法，一律用static方法 + 自限定类型<T extends Comparable<T>>来实现的
 */
final public class Vector<T> {
    private static final int DEFAULT_CAPACITY = 47;     //默认初始容量
    private int size;                                   //当前有效元素的数量
    private int capacity;                               //容量
    T[] data;                                           //数据

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
     * @return 返回当前Vector的规模
     */
    public int size() {
        return size;
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
     * 对容量进行两倍的扩容
     * 这里的扩容采用了懒惰策略只有在的确即将发生溢出时，才不得不将容量加倍
     */
    @SuppressWarnings("unchecked")
    private void expand() {
        if (size < capacity) return;            //未发生上溢，不进行扩容
        capacity = capacity << 1;
        if (capacity < DEFAULT_CAPACITY) capacity = DEFAULT_CAPACITY;
        Object[] oldData = data;
        data = (T[])new Object[capacity];
        System.arraycopy(oldData,0, data, 0, size);
    }

    /**
     * 当满足当前容量大于默认容量的两倍并且装载因子小于25%时
     * 进行收缩，容量进行减半
     */
    private void shrink() {
        if (capacity < DEFAULT_CAPACITY * 2         //避免收缩到DEFAULT_CAPACITY以下
        || size * 4 > capacity)                     //以25%作为装填因子的下限
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
        if (index >= size) throw new ArrayIndexOutOfBoundsException();
        return data[index];
    }

    /**
     * 将秩为index的元素设置为data
     * @param data 要更新的数据
     * @param index 被修改元素的秩
     */
    public void set(T data, int index) {
        if (index >= size) throw new ArrayIndexOutOfBoundsException();
        this.data[index] = data;
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
     * @param right 待置乱区间的右端点（不包括）
     */
    private void unsort(int left, int right) {
        for (int i = right; i > left; i--) {
            swap(this, i - 1, rand.nextInt(i) + left);
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
     * 在[left, right)中查找指定的元素，并返回其rank（如果成功的话），要求对象实现equals
     * @param element   待查找元素
     * @param left 待删除区间的左端
     * @param right 待查找区间的右端
     * @return 如果查找成功，返回元素的rank，如果有多个匹配的元素，那么返回最小的rank
     * 查找失败返回 left - 1
     */
    public int find(T element, int left, int right) {
        while ((left < right--) && !(element.equals(data[right])))
            ;
        return right;
    }

    /**
     * 在整个向量中查找指定的元素，并返回其rank（如果成功的话）
     * @param element 待查找元素
     * @return 如果查找成功，返回元素的rank，如果有多个匹配的元素，那么返回最小的rank
     * 查找失败返回 -1
     */
    public int find(T element) {
        return find(element, 0, size);
    }


    /**
     * 在指定的rank处插入指定元素
     * @param element 待插入的元素
     * @param rank 插入的位置，要满足0 <= rank <= size。
     * @return 返回参数rank，插入失败返回-1
     */
    public int insert(T element, int rank) {
        if (rank > size) return -1;
        expand();
        System.arraycopy(data, rank, data, rank + 1, size - rank);
        data[rank] = element;
        size += 1;
        return rank;
    }

    /**
     * 作为末元素插入
     * @param element 待插入的元素
     * @return 新插入元素的秩
     */
    public int insert(T element) {
        return insert(element, size);
    }

    /**
     * 删除向量区间[left, right)中的元素
     * @param left 待删除区间的左端点
     * @param right 待删除区间的右端点（不包括）
     * @return 返回删除元素的个数, 即right - left
     */
    public int remove(int left, int right) {
        if (left < 0 || right > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int length = size - right;
        System.arraycopy(data, right, data, left, length);
        size -= (right - left);
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
        int oldSize = size;
        for (int index = 1; index < size; index++) {
            if (find((T)data[index], 0, index) != -1) {
                remove(index);
                index -= 1;
            }
        }
        return oldSize - size;
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
     * 对于有序向量的删除重复元素的高效实现，复杂度为O(n)<br>
     * 要求实现comparable接口，不必覆写equal方法
     * @param vector 待操作的数组
     * @return 删除多少个元素
     */
    public static <U extends Object & Comparable<U>>
    int uniquify(Vector<U> vector) {
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
        return second - first;
    }

    /**
     * 返回向量[left, right)中不大于element的最大元素的rank
     * 后置条件 data[rank] <= element < data[rank + 1]
     */
    public static <T extends Object & Comparable<T>>
    int search(Vector<T> vector, T element) {
        return search(vector, element, 0, vector.size());
    }

    /**
     * 返回向量[left, right)中不大于element的最大元素的rank
     * @param element 带查找元素
     * @param left 区间左端点
     * @param right 区间右端点
     * @return 查找失败返回
     */
    public static <T extends Object & Comparable<T>>
    int search(Vector<T> vector, T element, int left, int right) {
        while (left < right-- && vector.data[right].compareTo(element) > 0)
            ;
        return right;
    }

    /**
     * 在有序向量[left, right)中查找是否存在目标元素
     * @param vector 有序向量
     * @param element 带查找的元素
     * @param left 区间左端点
     * @param right 区间右端点
     * @return 查找失败时，返回-1。若命中多个元素时，不能保证返回的rank是最大的
     */
    public static <T extends Object & Comparable<T>>
    int binarySearch(Vector<T> vector, T element, int left, int right) {
        while (left < right) {
            int mid = (left + right) >> 1;
            int result = vector.data[mid].compareTo(element);
            if (result > 0) {
                right = mid;
            } else if (result < 0) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public static <T extends  Object & Comparable<T>>
    int binarySearch(Vector<T> vector, T element) {
        return binarySearch(vector, element, 0, vector.size);
    }

    /**
     * 使用冒泡排序对向量的[left,right)中的元素有序化
     * @param vector 待排序的向量
     * @param left 区间左端点
     * @param right 区间右端点
     */
    public static <T extends Object & Comparable<T>>
    Vector<T> bubbleSort(Vector<T> vector, int left, int right) {
        while (left < right) {                //自左向右，逐一检查各对相邻元素
            boolean sorted = true;              //整体有序的标志
            int rank = left + 1;
            while (rank < right) {
                if (vector.data[rank - 1].compareTo(vector.data[rank]) > 0) {   //发现逆序对
                    swap(vector, rank - 1, rank);       //通过交换使得局部有序
                    sorted = false;
                }
                rank += 1;
            }
            if (sorted) break;
            right -= 1;
        }
        return vector;
    }
    public static <T extends Object & Comparable<T>>
    Vector<T> bubbleSort(Vector<T> vector) {

        return bubbleSort(vector, 0, vector.size);
    }
    /**
     * 使用冒泡排序对向量的[left,right)中的元素有序化
     * @param vector 待排序的向量
     * @param left 区间左端点
     * @param right 区间右端点
     */
    public static <T extends Object & Comparable<T>>
    Vector<T> mergeSort(Vector<T> vector, int left, int right) {
        if (right - left < 2) return vector;       //元素个数小于2个
        int mid = (left + right) / 2;
        mergeSort(vector, left, mid);
        mergeSort(vector, mid, right);
        merge(vector, left, right);
        return vector;
    }
    @SuppressWarnings("unchecked")
    private static <T extends Object & Comparable<T>>
    void merge(Vector<T> vector, int left, int right) {
        int mid = (left + right) / 2;
        T[] tempArray = (T[])new Object[right - left];
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
        while (leftIndex < mid) tempArray[index++] = vector.data[leftIndex++];
        while (rightIndex < right) tempArray[index++] = vector.data[rightIndex++];
        System.arraycopy(tempArray, 0, vector.data, left, right - left);
    }
    public static <T extends Object & Comparable<T>>
    Vector<T> mergeSort(Vector<T> vector) {
        return mergeSort(vector, 0, vector.size);
    }

    public static <T> Vector<T> of(T[] elements) {
        Vector<T> ret = new Vector<>(elements.length);
        for (T element : elements) {
            ret.data[ret.size++] = element;
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        for (int index = 0; index < size; index++) {
            stringBuilder.append(data[index] == null ? "null" : data[index].toString());
            if (index != size - 1) stringBuilder.append(", ");
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}
