package cn.atsukoruo.list;


public class Stack<T> {
    List<T> stack = new List<T>();
    public int size() {
        return stack.size();
    }
    public boolean empty() {
        return stack.empty();
    }

    /**
     * 将数据插入到栈顶
     * @param element 待插入的数据
     */
    public void push(T element) {
        stack.insertAsLast(element);
    }

    /**
     * 删除并返回栈顶对象
     * @return 返回栈顶对象。栈为空 返回null
     */
    public T pop() {
        return stack.empty() ? null : stack.remove(stack.last());
    }

    /**
     * 引用栈顶对象
     * @return 栈顶对象。栈为空 返回null
     */
    public T top() {
        List.ListNode<T> element = stack.last();
        return element == null ? null : element.data();
    }

    /**
     * 查询栈中是否有该元素，要求元素重载equal方法
     * @param element 待查找的元素
     * @return 查找成功返回true
     */
    public boolean find(T element) {
        return stack.find(element, stack.size(), stack.trailer) == null;
    }

    public void clear() {
        stack = new List<T>();
    }
    @Override
    public String toString() {
        return stack.toString();
    }
}
