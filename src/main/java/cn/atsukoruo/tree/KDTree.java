package cn.atsukoruo.tree;


import cn.atsukoruo.list.Vector;

import java.util.function.BiFunction;
import java.util.function.Consumer;

class KDNode <T extends Comparable<T>>{
    Vector<T> data;
    KDNode<T> leftChild;
    KDNode<T> rightChild;

    int splitDirection;

    public KDNode(Vector<T> data, KDNode<T> leftChild, KDNode<T> rightChild) {
        this.data = data;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
}
public class KDTree<T extends Comparable<T>> {
    KDNode<T> root;
    int dimension;

    BiFunction<Vector<T>, Integer, Vector<T>[]> divide;
    public KDTree(Vector<T> collection,
           int dimension,
           BiFunction<Vector<T>, Integer, Vector<T>[]> divide) {
        this.divide = divide;
        this.dimension = dimension;
        root = buildKDTree(collection, 0);
    }

    private KDNode<T> buildKDTree(Vector<T> collection, int depth) {
        KDNode<T> node = new KDNode<>(collection, null, null);
        if (collection.size() > 1) {
            node.splitDirection = depth;
            Vector<T>[] collections = divide.apply(collection, node.splitDirection);
            node.leftChild = buildKDTree(collections[0], (depth + 1) % dimension);
            node.rightChild = buildKDTree(collections[1], (depth + 1) % dimension);
        }
        return node;
    }


    public void search(Vector<T> R, Consumer<Vector<T>> consumer) {
        search(root, R, consumer);
    }

    private void search(KDNode<T> v, Vector<T> R, Consumer<Vector<T>> consumer) {
        //null对应空集，自然地包含于R中
        if (R.hasContained(v.leftChild.data)) {
            consumer.accept(v.leftChild.data);
        } else if (R.intersection(v.leftChild.data)) {
            search(v.leftChild, R, consumer);
        }
        if (R.hasContained(v.rightChild.data)) {
            consumer.accept(v.rightChild.data);
        } else if (R.intersection(v.rightChild.data)) {
            search(v.rightChild, R, consumer);
        }
    }
}
