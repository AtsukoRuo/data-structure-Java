package cn.atsukoruo.graph;

import cn.atsukoruo.list.List;
import cn.atsukoruo.list.Vector;

import java.util.Iterator;

public class GraphAdjacencyList<VertexType, EdgeType>
    extends Graph<VertexType, EdgeType>{

    Vector<List<Edge<EdgeType>>> E;             //边集
    Vector<Vertex<VertexType>> V;               //点集

    public GraphAdjacencyList() {
        V = new Vector<Vertex<VertexType>>();
        E = new Vector<List<Edge<EdgeType>>>();
    }
    @Override
    public VertexType vertex(int i) {
        return V.get(i).data;
    }

    @Override
    public int inDegree(int i) {
        return V.get(i).inDegree;
    }

    @Override
    public int outDegree(int i) {
        return V.get(i).outDegree;
    }

    @Override
    public VStatus status(int i) {
        return V.get(i).status;
    }

    @Override
    public void status(int i, VStatus status) {
        V.get(i).status = status;
    }

    @Override
    public int dTime(int i) {
        return V.get(i).dTime;
    }

    @Override
    public void dTime(int i, int time) {
        V.get(i).dTime = time;
    }

    @Override
    public int fTime(int i) {
        return V.get(i).fTime;
    }

    @Override
    public void fTime(int i, int time) {
        V.get(i).fTime = time;
    }

    @Override
    public int parent(int i) {
        return V.get(i).parent;
    }

    @Override
    public void parent(int i, int parent) {
        V.get(i).parent = parent;
    }

    @Override
    public int priority(int i) {
        return V.get(i).priority;
    }

    @Override
    public void priority(int i, int priority) {
        V.get(i).priority = priority;
    }

    @Override
    public Iterator<Integer> getIteratorOfNode(int i) {
        return new Iterator<Integer>() {
            final Iterator<List.ListNode<Edge<EdgeType>>> iterator = E.get(i).iterator();
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public Integer next() {
                return iterator.next().data().v;
            }
        };
    }


    @Override
    public boolean exists(int i, int j) {
        Iterator<Integer> iterator = getIteratorOfNode(i);
        while (iterator.hasNext()) {
            if (iterator.next() == j) return true;
        }
        return false;
    }

    @Override
    public EType type(int i, int j) {
        for (var temp : E.get(i)) {
            if (temp.data().v == j) {           //v是关联节点的编号
                return temp.data().type;
            }
        }
        return null;
    }

    @Override
    public void type(int i, int j, EType type) {
        for (var temp : E.get(i)) {
            if (temp.data().v == j) {
                temp.data().type = type;
                break;
            }
        }
    }

    @Override
    public EdgeType edge(int i, int j) {
        for (var temp : E.get(i)) {
            if (temp.data().v == j) {
                return temp.data().data;
            }
        }
        return null;
    }

    @Override
    public int weight(int i, int j) {
        for (var temp : E.get(i)) {
            if (temp.data().v == j) {
                return temp.data().weight;
            }
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public int insert(VertexType data) {
        n += 1;
        E.insert(new List<Edge<EdgeType>>());
        return V.insert(new Vertex<>(data));
    }

    @Override
    public VertexType remove(int i) {
        //先删边(j, i);
        for (int j = 0; j < V.size(); j++) {
            if (j == i) continue;
            for (var temp : E.get(j)) {
                if (temp.data().v == i) {
                    V.get(j).outDegree -= 1;
                    E.get(j).remove(temp);
                } else if (temp.data().v > i) {
                    //保证关联节点的语义
                    temp.data().v -= 1;
                }
            }
        }

        //再删边(i, j)
        for (int j = 0; j < E.get(i).size(); j++) {
            V.get(j).inDegree -= 1;
        }
        E.remove(i);
        n -= 1;
        return V.remove(i).data;
    }

    @Override
    public void insert(EdgeType edgeData, int w, int i, int j) {
        e += 1;
        E.get(i).insertAsLast(new Edge<>(edgeData, w, j));
    }

    @Override
    public EdgeType remove(int i, int j) {
        e -= 1;
        return E.get(i).remove(j).data;
    }

    @Override
    public void print() {
        for (int i = 0; i < n; i++) {
            System.out.printf("%-4d", i);
            for (List.ListNode<Edge<EdgeType>> node : E.get(i)) {
                Edge<EdgeType> e = node.data();
                System.out.printf("{v: %d, w: %d, type: %s}   ", e.v, e.weight, e.type);
            }
            System.out.printf("%n");
        }
        System.out.printf("%n");
    }
}
