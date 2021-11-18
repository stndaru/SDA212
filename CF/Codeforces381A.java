package CF;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Codeforces381A {
    private static InputReader in;
    private static PrintWriter out;


    static int[] find(int N, int[] a) {

        int[] result = new int[2];

        int start = 0;
        int end = N-1;

        int order = 0;

        while(start != end){

            // left is greater than right, add left then increment left
            if(a[start] >= a[end]){
                if(order % 2 == 0) result[0] += a[start];
                else result[1] += a[start];
                start++;
            }
            
            //right is greater than left, add right then increment right
            else{
                if(order % 2 == 0) result[0] += a[end];
                else result[1] += a[end];
                end--;
            }

            order++;
        }

        if(order % 2 == 0) result[0] += a[start];
        else result[1] += a[start];
        

        return result;
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of a
        int[] a = new int[N];
        for (int i = 0; i < N; ++i) {
            a[i] = in.nextInt();
        }
        
        // TODO: implement method multiplyMod(int, int, int[]) to get the answer
        int[] ans = find(N, a);
        out.print(ans[0] + " " + ans[1]);

        // don't forget to close/flush the output
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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