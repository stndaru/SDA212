package Lab3;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import static java.lang.Math.min;
import static java.lang.Math.max;


public class Lab3RecFailed {

    private static InputReader in;
    private static PrintWriter out;
    static long[][][] cache;
    static long res;
    static long temp;

    /*
    base case
     
    if N==1 i==1
        if prev bolos
            res = find max(s[N-1],m[N-1]) // beda funct
            cache[i][n][prev] = res
            return res
        else if prev siang
            res = m[N-1]
            cache
            return m
        else
            res = s[N-1]
            cache
            return m

    if i == 0 && N > 0
        return 0
        
    recursive case

    if(cache[i][n][prev > 0]) return cache

    else
        if prev B
            if i != N
                res = max(getmaxS, getmaxM, getmaxB)
            else
                res = max(getmaxS, getmaxM)
            cache
            return

        else if prev S
            res = max(getmaxM, getmaxB)
            cache
            return

        else
            res = max(getmaxS, getmaxB)
            cache
            return

    call func 3 times, S,M,B
    */

    // malem 0, siang 1, bolos 2

    static long getMax(int i, int N, int prev, Integer[] S, Integer[] M){

        // base case
        if(N==1 && i==1){
            if(prev == 2){
                res = max(S[N-1], M[N-1]);
                //res = max(Collections.max(Arrays.asList(S)), Collections.max(Arrays.asList(M))); // beda funct
            }
            
            else if(prev == 1){
                res = M[N-1];
                //res = Collections.max(Arrays.asList(M));
            }
            else{
                res = S[N-1];
                //res = Collections.max(Arrays.asList(S));
            }
            cache[i][N][prev] = res;
            //System.out.println(Arrays.toString(cache));
            return res;
            
        }

        // else if(i == 0 && N > 0){
        //     if(prev == 2){
        //         res = max(M[N-1], S[N-1]);
        //         cache[i][N][prev] = res;
        //     }
        //     else
        //         res = 0;


        //     // if(prev == 1)
        //     //     res = M[N-1];
        //     // else
        //     //     res = S[N-1];
        //     // return res;
            
        //     return res;
        // }
        if(i == 0 || N == 0) return 0;
        
      
        
        // recursive case

        //System.out.println(N + " " + i);
        if(cache[i][N][prev] > 0){
            //System.out.println("Cache " + cache[i][N][prev]);
            return cache[i][N][prev]; 
        } 
    
        else{
            if(prev == 2){
                // if(i == 1 && N == 3){
                //     res = max(S[N-2] + M[N-3], S[N-3] + M[N-2]);
                //     // System.out.println("HIT");
                //     // System.out.println(res);
                //     // System.out.println(S[N-1] + M[N-2]);
                //     // System.out.println(S[N-2] + M[N-1]);
                //     //res = max(getMax(i-1, N-1, 1, S, M), getMax(i-1, N-1, 0, S, M));
                //     //res = max(temp , getMax(i, N-1, 2, S, M));
                // }
                if(i == 1){
                    // logic error
                    // cuma ambil max di index sebelum padahal harusnya ambil 2
                    temp = max(S[N-1], M[N-1]);
                    res = max(temp, getMax(i, N-1, 2, S, M));
                }
                else if(i < N){
                    temp = max(getMax(i-1, N-1, 1, S, M), getMax(i-1, N-1, 0, S, M));
                    res = max(temp , getMax(i, N-1, 2, S, M));
                }   
                else
                    res = max(getMax(i-1, N-1, 1, S, M), getMax(i-1, N-1, 0, S, M));
                
            }  
    
            else if(prev == 1){
                res = max(getMax(i-1, N-1, 0, S, M), getMax(i, N-1, 2, S, M));
                res += S[N-1];
            }
    
            else{
                res = max(getMax(i-1, N-1, 1, S, M), getMax(i, N-1, 2, S, M));
                res += M[N-1];
            }

            cache[i][N][prev] = res;
            return res;
        }
    }

    // TODO
    static private long[] findMaxBerlian(ArrayList<Integer> Sa, ArrayList<Integer> Ma, ArrayList<Integer> Ba, int N) {
        // long[][][] cache = new long[(int) N][(int) N][3];
        Integer[] S = new Integer[N];
        Integer[] M = new Integer[N];
        for(int i = 0; i < N; i++){
            S[i] = Sa.get(i);
            M[i] = Ma.get(i);
        }

        long tempR = 0;
        long resR = 0;

        long tempMax = 0;
        long keyMax = 0;

        for(int k = 0 ; k < N; k++){
            int j = k;
            //System.out.println(getMax(j, N, 0, S, M));
            //System.out.println(getMax(j, N, 1, S, M));
            //System.out.println(getMax(j, N, 2, S, M));
            if(j == 0 ){
                resR = max(Collections.max(Arrays.asList(S)), Collections.max(Arrays.asList(M)));
            }
            else{
                tempR = max(getMax(j, N, 1, S, M), getMax(j, N, 0, S, M));
                resR = max(tempR, getMax(j, N, 2, S, M));
            }
            
            // System.out.print(resR + " " + Ba.get(j) + " // ");
            resR += Ba.get(j);
            

            if(resR > tempMax){
                tempMax = resR;
                keyMax = j+1;
            }
            resR = 0;
        }

        // System.out.println("\n"+ tempMax + " " + keyMax);
        long[] output = {tempMax,keyMax};
        return output;
    }

    // TODO
    static private int findBanyakGalian(ArrayList<Integer> S, ArrayList<Integer> M, ArrayList<Integer> B) {
        return -1;
    }

    public static void main(String args[]) throws IOException {

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        ArrayList<Integer> S = new ArrayList<>();
        ArrayList<Integer> M = new ArrayList<>();
        ArrayList<Integer> B = new ArrayList<>();
        
        int N = in.nextInt();
        
        for(int i=0;i<N;i++) {
            int tmp = in.nextInt();
            S.add(tmp);
        }

        for(int i=0;i<N;i++) {
            int tmp = in.nextInt();
            M.add(tmp);
        }

        for(int i=0;i<N;i++) {
            int tmp = in.nextInt();
            B.add(tmp);
        }

        cache = new long[(int) N + 1][(int) N + 2][3];

        long[] jawabanBerlian = findMaxBerlian(S,M,B, N);
        int jawabanGalian = findBanyakGalian(S,M,B);

        out.print(jawabanBerlian[0] + " " + jawabanBerlian[1]);

        out.flush();
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