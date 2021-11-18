package Lab1;
import java.io.*;
import java.util.*;

public class Lab1 {
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);

    /**
     * The main method that reads input, calls the function 
     * for each question's query, and output the results.
     * @param args Unused.
     * @return Nothing.
     */

    public static void main(String[] args) {
        
        int N = in.nextInt();   // banyak bintang
        int M = in.nextInt();   // panjang sequence
        
        List<String> sequence = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            String temp = in.next();
            sequence.add(temp);
        }

        int maxMoney = getMaxMoney(N, M, sequence);
        out.println(maxMoney);
        out.close();
    }

    public static int getMaxMoney(int N, int M, List<String> sequence) {
        
        List<Integer> tempSeq = new ArrayList<>();

        // set lower bound of result
        int result = -2147483648;
        int tempSum = 0;
        
        // create ArrayList which contains sum of digit between stars
        for(int i = 1; i < sequence.size(); i++){
            if(sequence.get(i).equals("*")){
                tempSeq.add(tempSum);
                tempSum = 0;
            }
            else
                tempSum += Integer.parseInt(sequence.get(i));
        }

        // Iterate through elements to find highest possible sum
        // this algorithm uses the Maximum Contiguous Subsequence Sum from SDA slide
        for(int i = 0; i < tempSeq.size(); i++){
            tempSum += tempSeq.get(i);

            if(tempSum > result)
                result = tempSum;
            if(tempSum < 0)
                tempSum = 0;
        }

        // if there is a single element which is greater
        if(result < Collections.max(tempSeq))
            result = Collections.max(tempSeq);
 
        return result;
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