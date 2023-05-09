package cn.atsukoruo.list;

import java.util.function.Consumer;

final public class List<T> {
    /**
     * @param <U> 这样保证使用List<T>类型时，ListNode的数据类型为T
     */
    final public static class ListNode<U> {
        U data;
        private ListNode<U> prev;
        private ListNode<U> next;

        private ListNode(U data, ListNode<U> prev, ListNode<U> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
        private ListNode(U data) {
            this.data = data;
        }
        private ListNode() {}

        /**
         * 在节点的前驱处添加节点，此节点不能是header哨兵节点
         * @param data 要插入的数据
         * @return 返回新增节点
         */
        private ListNode<U> insertAsPerv(U data) {
            ListNode<U> temp = new ListNode<U>(data, this.prev, this);
            this.prev.next = temp;
            this.prev = new ListNode<U>(data);
            return temp;
        }

        /**
         * 在节点的后继处添加节点，此节点不能是trailer哨兵节点
         * @param data 要插入的数据
         * @return 返回新增节点
         */
        private ListNode<U> insertAsNext(U data) {
            ListNode<U> temp = new ListNode<U>(data, this, this.next);
            this.next.prev = temp;
            this.next = temp;
            return temp;
        }
    }

    private ListNode<T> header;        //头哨兵
    private ListNode<T> trailer;       //尾哨兵

    private int size;               //记录有效节点的数量，不包括header与trailer


    private void init() {
        header = new ListNode<T>();
        trailer = new ListNode<T>();
        header.next = trailer;
        trailer.prev = header;
    }

    /**
     * 复制从p开始的n个节点（包括p）。获得一个新的List
     * @param p 起始位置
     * @param n 要复制的个数，要满足 rank(p) + n - 1 < size
     */
    private void copyNodes(ListNode<T> p, int n) {
        init();
        while (n-- > 0) {
            insertAsLast(p.data);
            p = p.next;     //注意，在Java中的对象赋值仅仅改变所指向的对象，并不是深拷贝
        }
        //无需更新size，insertAsLast会自动更新size
    }

    /**
     * 获取一个空链表
     */
    public List() {
        init();
    }

    /**
     * 复制从p开始的n个节点（包括p）。获得一个新的List
     * @param p 起始位置
     * @param n 要复制的个数，要满足 rank(p) + n - 1 < size
     */
    public List(ListNode<T> p, int n) {
        copyNodes(p, n);
    }

    /**
     * 复制整个链表
     * @param list 要被复制的链表
     */
    public List(List<T> list) {
        copyNodes(list.first(), list.size);
    }

    /**
     * 从指定rank处开始复制
     * @param list 要被复制的链表
     * @param rank 被复制的第一个节点的rank
     * @param n 要复制的个数
     */
    public List(List<T> list, int rank, int n) {
        copyNodes(list.getNode(rank), n);
    }

    /**
     * 返回节点个数
     * @return 返回节点个数
     */
    public int size() { return size; }

    /**
     * 列表是否为空
     * @return 列表为空返回true
     */
    public boolean empty() { return size <= 0; }

    /**
     * 返回链表的首个节点
     * @return 如果列表为空，那么返回null。否则返回首节点
     */
    public ListNode<T> first() {
        return empty() ? null : header.next;
    }

    /**
     * 获取秩为rank的节点中的数据
     * @param rank 指定秩为rank的节点 0 <= rank < size
     * @return 秩为rank节点的数据。如果列表为空，则直接返回null
     */
    public T get(int rank) {
        return empty() ? null : getNode(rank).data;
    }

    /**
     * 获取秩为rank的节点
     * @param rank 指定秩为rank的节点 0 <= rank < size
     * @return 秩为rank节点的数据。如果列表为空，则直接返回null
     */
    private ListNode<T> getNode(int rank) {
        ListNode<T> p = first();
        if (p == null) return null;
        while (rank-- > 0) p = p.next;
        return p;
    }
    /**
     * 在无序列表内节点p的n个前驱中（不包括p），找到等于e的最后者
     * 基于equal方法来判等
     * @param data 要查找的数据
     * @param n p的前n个节点
     * @param p 要搜索的节点p
     * @return 查找成功，返回秩最大的匹配节点。查找失败返回null
     */
    public ListNode<T> find(T data, int n, ListNode<T> p) {
        while (n-- > 0) {
            p = p.prev;
            if (data.equals(p.data)) {
                return p;
            }
        }
        return null;
    }

    /**
     * 在列表头部插入数据
     */
    public ListNode<T> insertAsFirst(T data) {
        size++;
        return header.insertAsNext(data);
    }

    /**
     * 在列表尾部插入数据
     */
    public ListNode<T> insertAsLast(T data) {
        size++;
        return trailer.insertAsPerv(data);
    }
    /**
     * 在指定节点的前驱处插入数据
     */
    public ListNode<T> insertPrev(T data, ListNode<T> p) {
        size++;
        return p.insertAsPerv(data);
    }
    /**
     * 在指定节点的后继处插入数据
     */
    public ListNode<T> insertNext(T data, ListNode<T> p) {
        size++;
        return p.insertAsNext(data);
    }

    /**
     * 删除指定节点
     * @param p 要被删除的节点
     * @return 要被删除节点的数据
     */
    public T remove(ListNode<T> p) {
        T data = p.data;
        p.prev.next = p.next;
        p.next.prev = p.prev;
        size--;
        return data;
    }

    /**
     * 清空列表中的所有节点
     * @return
     */
    public int clear() {
        int oldSize = size;
        while (size > 0) {
            remove(header.next);
        }
        return oldSize;
    }

    /**
     * 对于无序向量的唯一化
     * @return 删除节点的个数
     */
    public int deduplicate() {
        ListNode<T> p = header.next;
        int oldSize = size;
        while (p != trailer) {
            for (ListNode<T> q = p.next; q != trailer; q = q.next) {
                if (p.data.equals(q.data)) {
                    remove(q);
                }
            }
            p = p.next;
        }
        return oldSize - size;
    }

    /**
     * 遍历操作
     * @param consumer
     */
    public void traverse(Consumer<T> consumer) {
        for (ListNode<T> p = header.next; p != trailer; p = p.next) {
            consumer.accept(p.data);
        }
    }

    /**
     * 对于有序向量的唯一化
     * @return 删除节点的个数
     */
    public static <U extends Comparable<U>>
    int uniquify(List<U> list) {
        int oldSize = list.size;
        ListNode<U> p = list.first();
        for (ListNode<U> q = p.next; q != list.trailer; q = p.next) {
            if (q.data.equals(p.data)) {
                list.remove(q);
            } else {
                p = q;
            }
        }
        return oldSize - list.size;
    }

    /**
     * 从p的前n个前驱中（不包括p）查找不大于data的最后者
     * @param data 待查找的数据
     * @param n p的前n个前驱中
     * @param p 从p开始查找
     * @return 找到不大于e的最后者，若查找失败返回左边界的前驱
     */
    public static <T extends Comparable<T>>
    ListNode<T> search(List<T> list, T data, int n, ListNode<T> p) {
        // 这里取等号的原因是要考虑左边界的前驱。而与左边界前驱比较是没有任何意义的
        while (n-- >= 0) {
            p = p.prev;
            if (p.data.compareTo(data) <= 0) break;
        }
        return p;
    }

    /**
     * 使用插入排序对链表有序化
     * @param list 待排序的链表
     * @param p 起始位置。这个节点必须是在list中的
     * @param n 对于起始位置p的n个元素排序（包括p）
     */
    public static <T extends Comparable<T>>
    void insertSort(List<T> list, ListNode<T> p, int n) {
        for (int rank = 0; rank < n; rank++) {
            list.insertNext(p.data, search(list, p.data, rank, p));
            p = p.next;
            list.remove(p.prev);        //因为在链表中插入了一个新的节点，因此要删除这多余的节点
        }
    }

    /**
     * 从有序链表指定节点p处开始，选出从位置p的n个元素中（包括p）的最大者
     * @param list 待选择的链表
     * @param p 起始位置 这个节点必须是在list中的
     * @param n 对于起始位置p的n个元素进行选择（包括p）
     * @return 返回最大的节点
     */
    public static <T extends Comparable<T>>
    ListNode<T> selectMax(List<T> list, ListNode<T> p, int n) {
        ListNode<T> max = p;
        for (ListNode<T> currentNode = p.next; n > 1; n--, currentNode = currentNode.next) {
            if (currentNode.data.compareTo(max.data) > 0) {
                max = currentNode;
            }
        }
        return max;
    }

    /**
     * 使用选择排序对链表有序化
     * @param list 待排序的链表
     * @param p 起始位置。这个节点必须是在list中的
     * @param n 对于起始位置p的n个元素排序（包括p）
     */
    public static <T extends Comparable<T>>
    void selectionSort(List<T> list, ListNode<T> p, int n) {
        ListNode<T> left = p.prev;
        ListNode<T> right = p;
        for (int i = 0; i < n; i++) {
            right = right.next;
        }
        //待排序区间(left, right)
        while (n > 1) {
            ListNode<T> max = selectMax(list, left.next, n);
            list.insertPrev(list.remove(max), right);       //一定要注意删除操作，否则链表中有两个相同的节点
            right = right.prev;
            n--;
        }
    }

    /**
     * 将src中的节点有序合并到des，并且在src中删除这些节点。用于归并排序中
     * @param des 目标链表
     * @param p 起始位置
     * @param n 合并的范围设置为n个节点（包括节点p）
     * @param src 源链表
     * @param q 起始位置
     * @param m 合并的范围设置为m个节点（包括节点q）
     * @return 通过参数p返回合并后链表的新的起始位置
     */
    public static <T extends Comparable<T>>
    void merge(List<T> des, ListNode<T> p, int n, List<T> src, ListNode<T> q, int m) {
        //在归并排序中 this = L && rank(p) + n = rank(q)
        ListNode pp = p.prev;
        while (m > 0) {
            if (n > 0 && p.data.compareTo(q.data) <= 0) {
                p = p.next;
                if (q == p) {
                    //这里处理的是 des = src 而且要合并的两个链表的范围是重合的
                    break;
                }
                n--;
            } else {
                q = q.next;
                des.insertPrev(src.remove(q.prev), p);
                m--;
            }
        }
    }

    /**
     * 使用递归排序对链表有序化
     * @param list 待排序的链表
     * @param p 起始位置。这个节点必须是在list中的
     * @param n 对于起始位置p的n个元素排序（包括p）
     */
    public static <T extends Comparable<T>>
    void mergeSort(List<T> list, ListNode p, int n) {
        if (n < 2) return;
        int m = n / 2;
        ListNode q = p;
        for (int i = 0; i < m; i++) q = q.next;
        mergeSort(list, p, m);
        mergeSort(list, q,n - m);
        merge(list, p, m, list, q, n - m);
    }
}
