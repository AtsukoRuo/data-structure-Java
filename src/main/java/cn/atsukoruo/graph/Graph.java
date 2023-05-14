package cn.atsukoruo.graph;

//
public abstract class Graph<VertexType, EdgeType> {
    private void reset() {

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

    public abstract Integer dTime(int i);

    public abstract Integer fTime(int i);

    public abstract Integer parent(int i);

    public abstract Integer priority(int i);

    /**
     * 返回节点i的一个邻接节点，并且邻接节点的索引小于j
     * @param i 返回节点i的一个邻接节点
     * @param j 返回的邻接节点的索引小于j
     * @return 如果未查询到，则返回-1。
     */
    public abstract int nextNeighbour(int i, int j);
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
    public abstract Integer weight(int i, int j);
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
    Integer dTime;          //时间戳
    Integer fTime;          //时间戳
    Integer parent;         //在遍历树中的父节点
    Integer priority;       //在遍历树中的优先级

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
    Integer weight;     // 权重
    EType type;         // 边的类型

    int v;              // 关联节点，用于邻接表中

    Edge(T data, int weight) {
        this.data = data;
        this.weight = weight;
        type = EType.UNDETERMINED;
    }
}