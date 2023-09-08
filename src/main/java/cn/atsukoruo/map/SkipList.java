package cn.atsukoruo.map;

import cn.atsukoruo.list.List;

import java.lang.ref.Reference;
import java.util.Random;
import java.util.function.Consumer;

public class SkipList<K extends Comparable<K>, V> extends Map<K, V> {

    final double probability;                                       //生长概率
    final List<QuadList<Entry<K, V>>> list = new List<>();         //垂直层面上的List

    public SkipList() {
        this(0.50);
    }

    /**
     * @param probability 概率必须在[0.2, 0.8]之间。若超出这个范围，那么强制设置为0.50
     */
    public SkipList(double probability) {
        if (probability < 0.2 || probability > 0.8)
            probability = 0.50;
        this.probability = probability;
    }



    static Random random = new Random(System.currentTimeMillis());


    @Override
    public boolean put(K key, V value) {
        Entry<K, V> entry = new Entry<>(key, value);
        if (list.empty()) {
            list.insertAsFirst(new QuadList<>());
        }

        List.ListNode<QuadList<Entry<K, V>>> nodeOfQuadList = list.first();
        QuadListNode<Entry<K, V>> p = nodeOfQuadList.data().first();

        //将p设置为底层的节点，且要作为key的前驱
        if ((p = skipSearch(nodeOfQuadList, p, key)) != null) {
            while (p.below != null)
                p = p.below;                                    //p到最底层
        } else {
            //此时，skipList为空。
            p = nodeOfQuadList.data().header;                   //将p设置为头哨兵
        }
        assert p != null;

        QuadList<Entry<K, V>> quadList = list.last().data();
        nodeOfQuadList = list.last();                           //跳转到底层
        QuadListNode<Entry<K, V>> newNode = quadList.insertAfterAbove(entry, p, null);      //作为p的后续

        while (random.nextInt(100) <= 100 * probability) {
            while (quadList.isValid(p) && p.above == null) {
                p = p.prev;
            }
            if (quadList.isValid(p)) {
                p = p.above;
            } else {                                            //若p为头哨兵header
                if (quadList == list.first().data()) {          //已经到达顶部，不能再向上
                    list.insertAsFirst(new QuadList<>());       //插入新的一层
                }
                p = nodeOfQuadList.getPrev().data().header;     //p设置为上一层的header
            }

            nodeOfQuadList = nodeOfQuadList.getPrev();
            quadList = nodeOfQuadList.data();                   //上升一层
            assert quadList != null;
            newNode = quadList.insertAfterAbove(entry, p, newNode);
        }
        return true;
    }

    /**
     * 获取指定key的value
     * @param key
     * @return 如果未查询到key，那么返回null
     */
    @Override
    public V get(K key) {
        if (list.empty()) {
            return null;
        }

        assert list.first() != null;
        List.ListNode<QuadList<Entry<K, V>>> nodeOfQuadList = list.first();
        QuadList<Entry<K, V>> quadList = nodeOfQuadList.data();
        //从顶部开始搜索
        Entry<K, V> entry = skipSearch(
                nodeOfQuadList,
                quadList.first(),
                key
        ).data;
        //如果key比任何节点小，那么返回就是header，此时数据域为null
        //如果key比任何节点大，那么返回就是last(),此时必须做一次特判
        return entry != null ?
                (entry.key.compareTo(key) == 0 ?
                        entry.value
                        : null)
                : null;
    }

    @Override
    public boolean remove(K key) {
        List.ListNode<QuadList<Entry<K, V>>> nodeOfQuadList = list.first();
        QuadListNode<Entry<K, V>> p = nodeOfQuadList.data().first();

        //这里判空处理了空skipList的边界情况
        if ((p = skipSearch(nodeOfQuadList, p, key)) == null
                || p.data.key.compareTo(key) != 0) {     //目标节点不存在
            return false;
        }

        //拆除对应的tower
        do {
            QuadListNode<Entry<K, V>> belowNode = p.below;
            nodeOfQuadList.data().remove(p);
            p = belowNode;
            nodeOfQuadList = nodeOfQuadList.getNext();
        } while (p != null);

        //塔顶没有key了。那么就删除塔顶
        while (!list.empty() && list.first().data().isEmpty()) {
            //由于先list.empty() 所以data()并不会返回null
            list.remove(list.first());
        }
        return true;
    }

    /**
     * 判断key是否在map中存在，
     * @param nodeOfQuadList    开始搜索的水平层，一般从顶层开始
     * @param p                 开始搜索的节点
     * @param key               待查询的key
     * @return                  查找成功返回true，否则返回false
     */
    public boolean search(
            List.ListNode<QuadList<Entry<K, V>>> nodeOfQuadList,
            QuadListNode<Entry<K, V>> p,
            K key
    ) {
        QuadListNode<Entry<K, V>> node = skipSearch(nodeOfQuadList, p, key);
        return node.prev != null && node.data.key.compareTo(key) == 0;
    }

    /**
     * 返回不大于key的节点，
     * 若key比任何QuadList中节点的key小，那么p此时为header
     * 若key比任何QuadList中节点的key大，那么p此时为last()
     * 如果key命中了，那么就返回命中节点
     * @param nodeOfQuadList    开始搜索的水平层，一般从顶层开始
     * @param p                 开始搜索的节点，不能是头哨兵，而且是开始搜索水平层中的节点
     * @param key               待查询的key
     * @return                  不大于key的节点，如果nodeOfQuadList为null或者p为null，那么返回null
     */
    protected QuadListNode<Entry<K, V>> skipSearch(
            List.ListNode<QuadList<Entry<K, V>>> nodeOfQuadList,
            QuadListNode<Entry<K, V>> p,
            K key
    ) {
        if (nodeOfQuadList == null || p == null) {
            //异常情况
            return null;
        }

        while (true) {
            //在当前水平层 从前往后查找，直至溢出至trailer或者出现更大的key
            while (p.succ != null && p.data.key.compareTo(key) <= 0) {
                p = p.succ;
            }
            p = p.prev;
            //若key比任何QuadList中节点的key小，那么p此时为header，故要判断p.prev != null
            //若key比任何QuadList中节点的key大，那么p此时为last()
            if (p.prev != null && p.data.key.compareTo(key) == 0) {
                return p;      //命中
            }
            nodeOfQuadList = nodeOfQuadList.getNext();          //跳转至下一层的链表
            if (nodeOfQuadList == null || nodeOfQuadList.data() == null) {
                //因为无法获取到List的尾哨兵，所以只能通过data() == null的方式来判断。
                //已经到达了底层，无法继续向下
                return p;
            }
            //p如果是头哨兵，那么将它设置为下一层链表的第一个元素
            //否则，直接向下即可
            p = p.prev == null ? nodeOfQuadList.data().first() : p.below;
        }
    }


    public void print() {
        if (list.empty())
            return;
        QuadList<Entry<K, V>> quadList = list.last().data();
        QuadListNode<Entry<K, V>> p = quadList.first();
        while (quadList.isValid(p)) {
            QuadListNode<Entry<K, V>> q = p;
            int count = 0;
            while (q != null) {
                q = q.above;
                count++;
            }
            Entry<K, V> entry = p.data;
            for (int i = 0; i < count; i++) {
                System.out.printf("{%-4s, %-4s}  ", entry.key, entry.value);
            }
            System.out.printf("%n");
            p = p.succ;
        }
    }
}


class QuadList<T extends Comparable<T>> {
    int size;
    QuadListNode<T> header;     //哨兵节点
    QuadListNode<T> trailer;    //哨兵节点

    public QuadList() {
        header = new QuadListNode<>();
        trailer = new QuadListNode<>();
        header.succ = trailer;
        trailer.prev = header;
    }
    /**
     * 获取节点的个数
     * @return 节点的个数
     */
    public int getSize() { return size; }

    /**
     * 判断列表是否为空
     * @return 为空返回true，否则返回false
     */
    public boolean isEmpty() {return size == 0;}

    /**
     * 获取第一个节点，如果链表此时为空，那么返回null
     * @return 第一个节点
     */
    QuadListNode<T> first() { return isEmpty() ? null : header.succ; }

    /**
     * 获取最后一个节点，如果链表此时为空，那么返回null
     * @return 最后一个节点
     */
    QuadListNode<T> last() { return isEmpty() ? null : trailer.prev; }

    public boolean isValid(QuadListNode<T> p) {
        return p != null && trailer != p && header != p;
    }

    /**
     * 删除指定节点，仅仅修改水平方向的引用，而垂直方向的引用不修改
     * 所以一般用于删除tower的情况，而不适用于删除单个节点的情况
     * @param p 要删除的节点
     * @return 被删除节点的数据
     */
    T remove(QuadListNode<T> p) {
        p.prev.succ = p.succ;
        p.succ.prev = p.prev;
        //基于可达性的垃圾回收才不会造成内存泄露
        return p.data;
    }

    /**
     * 将data作为p的后继，b的上邻插入
     * @param data 待插入的数据
     * @param p 新节点的前驱，不能是trailer
     * @param b 新节点的下驱，
     * @return 返回新节点
     */
    QuadListNode<T> insertAfterAbove(
            T data,
            QuadListNode<T> p,
            QuadListNode<T> b
    ) {
        size++;
        return p.insertAsSuccAbove(data, b);
    }

    /**
     * 遍历所有节点，并依次实施指定操作
     */
    void traverse(Consumer<QuadListNode<T>> consumer) {

    }
}

class QuadListNode<T> {
    T data;
    QuadListNode<T> prev;
    QuadListNode<T> succ;
    QuadListNode<T> above;
    QuadListNode<T> below;

    public QuadListNode(
            T data,
            QuadListNode<T> prev,
            QuadListNode<T> succ,
            QuadListNode<T> above,
            QuadListNode<T> below
    ) {
        this.data = data;
        this.prev = prev;
        this.succ = succ;
        this.above = above;
        this.below = below;
    }

    public QuadListNode() {
        this(null, null, null, null, null);
    }

    /**
     * 插入新的节点，当前节点的后继，节点node的上驱
     * @param data
     * @param node
     * @return
     */
    QuadListNode<T> insertAsSuccAbove(
            T data,
            QuadListNode<T> node
    ) {
        QuadListNode<T> newNode = new QuadListNode<>(data, this, succ, null, node);
        if (node != null)
            node.above = newNode;
        this.succ.prev = newNode;
        this.succ = newNode;
        return newNode;
    }
}