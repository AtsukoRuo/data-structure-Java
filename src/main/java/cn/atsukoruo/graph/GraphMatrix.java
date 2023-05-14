package cn.atsukoruo.graph;

import cn.atsukoruo.list.Vector;

/**
 * @param <VertexType> 顶点数据域的类型
 * @param <EdgeType>    边数据域的类型
 */
public class GraphMatrix<VertexType, EdgeType>
    extends Graph<VertexType, EdgeType> {
    Vector<Vertex<VertexType>> V;       //顶点集
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
    public Integer dTime(int i) { return V.get(i).dTime; }


    @Override
    public Integer fTime(int i) { return V.get(i).fTime; }

    @Override
    public Integer parent(int i) { return V.get(i).parent; }

    @Override
    public Integer priority(int i) { return V.get(i).priority; }


    @Override
    public int nextNeighbour(int i, int j) {
        while ((j > -1) && !exists(i, --j))
            ;
        return j;
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
    public EdgeType edge(int i, int j) {
        return E.get(i).get(j).data;
    }


    @Override
    public Integer weight(int i, int j) {
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
                E.get(j).remove(i);
                V.get(j).outDegree -= 1;
            }
        }

        //再删(i, j)边
        for (int j = 0; j < n; j++) {
            if (exists(i, j)) {
                V.get(j).inDegree -= 1;
            }
        }
        E.remove(i);
        n -= 1;            //节点规模减1
        return V.remove(i).data;
    }


    @Override
    public void insert(EdgeType edge, int w, int i, int j) {
        if (exists(i,j)) return;
        E.get(i).insert(new Edge<>(edge, w), j);

        //更新边计数与关联顶点的度数
        e++;
        V.get(i).outDegree += 1;
        V.get(j).inDegree += 1;
    }


    @Override
    public EdgeType remove(int i, int j) {
        if (!exists(i,j)) return null;
        EdgeType edge = E.get(i).get(j).data;
        E.get(i).set(null, i);
        //更新边计数与关联顶点的度数
        e--;
        V.get(i).outDegree--;
        V.get(j).inDegree--;
        return edge;
    }

}

