package cn.atsukoruo.graph;

@FunctionalInterface
public interface PriorityUpdater<VertexType, EdgeType> {
    /**
     * 更新图中的优先级
     * @param graph 正在遍历的图
     * @param s 当前选取的节点，即优先级最高
     * @param w 当前节点的邻居
     */
    void update(Graph<VertexType, EdgeType> graph, int s, int w);

}