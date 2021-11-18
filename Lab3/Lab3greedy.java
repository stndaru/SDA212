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


class Lab3greedy {

    private static InputReader in;
    private static PrintWriter out;

    static long findMax(int ind, ArrayList<Integer> S, ArrayList<Integer> M){
        // returns 1 if siang is greatest
        return (S.get(ind) > M.get(ind)) ? 1 : 0;
    }

    // TODO
    static private long[] findMaxBerlian(ArrayList<Integer> S, ArrayList<Integer> M, ArrayList<Integer> B, int N) {

        // HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
        // hari, lalu value max
        long[] res = new long[N];
        long maxt = 0;
        int keyt = 0;
        int typet = 0;
        long temp = 0;

        // iterate k dari ambil 1 sampai n
        for(int i = 0; i < N; i++){
            // iterate elemen cari max, kalo 0 masukin max, kalo 1 dst tambah max sebelomnya
            
            maxt = 0;
            keyt = 0;
            typet = 0;

            for(int j = 0; j < N; j++){
                // kalo S append res[0], kalo M append res[1]
                /*
                1 5 3 4 5       ambil 6, maka   1 5 0 4 5   lalu lanjut cari max
                1 5 6 2 1                       1 0 0 0 1

                ambil index i di M, ganti index i+1 dan i-1 nya M sama i nya S jadi 0
                for iterate k hari
                    for nyari elemen max dan apus elemen
                */
                temp = findMax(j, S, M);
                if(temp == 1){
                    if(S.get(j) > maxt){
                        maxt = S.get(j);
                        keyt = j;
                        typet = 1;
                    }
                        //System.out.println("S");
                        //System.out.println(maxt);
                }
                else{
                    if(M.get(j) > maxt){
                        maxt = M.get(j);
                        keyt = j;
                        typet = 0;
                    }
                        //System.out.println("M");
                        //System.out.println(maxt);
                }
                //System.out.println(maxt);
                //System.out.println(keyt);
            }
            if(i == 0)
                res[i] = maxt;
            else{
                if(maxt == 0)
                    break;
                res[i] = res[i-1] + maxt;
            }
            //System.out.println(M.get(keyt));
            //System.out.println(S.get(keyt));

            M.set(keyt, 0);
            S.set(keyt, 0);

            if(typet == 1){
                if(keyt != 0)
                    S.set(keyt-1, 0);
                if(keyt != N-1)
                    S.set(keyt+1, 0);
            }
            else{
                if(keyt != 0)
                    M.set(keyt-1, 0);
                if(keyt != N-1)
                    M.set(keyt+1, 0);
            }

            //System.out.println(keyt);
            System.out.println(Arrays.toString(res));
            System.out.println(S);
            System.out.println(M);
            System.out.println("");
        }

        for(int k = 0; k < N; k++){
            res[k] += B.get(k);
        }

        System.out.println(Arrays.toString(res));


        long[] output = new long[2];
        for(int r = 0; r < res.length; r++){
            if(res[r] > output[0]){
                output[0] = res[r];
                output[1] = r+1;
            }
        }

    
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

        // int jawabanBerlian = findMaxBerlian(S,M,B,N);
        // int jawabanGalian = findBanyakGalian(S,M,B);

        // out.print(jawabanBerlian + " " + jawabanGalian);

        long[] outp = findMaxBerlian(S, M, B, N);
        out.print(outp[0] + " " +outp[1]);

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