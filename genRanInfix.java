import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * @author Erving
 * @version final
 */
public class testRan {

    /**
     * 存了字母表，用于后面的随机选取以生成测试表达式
     */
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private String op = "+-*/";
    private String wrongExp = "     abcdefghijklmnopqrstuvwxyz++++++++++----------**********//////////(((((((((())))))";
    Random ran = new Random();

    private String exp = "";

    /**
     * 生成正确的随机中缀表达式，由文法分析的递归而来
     */
    private void genRExp() {

        switch (ran.nextInt(3)) { // 随机选一个数
            case 0:
                char alp = alphabet.charAt(ran.nextInt(25));
                exp = exp + alp;
                break;

            case 1:
                exp = exp + "(";
                genRExp();
                exp = exp + ")";
                break;

            case 2:
                genRExp();
                exp = exp + op.charAt(ran.nextInt(3));
                genRExp();
                break;

            // 加上后面两个代码，可能会导致：StackOverflowError在程序栈空间耗尽时抛出，通常是深度递归导致（你可以通过-Xss参数增加栈的大小）
            // case 3:
            // genRExp();
            // break;

            // case 4:
            // exp = exp + "(";
            // genRExp();
            // exp = exp + op.charAt(ran.nextInt(3));
            // genRExp();
            // exp = exp + ")";
            // break;

        }
    }

    /**
     * 生成错误的中缀表达式，从字母表以及操作符表中随机生成
     */
    private void genWExp() {

        int leng = ran.nextInt(100);
        while (leng > 0) {
            char c = wrongExp.charAt(ran.nextInt(wrongExp.length() - 1));
            exp += c;
            leng--;
        }

    }

    /**
     * 把只有一个操作数的随机中缀表达式去掉
     * 
     * @param exp
     * @return boolean
     */
    private boolean alpSingle(String exp) {
        int i = 0;
        int alpCount = 0;

        while (i < exp.length()) {
            char c = exp.charAt(i);

            if (c >= 'a' && c <= 'z')
                alpCount++;
            i++;
        }
        if (alpCount == 1 || alpCount == 0)
            return true;
        else
            return false;
    }

    /**
     * 判断是否是操作符
     * 
     * @param c
     * @return boolean
     */
    private boolean isOP(char c) {
        String s = "+-*/";
        int i = 0;
        while (i < 4) {
            if (c == s.charAt(i))
                return true;
            i++;

        }
        return false;

    }

    /**
     * 判断括号两边的符号是否合法
     * 
     * @param c1
     * @param kuoHao
     * @param c2
     * @return boolean
     */
    private boolean BracketleftRight(char c1, char kuoHao, char c2) {
        // char op='+'|'-'|'*'|'/';
        if (kuoHao == '(' && isOP(c1) && c1 == '(' && c2 != ')' && !isOP(c2)) {
            return true;
        } else if (kuoHao == ')' && c1 != '(' && !isOP(c1) && isOP(c2) && c2 == ')') {
            return true;
        } else
            return false;

    }

    /**
     * 对中缀表达式进行查错处理，并打印结果
     * 
     * @param exp
     */
    private void Error(String exp) {

        int len = exp.length();
        int i = 1;
        char c0 = exp.charAt(0);
        int leftBracket = 0, rightBracket = 0;
        if (c0 == ')' || c0 == '+' || c0 == '-' || c0 == '/' || c0 == '*')
            System.out.println("第1处错误，第一个字符应该为字母或者左括号");
        while (i < len) {
            char c = exp.charAt(i);

            switch (c) {
                case '(':
                    leftBracket++;
                    if (i != len - 1)
                        if (BracketleftRight(exp.charAt(i - 1), '(', exp.charAt(i + 1)) == false)
                            System.out.println("第" + i + "处左括号两侧不正确，左侧只能出现【操作符】或者【左括号】，右侧只能出现【操作数】或者【左括号】");
                case ')':
                    rightBracket++;
                    if (i != len - 1)
                        if (BracketleftRight(exp.charAt(i - 1), ')', exp.charAt(i + 1)) == false)
                            System.out.println("第" + i + "处右括号两侧不正确，左侧只能出现【操作数】或者【右括号】，右侧只能出现【操作符】或者【右括号】");
                default:

                    // if(i!=len-1)
                    if (exp.charAt(i - 1) != '(' && exp.charAt(i) != ')') {
                        char c3 = exp.charAt(i - 1);
                        char c4 = exp.charAt(i);
                        if (c3 > 96 && c3 < 123 && isOP(c4) || c4 > 96 && c4 < 123 && isOP(c3)) {

                        } else {
                            System.out.println("第" + i + "处出现连续两个操作符或操作数");
                        }

                    }
            }

            i++;
        }
        if (leftBracket > rightBracket)
            System.out.println("左右括号不匹配，左多于右");
        if (leftBracket < rightBracket)
            System.out.println("左右括号不匹配，右多于左");

    }

    public static void main(String[] args) {
        // javac Test.java
        // 运行： java Test a b c 传递参数a，b，c

        // int n = Integer.parseInt(args[0]);
        // char c = args[1].charAt(0);
        int n = 1;
        char c = 'r';

        System.out.println("生成的" + n + "个算术表达式如下：");
        System.out.println("（正确的可在ranRTest.txt中查看，错误的在ranWTest.txt中查看）");

        if (c == 'r') { // 生成正确随机的表达式
            try {

                PrintWriter pw = new PrintWriter("./ranRTest.txt");
                pw.flush();
                testRan test = new testRan();
                // int count=0;

                for (int i = 0; i < n; i++) {
                    test.exp = "";
                    test.genRExp();
                    if (test.alpSingle(test.exp)) {
                        i -= 1;
                        continue;
                    } else
                        System.out.println(test.exp);
                    pw.print(test.exp + ";");

                }

                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (c == 'w') { // 生成错误的随机中缀表达式
            Random ran = new Random();

            try {
                PrintWriter pout = new PrintWriter("./ranWTest.txt");
                pout.flush();
                testRan te = new testRan();
                while (n > 0) {
                    te.genWExp();
                    n--;
                    pout.print(te.exp + ";");
                    System.out.println(te.exp + ";");
                    te.Error(te.exp);
                    te.exp = "";
                }

                pout.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        } else {
            System.out.println("Input Error! Please clearly Read the instruction and Check!");
        }

    }
}
