import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Test5 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        Tester first;
        Tester[] data = new Tester[5];

        // first = new Tester(5);
        // data[0] = first;
        // first = new Tester(7);
        // System.out.println(first.getData());
        // System.out.println(data[0].getData());
        // data[1] = first;
        // System.out.println(data[1].getData());


        // CustomInteger a = new CustomInteger(5);
        // CustomInteger b = a;
        // System.out.println(b);
        // a.increment(5);
        // System.out.println(b);

        // int count = 5;
        // while(count != 0){
        //     System.out.println(count);
        //     count--;
        // }

        String strData = in.next();
        System.out.println(strData);

        out.close();

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

    static class Tester{

        private int data;

        public Tester(int inp){
            this.data = inp;
        }

        public int getData(){
            return this.data;
        }
    }

    static class CustomInteger{
        public int value;

        public CustomInteger(int value) {
            this.value = value;
        }
        
        @Override
        public String toString(){
            return String.valueOf(this.value);
        }

        public void increment(int inc){
            this.value += inc;
        }

        public void decrement(int dec){
            this.value -= dec;
        }
    }
}
