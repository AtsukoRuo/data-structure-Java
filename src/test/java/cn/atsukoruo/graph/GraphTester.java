package cn.atsukoruo.graph;

import cn.atsukoruo.list.List;
import cn.atsukoruo.list.Stack;
import cn.atsukoruo.list.Vector;
import org.junit.jupiter.api.Test;

public class GraphTester {
    private void graphTest(Graph graph) {
        System.out.println("insert vertex");
        int a = graph.insert(null);
        int b = graph.insert(null);
        int c = graph.insert(null);
        int d = graph.insert(null);
        graph.print();

        System.out.println("insert edge");
        graph.insert(null, 7, b, a);
        graph.insert(null, 3, a, c);
        graph.insert(null, 4, c, a);
        graph.insert(null, 9, b, c);
        graph.insert(null, 2, b, d);
        graph.insert(null, 5, d, b);
        graph.print();

        System.out.println("insert vertex");
        int e = graph.insert(null);
        graph.print();
        graph.insert(null, 1, e ,e);

        System.out.println("remove vertex");
        graph.remove(c);
        graph.print();

        System.out.println("remove edge");
        graph.remove(1, 0);
        graph.print();
        System.out.printf("%n");
    }

    @Test
    public void graphMatrixTest() {
        System.out.println("---------------graphMatrixTest------------");
        graphTest(new GraphMatrix());
    }

    @Test
    public void graphAdjacencyListTest() {
        System.out.println("---------------graphAdjacencyListTest------------");
        graphTest(new GraphAdjacencyList());
    }

    @Test
    public void BFSTest() {
        System.out.println("---------------BFSTest------------");
        Graph<Character, Object> graph = new GraphAdjacencyList<>();
        int a = graph.insert('a');
        int b = graph.insert('b');
        int c = graph.insert('c');
        int d = graph.insert('d');
        int e = graph.insert('e');
        int f = graph.insert('f');
        int g = graph.insert('g');
        int s = graph.insert('s');

        graph.insert(null, 0, a, e);
        graph.insert(null, 0, a, c);
        graph.insert(null, 0, e, f);
        graph.insert(null, 0, e, g);
        graph.insert(null, 0, g, f);
        graph.insert(null, 0, g, b);
        graph.insert(null, 0, c, b);
        graph.insert(null, 0, d, b);
        graph.insert(null, 0, s, a);
        graph.insert(null, 0, s, d);
        graph.insert(null, 0, s, c);

        graph.bfs(s, (o) -> {System.out.print(o + " ");});
        System.out.printf("%n");
        graph.print();
    }

    @Test
    public void DFSTest() {
        System.out.println("---------------DFSTest------------");
        Graph<Character, Object> graph = new GraphAdjacencyList<>();
        int a = graph.insert('a');
        int b = graph.insert('b');
        int c = graph.insert('c');
        int d = graph.insert('d');
        int e = graph.insert('e');
        int f = graph.insert('f');
        int g = graph.insert('g');

        graph.insert(null, 0, a, b);
        graph.insert(null, 0, a, c);
        graph.insert(null, 0, a, f);
        graph.insert(null, 0, b, c);
        graph.insert(null, 0, c, a);
        graph.insert(null, 0, d, a);
        graph.insert(null, 0, d, e);
        graph.insert(null, 0, f, g);
        graph.insert(null, 0, e, f);
        graph.insert(null, 0, g, c);
        graph.insert(null, 0, g, a);

        graph.dfs(a, (o) -> {System.out.print(o + " ");});
        System.out.printf("%n");
        graph.print();

    }

    @Test
    public void topologicalSortTest() {
        System.out.println("---------------topologicalSortTest------------");
        Graph<Character, Object> graph = new GraphAdjacencyList<>();
        int a = graph.insert('a');
        int b = graph.insert('b');
        int c = graph.insert('c');
        int d = graph.insert('d');
        int e = graph.insert('e');
        int f = graph.insert('f');

        graph.insert(null, 0, a, c);
        graph.insert(null, 0, a, d);
        graph.insert(null, 0, c, d);
        graph.insert(null, 0, b, c);
        graph.insert(null, 0, c, e);
        graph.insert(null, 0, c, f);
        graph.insert(null, 0, e, f);

        Stack<Character> s = graph.topologicalSort(f);
        while (!s.empty()) {
            System.out.print(s.pop() + " ");
        }
        System.out.printf("%n");
    }

    @Test
    public void BCCTest() {
        System.out.println("---------------BCCTest------------");
        Graph<Character, Object> graph = new GraphAdjacencyList<>();
        int a = graph.insert('a');
        int b = graph.insert('b');
        int c = graph.insert('c');
        int d = graph.insert('d');
        int e = graph.insert('e');
        int f = graph.insert('f');
        int g = graph.insert('g');
        int h = graph.insert('h');
        int i = graph.insert('i');
        int j = graph.insert('j');

        graph.insert(null, 0, a, b);
        graph.insert(null, 0, b, a);

        graph.insert(null, 0, b, c);
        graph.insert(null, 0, c, b);

        graph.insert(null, 0, c, d);
        graph.insert(null, 0, d, c);

        graph.insert(null, 0, d, e);
        graph.insert(null, 0, e, d);

        graph.insert(null, 0, e, g);
        graph.insert(null, 0, g, e);

        graph.insert(null, 0, g, d);
        graph.insert(null, 0, d, g);

        graph.insert(null, 0, g, f);
        graph.insert(null, 0, f, g);

        graph.insert(null, 0, c, h);
        graph.insert(null, 0, h, c);

        graph.insert(null, 0, i, a);
        graph.insert(null, 0, a, i);

        graph.insert(null, 0, j, a);
        graph.insert(null, 0, a, j);

        graph.insert(null, 0, i, j);
        graph.insert(null, 0, j, i);

        graph.insert(null, 0, a, h);
        graph.insert(null, 0, h, a);

        List<Integer> ans = graph.bcc(a);
        for (List.ListNode<Integer> value : ans) {
            System.out.print(value.data() + " ");
        }
        System.out.printf("%n");
    }

    @Test
    public void DijkstraTest() {
        System.out.println("---------------DijkstraTest------------");
        Graph<Character, Object> graph = new GraphAdjacencyList<>();
        int a = graph.insert('a');
        int b = graph.insert('b');
        int c = graph.insert('c');
        int d = graph.insert('d');
        int e = graph.insert('e');
        int f = graph.insert('f');
        int g = graph.insert('g');
        int s = graph.insert('s');

        graph.insert(null, 14, s, c);
        graph.insert(null, 15, s, f);
        graph.insert(null, 9, s, a);

        graph.insert(null, 5, c, f);
        graph.insert(null, 18, c, b);
        graph.insert(null, 30, c, d);

        graph.insert(null, 25, a, b);

        graph.insert(null, 2, b, d);

        graph.insert(null, 11, d, e);
        graph.insert(null, 16, d, g);

        graph.insert(null, 6, e, g);
        graph.insert(null, 6, e, b);

        graph.insert(null, 40, f, g);
        graph.insert(null, 20, f, d);

        for (Integer i : graph.Dijkstra(s)) {
            System.out.print(i + " ");
        }
        System.out.printf("%n");
    }

    @Test
    public void PrimTest() {
        System.out.println("---------------PrimTest------------");
        Graph<Character, Object> graph = new GraphAdjacencyList<>();
        int a = graph.insert('a');
        int b = graph.insert('b');
        int c = graph.insert('c');
        int d = graph.insert('d');
        int e = graph.insert('e');
        int f = graph.insert('f');
        int g = graph.insert('g');
        int h = graph.insert('h');

        graph.insert(null, 6, a, d);
        graph.insert(null, 4, a, b);
        graph.insert(null, 7, a, g);
        graph.insert(null, 4, b, a);
        graph.insert(null, 12, b, c);
        graph.insert(null, 12, c, b);
        graph.insert(null, 9, c, d);
        graph.insert(null, 1, c, e);
        graph.insert(null, 2, c, f);
        graph.insert(null, 10, c, h);
        graph.insert(null, 6, d, a);
        graph.insert(null, 2, d, g);
        graph.insert(null, 13, d, e);
        graph.insert(null, 9, d, c);
        graph.insert(null, 13, e, d);
        graph.insert(null, 11, e, g);
        graph.insert(null, 1, e, c);
        graph.insert(null, 5, e, f);
        graph.insert(null, 8, e, h);
        graph.insert(null, 5, f, e);
        graph.insert(null, 2, f, c);
        graph.insert(null, 7, f, h);
        graph.insert(null, 7, g, a);
        graph.insert(null, 2, g, d);
        graph.insert(null, 11, g, e);
        graph.insert(null, 14, g, h);
        graph.insert(null, 8, h, e);
        graph.insert(null, 7, h, f);
        graph.insert(null, 10, h, c);
        graph.insert(null, 14, h, g);

        graph.prim(a);
        System.out.printf("%n");
    }
}
