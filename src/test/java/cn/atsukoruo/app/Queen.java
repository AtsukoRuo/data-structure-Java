package cn.atsukoruo.app;

import cn.atsukoruo.list.Stack;

import java.awt.desktop.QuitEvent;

public class Queen {
    int x, y;
    public Queen(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        Queen queen = (Queen) obj;
        return this.x == queen.x ||
            this.y == queen.y ||
            this.x + this.y == queen.x + queen.y ||
            this.x - this.y == queen.x - queen.y;
    }

    public static void main(String[] args) {
        placeQueens(8);
    }


    /**
     * y轴
     * ^
     * |
     * |
     * ------>   x轴
     * @param N N皇后
     */

    public static void placeQueens(int N) {
        Stack<Queen> queenStack = new Stack<>();
        Queen q = new Queen(0, 0);      //表示当前要放置的Queen
        do {
            if (queenStack.size() == N || q.x == N) {   //回溯
                if (queenStack.size() == N)
                    System.out.println(queenStack); //找到一个解
                q = queenStack.pop();
                q.x++;
            } else {
                while (q.x < N && !queenStack.find(q)) { //尝试找到一个合适的位置
                    q.x++;
                }
                if (q.x < N) {              //可以放置
                    queenStack.push(q);
                    q = new Queen(0, q.y + 1);
                }
            }
        } while (q.x < N || q.y > 0 );      //当回溯到(0, N)时退出
    }

    @Override public String toString() {
        return "(%d, %d)".formatted(x, y);
    }
}
