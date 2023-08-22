package cn.atsukoruo.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ListTester {

    @Test
    public void isEmptyTest() {
        Assertions.assertTrue(new List<>().empty());
    }

    @Test
    public void insertTest() {
        List<Integer> list = new List<> ();
        list.insertAsFirst(1);
        System.out.println("insertTest : " + list);
        Assertions.assertEquals(1, list.size());
    }

    @Test
    public void firstAndLastTest() {
        List<Integer> list = getSequenceList(100);
        Assertions.assertEquals(0, list.first().data);
        Assertions.assertEquals(99, list.last().data);
        list = new List<>();
        Assertions.assertEquals(null, list.last());
        Assertions.assertEquals(null, list.first());
    }

    @Test
    public void getTest() {
        List<Integer> list = getSequenceList(100);
        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i, list.get(i));
        }
        list = new List<>();
        Assertions.assertEquals(null, list.get(1));
    }

    @Test
    public void find() {
        List<Integer> list = getSequenceList(100);
        Assertions.assertEquals(null, list.find(2, 10, list.getNode(100)));
        Assertions.assertEquals(null, list.find(22, 21, list.getNode(10)));
        Assertions.assertEquals(list.getNode(1), list.find(1, 1, list.getNode(1)));
        Assertions.assertEquals(list.getNode(0), list.find(0, 13, list.getNode(11)));
        Assertions.assertEquals(list.getNode(1), list.find(1, 2, list.getNode(2)));
    }

    @Test
    void insertAsFirstTest() {
        List<Integer> list = getSequenceList(100);
        list.insertAsFirst(-100);
        Assertions.assertEquals(-100, list.first().data);
    }
    @Test
    void insertAsLastTest() {
        List<Integer> list = getSequenceList(100);
        list.insertAsLast(-100);
        Assertions.assertEquals(-100, list.last().data);
    }

    @Test
    void insertPrevTest() {
        List<Integer> list = getSequenceList(100);
        List.ListNode<Integer> targetNode = list.getNode(20);
        list.insertPrev(-1000, targetNode);
        Assertions.assertEquals(-1000, list.get(20));
        Assertions.assertEquals(19, list.get(19));
        Assertions.assertEquals(20, list.get(21));
    }

    @Test
    void insertNextTest() {
        List<Integer> list = getSequenceList(100);
        List.ListNode<Integer> targetNode = list.getNode(20);
        list.insertNext(-1000, targetNode);
        Assertions.assertEquals(-1000, list.get(21));
        Assertions.assertEquals(21, list.get(22));
    }

    @Test
    void removeTest() {
        List<Integer> list = getSequenceList(7);
        List.ListNode<Integer> targetNode = list.getNode(3);
        list.remove(targetNode);
        System.out.println("remove test : " + list);

        list.remove(1);
        System.out.println("remove test : " + list);
    }

    @Test
    void clearTest(){
        List<Integer> list = getSequenceList(100);
        list.clear();
        System.out.println("clear test : " + list);
        Assertions.assertEquals(0, list.size());
    }


    @Test
    void deduplicate() {
        List<Integer> list = new List<>();
        list.insertAsFirst(1);
        list.deduplicate();
        System.out.println("deduplicate test : " + list);

        list.insertAsFirst(1);
        list.insertAsFirst(2);
        list.insertAsFirst(3);
        list.insertAsFirst(3);
        list.insertAsFirst(3);
        list.insertAsFirst(4);
        list.insertAsFirst(5);
        list.insertAsFirst(5);
        list.insertAsFirst(5);
        list.insertAsFirst(5);

        list.deduplicate();
        System.out.println("deduplicate test : " + list);
    }

    @Test
    public void uniquify() {
        List<Integer> list = new List<>();
        list.insertAsFirst(1);
        List.uniquify(list);
        System.out.println("uniquify test : " + list);

        list.insertAsFirst(1);
        list.insertAsFirst(2);
        list.insertAsFirst(3);
        list.insertAsFirst(3);
        list.insertAsFirst(3);
        list.insertAsFirst(4);
        list.insertAsFirst(5);
        list.insertAsFirst(5);
        list.insertAsFirst(5);
        list.insertAsFirst(5);

        List.uniquify(list);
        System.out.println("uniquify test : " + list);
    }

    @Test
    public void search() {
        List<Integer> list = getSequenceList(100);
        Assertions.assertEquals(10, List.search(list, 10, 1, list.getNode(10)).data);
        Assertions.assertEquals(1, List.search(list, 1, 10, list.getNode(10)).data);
        Assertions.assertEquals(null , List.search(list, -1000, 111, list.getNode(10)));
    }
    @Test
    public void deleteTest() {
        List<Integer> list = getSequenceList(1000);
    }

    @Test
    public void getSequenceListTest() {
        System.out.println("getSequenceList" + getSequenceList(21));
    }

    @Test
    private List<Integer> getSequenceList(int size) {
        List<Integer> list = new List<>();
        for (int i = 0; i < size; i++) {
            list.insertAsLast(i);
        }
        Assertions.assertEquals(size, list.size());
        return list;
    }
}
