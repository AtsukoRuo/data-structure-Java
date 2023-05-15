package cn.atsukoruo.graph;

import cn.atsukoruo.list.List;
import cn.atsukoruo.list.Queue;
import cn.atsukoruo.list.Stack;
import cn.atsukoruo.util.MutableInteger;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;


//
public abstract class Graph<VertexType, EdgeType> {
    private void reset() {
        for (int i = 0; i < n; i++) {
            status(i, VStatus.UNDISCOVERED);
            dTime(i, -1);
            fTime(i, -1);
            parent(i, -1);
            priority(i, Integer.MAX_VALUE);
            for (int j = 0; j < n; j++) {
                if (exists(i, j)) {

                }
            }
        }
    }

    protected int n;        //顶点集的规模
    protected int e;        //边集的规模

    /**
     * 获取顶点集的规模
     * @return 顶点集的规模
     */
    public int getN() { return n; }

    /**
     * 获取边集的规模
     * @return 边集的规模
     */
    public int getE() { return e; }

    public abstract VertexType vertex(int i);


    public abstract int inDegree(int i);

    public abstract int outDegree(int i);

    public abstract VStatus status(int i);

    public abstract void status(int i, VStatus status);

    public abstract int dTime(int i);

    public abstract void dTime(int i, int time);

    public abstract int fTime(int i);

    public abstract void fTime(int i, int time);

    public abstract int parent(int i);

    public abstract void parent(int i, int parent);

    public abstract int priority(int i);

    public abstract void priority(int i, int priority);


    /**
     * 获取指定节点的迭代器，通过该迭代器可以遍历该节点的所有邻居（有向边的终点）
     * @param i 指定索引为i的节点
     * @return 该节点的迭代器
     */
    public abstract Iterator<Integer> getIteratorOfNode(int i);

    /**
     * 判断节点i与节点j是否为邻接节点
     * @param i 节点的索引，满足 0 <= i < n
     * @param j 节点的索引，满足 0 <= j < n
     * @return 为邻接节点返回true
     */
    public abstract boolean exists(int i, int j);
    /**
     * 返回边(i,j)的类型，前置条件为边(i,j)存在
     * @param i 边的起点
     * @param j 边的终点
     * @return 边(i,j)的类型的引用
     */
    public abstract EType type(int i, int j);

    public abstract void type(int i, int j, EType type);
    /**
     * 返回边(i,j)中的数据，前置条件为边(i,j)存在
     * @param i 边的起点
     * @param j 边的终点
     * @return 边(i,j)中的数据的引用
     */
    public abstract EdgeType edge(int i, int j);
    /**
     * 返回边(i,j)中的权重，前置条件为边(i,j)存在
     * @param i 边的起点
     * @param j 边的终点
     * @return 边(i,j)中的权重的引用
     */
    public abstract int weight(int i, int j);
    /**
     * 插入一个节点
     * @param data 节点的数据域
     * @return 新插入节点的索引
     */
    public abstract int insert(VertexType data);
    /**
     * 删除指定节点i，以及相关联的边。
     * 这会使调用者所拥有的全部索引失效
     * @param i 待删除的节点
     * @return 删除节点的数据域
     */
    public abstract VertexType remove(int i);

    /**
     * 插入一条新边(i, j)。若边已存在，那么什么也不做
     * @param edge 要插入的新边
     * @param w 边的权重
     * @param i 边的起始点
     * @param j 边的终点
     */
    public abstract void insert(EdgeType edge, int w, int i, int j);
    /**
     * 删除边（i, j）。
     * @param i 边的起点
     * @param j 边的终点
     * @return 若边不存在则返回null，否则返回边的数据
     */
    public abstract EdgeType remove(int i, int j);


    /**
     * 从节点s开始广度优先搜索。
     * 并修改节点的dTime、parent状态以及某些边的type状态
     * 可以生成一个森林
     * @param s 搜索的起始节点
     */
    public void bfs(int s) {
        int k = s;
        MutableInteger clock = new MutableInteger();
        do {
            if (status(k) == VStatus.UNDISCOVERED) {
                bfs(s, clock);
            }
        } while (s != (k = (k + 1) % n));
    }

    private void bfs(int s, MutableInteger clock) {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(s);
        status(s,VStatus.DISCOVERED);
        while (!queue.empty()) {
            s = queue.dequeue();
            dTime(s, clock.getAndAdd(1));
            for (var iterator = getIteratorOfNode(s);
                 iterator.hasNext();) {
                int x = iterator.next();
                if (status(x) == VStatus.UNDISCOVERED) {
                    status(x, VStatus.DISCOVERED);
                    queue.enqueue(x);
                    type(s, x, EType.TREE);
                    parent(x, s);
                } else {
                    type(s, x, EType.CROSS);
                }
            }
            status(s,VStatus.VISITED);
        }
    }

    public void dfs(int s) {
        int v = s;
        status(s, VStatus.DISCOVERED);
        MutableInteger clock = new MutableInteger();
        do {
            if (status(v) == VStatus.UNDISCOVERED)
                dfs(v, clock);
        } while (s != (v = (v + 1) & n));
    }

    private void dfs(int s, MutableInteger clock) {
        dTime(s,clock.getAndAdd(1));
        status(s, VStatus.DISCOVERED);
        for (var iterator = getIteratorOfNode(s);
             iterator.hasNext();) {
            int v = iterator.next();
            switch (status(v)) {
                case DISCOVERED :
                    type(s, v, EType.BACKWARD);
                    break;
                case VISITED :
                    type(s, v, (dTime(s) < dTime(v) ? EType.FORWARD : EType.CROSS));
                    break;
                case UNDISCOVERED :
                    type(s,v,EType.TREE);
                    parent(v, s);
                    dfs(v, clock);
                    break;
            }
        }
        status(s, VStatus.VISITED);
        clock.add(1);
        fTime(s, clock.value());
    }

    /**
     * 对图进行拓扑排序，并以栈的形式返回，栈中元素为节点的数据
     * @return 如果图是有向有环图，那么返回一个栈，该栈中无任何元素
     */
    public Stack<VertexType> topologicalSort(int s) {
        Stack<VertexType> stack = new Stack<>();
        status(s,VStatus.UNDISCOVERED);
        int t = s;
        do {
            if (status(t) == VStatus.UNDISCOVERED) {
                if (!tSort(t, stack)) {
                    stack.clear();
                    break;
                }
            }
        } while (s != (t = (t + 1) % n));
        return stack;
    }

    private boolean tSort(int s, Stack<VertexType> stack) {
        status(s, VStatus.DISCOVERED);
        for (var iterator = getIteratorOfNode(s);
            iterator.hasNext();) {
            int t = iterator.next();
            switch (status(t)) {
                case DISCOVERED:
                    //在有向图中发现回溯边
                    return false;
                case UNDISCOVERED:
                    if (!tSort(t, stack))
                        return false;

                //visited的节点对于接下来拓扑排序无影响
            }
        }
        status(s, VStatus.VISITED);
        stack.push(vertex(s));
        return true;
    }

    /**
     * 对无向图进行双连通域分解。
     * @param s 从节点s开始dfs
     * @return 返回关节点的列表
     */
    public List<Integer> bcc(int s) {
        //这里使用fTime代替hca
        reset();
        MutableInteger clock = new MutableInteger();
        List<Integer> ans = new List<>();
        int v = s;
        do {
            if (status(v) == VStatus.UNDISCOVERED) {
                bcc(s, ans, clock);
            }
        } while (s != (v = (v + 1) % n));
        return ans;
    }

    private void bcc(int s, List<Integer> list, MutableInteger clock) {
        status(s, VStatus.DISCOVERED);
        dTime(s, clock.value());
        fTime(s, clock.value());
        clock.add(1);
        for (var iterator = getIteratorOfNode(s);
             iterator.hasNext();) {
            int v = iterator.next();
            switch (status(v)) {
                case UNDISCOVERED :
                    parent(v, s);
                    bcc(s, list, clock);
                    if (fTime(v) < dTime(s)) {
                        fTime(s, Math.min(fTime(s), fTime(v)));
                    } else {
                        list.insertAsFirst(s);
                    }
                    break;
                case DISCOVERED:
                    //发现一条回溯边
                    if (v != parent(s)) {
                        fTime(s, Math.min(fTime(s), dTime(v)));
                    }
                    break;
            }
        }
    }

    public void pfs(int s,
                    PriorityUpdater<VertexType, EdgeType> updater) {
        reset();
        int v = s;
        do {
            if (status(v) == VStatus.UNDISCOVERED) {
                //此时v是当前优先级最高的节点
                status(v, VStatus.DISCOVERED);
                priority(v, 0);
                parent(v, -1);
                while (true) {
                    //更新节点v以及它的邻居的优先级
                    for (var iterator = getIteratorOfNode(v);
                        iterator.hasNext();) {
                        updater.update(this, v, iterator.next());
                    }

                    //从全图中选取一个优先级最高的节点
                    for (int shortest = Integer.MAX_VALUE, w = 0; w < n; w++) {
                        if (status(w) == VStatus.UNDISCOVERED) {
                            if (priority(w) < shortest) {
                                v = w;
                                shortest = priority(v);
                            }
                        }
                    }
                    if (status(v) == VStatus.VISITED) break;
                    status(v, VStatus.VISITED);
                    type(parent(v), v, EType.TREE);
                }
            }
        } while (s != (v = (v + 1) % n));
    }

    public void prim(int s) {
        pfs(s, new PriorityUpdater<VertexType, EdgeType>() {
            @Override
            public void update(Graph<VertexType, EdgeType> graph, int s, int w) {
                if (graph.status(s) == VStatus.UNDISCOVERED) {
                    graph.priority(w,
                        Math.min(graph.priority(w), graph.weight(s, w)));
                }
            }
        });
    }

    public void Dijkstra(int s) {
        pfs(s, new PriorityUpdater<VertexType, EdgeType>() {
            @Override
            public void update(Graph<VertexType, EdgeType> graph, int s, int w) {
                if (graph.status(w) == VStatus.UNDISCOVERED) {
                    graph.priority(w,
                        Math.min(graph.priority(w), graph.priority(s) + graph.weight(s, w)));
                }
            }
        });
    }
}

//顶点状态
enum VStatus {
    UNDISCOVERED,
    DISCOVERED,
    VISITED;
}

//边在遍历树中所属的类型
enum EType {
    UNDETERMINED,
    TREE,
    CROSS,
    FORWARD,
    BACKWARD;
}

class Vertex<T> {
    T data;             //数据域
    int inDegree;       //入度
    int outDegree;      //出度
    VStatus status;     //状态
    int dTime;          //时间戳
    int fTime;          //时间戳
    int parent;         //在遍历树中的父节点
    int priority;       //在遍历树中的优先级

    private void init() {
        status = VStatus.UNDISCOVERED;
        dTime = -1;
        fTime = -1;
        parent = -1;
        priority = Integer.MAX_VALUE;
    }
    Vertex(T data) {
        this.data = data;
        init();
    }
    Vertex() {
        init();
    }
}

class Edge<T> {
    T data;             // 数据域
    int weight;     // 权重
    EType type;         // 边的类型

    int v;              // 关联节点，用于邻接表中

    Edge(T data, int weight) {
        this.data = data;
        this.weight = weight;
        type = EType.UNDETERMINED;
    }

    Edge(T data, int weight, int v) {
        this(data, weight);
        this.v = v;
    }
}

