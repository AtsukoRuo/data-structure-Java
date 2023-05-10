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
}
