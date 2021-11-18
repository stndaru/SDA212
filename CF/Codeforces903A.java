package CF;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Codeforces903A {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        String result;
        int inpAmount = in.nextInt();

        for(int i = 0; i < inpAmount; i++){
            int inpVal = in.nextInt();
            result = output(inpVal);
            out.println(result);
        }

        // for(int i = 0; i < 100; i++){
        //     result = output(i);
        //     out.println(i+ ": " + result);
        // }

        out.flush();
        out.close();
    }

    public static String output(int inp){
        // String result = "";

        // if(inp % 7 == 0 || inp % 3 == 0
        //     || (inp % 7) % 3 == 0
        //     || (inp % 3) % 7 == 0
        //     || inp % 10 == 0
        //     || (inp % 10) % 3 == 0
        //     || (inp % 10) % 7 == 0
        // )
        //     return "YES";

        // return "NO";
        
        
        for(int i = 0; i < 34; i++){
            for(int j = 0; j < 34; j++){
                if(7*i + 3*j == inp)
                    return "YES";
            }
        }

        return "NO";
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;
 
        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }
 
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
 
        public int nextInt() {
            return Integer.parseInt(next());
        }
 
    }
}
