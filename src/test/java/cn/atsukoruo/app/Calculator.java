package cn.atsukoruo.app;

import cn.atsukoruo.list.Stack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Calculator {

    private final  static int NUM = 9;
    private final static char[][] priority = {
                /* +    -    *    /    ^    !    (    )   \0 */
        /* + */  {'>', '>', '<', '<', '<', '<', '<', '>', '>'},
        /* - */  {'>', '>', '<', '<', '<', '<', '<', '>', '>'},
        /* * */  {'>', '>', '>', '>', '<', '<', '<', '>', '>'},
        /* / */  {'>', '>', '>', '>', '<', '<', '<', '>', '>'},
        /* ^ */  {'>', '>', '>', '>', '>', '<', '<', '>', '>'},
        /* ! */  {'>', '>', '>', '>', '>', '>', ' ', '>', '>'},
        /* ( */  {'<', '<', '<', '<', '<', '<', '<', '=', ' '},
        /* ) */  {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        /* \0 */ {'<', '<', '<', '<', '<', '<', '<', ' ', '='}
    };
    //' '对应的非法情况

    final static Map<Character, Integer> map = new HashMap<>();
    static {
        map.put('+', 0);
        map.put('-', 1);
        map.put('*', 2);
        map.put('/', 3);
        map.put('^', 4);
        map.put('!', 5);
        map.put('(', 6);
        map.put(')', 7);
        map.put('\0', 8);
    }

    /**
     * 对中缀表达式求值，并将其转换为后缀表达式
     * @param expression 中缀表达式，
     * @param RPN 重新指向后缀表达式
     * @return 中缀表达式的求值结果
     */
    public static double calculate(String expression, String RPN) {
        expression = expression + "\0";
        Stack<Double> operand = new Stack<>();
        Stack<Character> operator = new Stack<>();
        operator.push('\0');
        int index = 0;
        StringBuilder builder = new StringBuilder();
        while (!operator.empty()) {
            char c = expression.charAt(index);
            if (isDigit(c)) {
                int oldIndex = index;
                while (isDigit(expression.charAt(index)))
                    index += 1;
                Double number = readNumber(expression, oldIndex, (index - 1) - oldIndex + 1);
                operand.push(number);
                builder.append(operand.top());
            } else if (c == ' '){
                index++;
            } else {
                switch (priority[map.get(operator.top())][map.get(c)]) {
                    case '<' :
                        operator.push(c);
                        index++;
                        break;
                    case '>' :
                        c = operator.pop();
                        builder.append(c);
                        if (c == '!') {
                            double opr = operand.pop();
                            operand.push(calculate(opr, 0, c));
                        } else {
                            double right = operand.pop();
                            double left = operand.pop();
                            operand.push(calculate(left, right, c));
                        }
                        break;
                    case '=' :
                        operator.pop();
                        index++;
                        break;
                }
            }
        }
        if (RPN != null)
            RPN = builder.toString();
        return operand.top();
    }

    private static double calculate(double left, double right, char c) {
        return switch (c) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            case '/' -> left / right;
            case '^' -> Math.pow(left, right);
            default -> 0.0;
        };
    }
    public static Double readNumber(String number, int begin, int length) {
        int position = 0;
        double ans = 0;
        for (int i = begin, j = 0; j < length; i++, j++) {
            if (number.charAt(i) == '.') {
                position = i;
                continue;
            }
            ans = ans * 10 + number.charAt(i) - '0';
        }
        return ans * Math.pow(10, -(length - 1 - position));
    }

    private static boolean isDigit(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }


    /**
     * 对后缀表达式求值
     * @param expression
     * @return
     */
    public static double calculate(String expression) {
        Stack<Double> operator = new Stack<>();
        for (int index = 0; index < expression.length(); index++) {
            char c = expression.charAt(index);
            if (isDigit(c)) {
                int oldIndex = index;
                while (isDigit(expression.charAt(index)))
                    index += 1;
                Double number = readNumber(expression, oldIndex, (index - 1) - oldIndex + 1);
                operator.push(number);
                while (isDigit(expression.charAt(index)))
                    index += 1;
            } else if (c == ' '){
                index += 1;
            } else {
                Double right = operator.pop();
                Double left = operator.pop();
                operator.push(calculate(left, right, c));
                index += 1;
            }
        }
        return operator.top();
    }

    private static class Tester {
        public static void main(String[] args) {
            System.out.println(readNumber("159.83", 0, 6));
            System.out.println(calculate("1 1 + 2 * 3 /") + "");
            System.out.println(calculate("1 + 2 * 5", null));
        }
    }
}
