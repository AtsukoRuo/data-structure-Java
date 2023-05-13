package cn.atsukoruo.app;

import cn.atsukoruo.list.Stack;

public class ConvertNumber {
    final static char[] digits = {
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'A', 'B',
        'C', 'D', 'E', 'F', 'G', 'H',
    };

    public static String convert(int n, int base) {
        Stack<Character> stack = new Stack<Character>();
        //convertRecursion(stack, n, base);
        convertIteration(stack, n, base);
        StringBuilder builder = new StringBuilder();
        while (!stack.empty()) {
            builder.append(stack.pop());
        }
        return builder.toString();
    }

    private static void convertRecursion(Stack<Character> stack, int n, int base) {
        if (n > 0) {
            stack.push(digits[n % base]);
            convertRecursion(stack , n / base, base);
        }
    }

    private static void convertIteration(Stack<Character> stack, int n, int base) {
        while (n > 0) {
            stack.push(digits[n % base]);
            n /= base;
        }
    }
    static private class Tester {
        public static void main(String[] args) {
            System.out.println(convert(12345, 8));
        }
    }
}
