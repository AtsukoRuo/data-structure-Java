package cn.atsukoruo.graph;

import cn.atsukoruo.list.List;
import cn.atsukoruo.list.Vector;

import java.util.Iterator;

public class GraphAdjacencyList<VertexType, EdgeType>
    extends Graph<VertexType, EdgeType>{

    Vector<List<Edge<EdgeType>>> E;             //边集
    Vector<Vertex<VertexType>> V;               //点集

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
            Iterator<List.ListNode<Edge<EdgeType>>> iterator
                = E.get(i).iterator();

            int index = 0;
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
        for (int k = 0; k < E.get(i).size(); k++) {
            if (E.get(i).get(k).v == j) return true;
        }
        return false;
    }

    @Override
    public EType type(int i, int j) {
        for (var temp : E.get(i)) {
            if (temp.data().v == j) {
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
        return V.insert(new Vertex<>(data));
    }

    @Override
    public VertexType remove(int i) {
        //先删边(j, i);
        for (int j = 0; j < V.size(); j++) {
            if (j == i) continue;
            for (var temp : E.get(j)) {
                if (temp.data().v == j) {
                    V.get(j).outDegree -= 1;
                    E.get(j).remove(temp);
                }
            }
        }

        //再删边(i, j)
        for (int j = 0; j < E.get(i).size(); i++) {
            V.get(j).inDegree -= 1;
        }
        E.remove(i);
        n -= 1;
        return V.remove(i).data;
    }

    @Override
    public void insert(EdgeType edge, int w, int i, int j) {
        e += 1;
        E.get(i).insertAsFirst(new Edge<>(edge, w, j));
    }

    @Override
    public EdgeType remove(int i, int j) {
        e -= 1;
        return E.get(i).remove(j).data;
    }
}
