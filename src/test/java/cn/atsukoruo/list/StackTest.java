package cn.atsukoruo.list;

import cn.atsukoruo.DAO.DataObject;
import cn.atsukoruo.list.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.xml.crypto.Data;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackTest {
    private Stack<DataObject> stack;

    @BeforeEach
    private void init() {
        stack = new Stack<>();
        DataObject.reset();
    }

    @Test
    public void testPopInEmptyStack() {
        assert stack.pop() == null : "为空测试失败";
    }

    @Test
    void testTopInEmptyStack() {
        assert stack.top() == null : "为空测试失败";
    }

    @Test
    void testPush() {
        stack.push(new DataObject());
        stack.push(new DataObject());
        stack.push(new DataObject());
        int id = stack.top().id;
        assert id == 2 : id + "";
    }
    @Test
    void testPopInEmptyStack2() {
        stack.push(new DataObject());
        stack.pop();
        stack.pop();
        assert stack.pop() == null : "为空测试失败";
    }

    @Test
    void testRunFor100s() {
        long beginTime = System.currentTimeMillis();
        Random rand = new Random(beginTime);
        int size = 0;
        int id = -1;
        while (System.currentTimeMillis() - beginTime < 10000) {
            switch (rand.nextInt(3)) {
                case 1: case 2:
                    size += 1;
                    id++;
                    stack.push(new DataObject());
                    break;
                case 3:
                    if (size > 0) size -= 1;
                    stack.pop();
                    break;
            }
        }
        assert stack.size() == size : "stack size : %d, size : %d".formatted(stack.size(), size);
        assert stack.top().id == id : "stack id : %d, id : %d".formatted(stack.top().id, id);
    }

    @Test
    void testRunFor100s2() {
        long beginTime = System.currentTimeMillis();
        Random rand = new Random(beginTime);
        int size = 0;
        int id = -1;
        while (System.currentTimeMillis() - beginTime < 10000) {
            switch (rand.nextInt(3)) {
                case 1:
                    size += 1;
                    id++;
                    stack.push(new DataObject());
                    break;
                case 2: case 3:
                    if (size > 0) size -= 1;
                    stack.pop();
                    break;
            }
        }
        assert stack.size() == size : "stack size : %d, size : %d".formatted(stack.size(), size);
        assert stack.top().id == id : "stack id : %d, id : %d".formatted(stack.top().id, id);
    }

}


