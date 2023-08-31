package cn.atsukoruo.graph;

import cn.atsukoruo.list.Vector;

import java.util.Iterator;

/**
 * @param <VertexType> 顶点数据域的类型
 * @param <EdgeType>    边数据域的类型
 */
public class GraphMatrix<VertexType, EdgeType>
    extends Graph<VertexType, EdgeType> {
    Vector<Vertex<VertexType>> V;               //顶点集
    Vector<Vector<Edge<EdgeType>>> E;         //边集（邻接矩阵）

    public GraphMatrix() {
        V = new Vector<Vertex<VertexType>>();
        E = new Vector<Vector<Edge<EdgeType>>>();
    }

    @Override
    public VertexType vertex(int i) { return V.get(i).data; }

    @Override
    public int inDegree(int i) { return V.get(i).inDegree; }

    @Override
    public int outDegree(int i) { return V.get(i).outDegree; }

    @Override
    public VStatus status(int i) { return V.get(i).status; }

    @Override
    public void status(int i, VStatus status) {
        V.get(i).status = status;
    }

    @Override
    public int dTime(int i) { return V.get(i).dTime; }

    @Override
    public void dTime(int i, int time) {
        V.get(i).dTime = time;
    }


    @Override
    public int fTime(int i) { return V.get(i).fTime; }

    @Override
    public void fTime(int i, int time) {
        V.get(i).fTime = time;
    }

    @Override
    public int parent(int i) { return V.get(i).parent; }

    @Override
    public void parent(int i, int parent) {
        V.get(i).parent = parent;
    }

    @Override
    public int priority(int i) { return V.get(i).priority; }

    @Override
    public void priority(int i, int priority) {
        V.get(i).priority = priority;
    }

    @Override
    public Iterator<Integer> getIteratorOfNode(int i) {
        return new Iterator<Integer>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index != n;
            }
            @Override
            public Integer next() {
                while (!exists(i, index))
                    index += 1;
                return index++;
            }
        };
    }


    @Override
    public boolean exists(int i, int j) {
        return E.get(i).get(j) != null;
    }


    @Override
    public EType type(int i, int j) {
        return E.get(i).get(j).type;
    }

    @Override
    public void type(int i, int j, EType type) {
        E.get(i).get(j).type = type;
    }


    @Override
    public EdgeType edge(int i, int j) {
        return E.get(i).get(j).data;
    }


    @Override
    public int weight(int i, int j) {
        return E.get(i).get(j).weight;
    }


    @Override
    public int insert(VertexType data) {
        for (int j = 0; j < n; j++)
            E.get(j).insert(null);
        n++;
        E.insert(new Vector<Edge<EdgeType>>(n, n));
        return V.insert(new Vertex<VertexType>(data));
    }


    @Override
    public VertexType remove(int i) {
        //先删(j, i)边
        for (int j = 0; j < n; j++) {
            if (exists(j, i)) {
                V.get(j).outDegree -= 1;
            }
            E.get(j).remove(i);
        }
        n -= 1;            //节点规模减1，必须在这里更新，否则删除(i,j)边时会越界

        //再删(i, j)边
        for (int j = 0; j < n; j++) {
            if (exists(i, j)) {
                V.get(j).inDegree -= 1;
            }
        }
        E.remove(i);

        return V.remove(i).data;
    }


    @Override
    public void insert(EdgeType edge, int w, int i, int j) {
        if (exists(i,j))
            return;
        E.get(i).set(new Edge<>(edge, w), j);

        //更新边计数与关联顶点的度数
        e++;
        V.get(i).outDegree += 1;
        V.get(j).inDegree += 1;
    }


    @Override
    public EdgeType remove(int i, int j) {
        if (!exists(i,j))
            return null;
        EdgeType edge = E.get(i).get(j).data;
        E.get(i).set(null, j);
        //更新边计数与关联顶点的度数
        e--;
        V.get(i).outDegree--;
        V.get(j).inDegree--;
        return edge;
    }

    public void print() {
        //左边是出边，上边是入边
        System.out.format("    ");
        for (int i = 0; i < n; i++) {
            System.out.printf("%-4d", i);
        }
        System.out.printf("%n");
        int index = 0;
        for (Vector<Edge<EdgeType>> edges : E) {
            System.out.printf("%-4d", index++);
            for (Edge<EdgeType> edge : edges) {
                System.out.printf("%-4s", edge != null ? String.valueOf(edge.weight) : "");
            }
            System.out.printf("%n");
        }
        System.out.printf("%n");
    }
}

