package cn.atsukoruo.list;

final public class Queue<T> {
    final List<T> queue = new List<>();

    /**
     * 返回队列中元素的个数
     * @return 返回队列中元素的个数
     */
    public int size() {
        return queue.size();
    }

    /**
     * 判断队列是否为空
     * @return 为空返回true
     */
    public boolean empty() {
        return queue.empty();
    }

    /**
     * 将元素入队
     * @param element 待入队的元素
     */
    public void enqueue(T element) {
        queue.insertAsLast(element);
    }

    /**
     * 返回队首元素，并删除
     * @return 返回队首元素
     */
    public T dequeue() {
        return queue.remove(queue.first());
    }

    /**
     * 引用队首元素，不删除
     * @return 返回队首元素
     */
    public T front() {
        return queue.first().data;
    }
}
