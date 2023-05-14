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
    public Integer dTime(int i) {
        return V.get(i).dTime;
    }

    @Override
    public Integer fTime(int i) {
        return V.get(i).fTime;
    }

    @Override
    public Integer parent(int i) {
        return V.get(i).parent;
    }

    @Override
    public Integer priority(int i) {
        return V.get(i).priority;
    }

    @Override
    public int nextNeighbour(int i, int j) {
        for (int k = 0; k < E.get(i).size(); k++) {
            int temp = E.get(i).get(j).v;
            if (temp < j) return temp;
        }
        return -1;
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
        return E.get(i).get(j).type;
    }

    @Override
    public EdgeType edge(int i, int j) {
        return E.get(i).get(j).data;
    }

    @Override
    public Integer weight(int i, int j) {
        return E.get(i).get(j).weight;
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
        E.get(i).insertAsFirst(new Edge<>(edge, w));
    }

    @Override
    public EdgeType remove(int i, int j) {
        e -= 1;
        return E.get(i).remove(j).data;
    }
}
