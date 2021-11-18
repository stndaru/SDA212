package UTS;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        int[] a = {5,3,12,6,2};
        processThis("a + b * c + d * e * f + g");
    }

    static void processThis(String strinput) {
        StringTokenizer tok = new StringTokenizer(strinput," ");
        Stack<String> s = new Stack<>();
        s.push("");
        while (tok.hasMoreTokens()) {
           String t = tok.nextToken();
           if (isOperator(t)) {
              while (!isHigher(t, s.peek())) 
                 System.out.print(s.pop());
                 s.push(t);
           } else System.out.print(t);
        }
        while (!s.isEmpty())
            System.out.print(s.pop());
        System.out.println();
     }
    
    static boolean isOperator(String t) {
        return t.equals("+") || t.equals("*");
    }
    
    static boolean isHigher(String x, String y) {
        if (y.equals("")) return true;
        if (orderOf(x) > orderOf( y )) return true;
        return false;
    }
    
    static int orderOf(String op) {
        if (op.equals("+")) return 1;
        if (op.equals("*")) return 2;
        return -99;
    }

}

    //////////////////////////////////////////
