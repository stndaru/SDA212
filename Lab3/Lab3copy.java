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


public class Lab3copy {

    private static InputReader in;
    private static PrintWriter out;
    static long temp;

    // CREDITS DUE WHERE IT IS #############################
    // SPECIAL THANKS TO: GEEKSFORGEEKS, ABDUL BARI, AND SOME OF THE BEST MINDS IN PACIL
    // ZIDAN KHARISMA, MUHAMMAD AGIL GHIFARI, MUHAMMAD KENSHIN HIMURA

    /*
    Array[Hari][Siang/Malam/Bolos][Hashmap:Value]
    iterate tiap hari
        iterate tiap hashmap siang
            go malam/bolos
                if malam, add key 1 and add bonus, cek if contains key di hari berikut, if ada compare value
                if bolos, don't add key
        iterate tiap hashmap malam
                same
        iterate tiap hashmap bolos
            go siang/malam/bolos
                if siang, add key 1 and add bonus, cek contains yadiyada
                same
    
    nanti dapet array hari 0 siang ada apa aja, malem apa, bolos apa dengan hashmap valuenya
    iterate every elemen hari terakhir dan waktu, cari max hashmap key dan store ke hashmap result
    */

        //System.out.println(Arrays.toString(B));
        // 1 1 1 1 1
        // 16 4 5 9 3
        // bonus = target - selisih
        // 15 3 4 8 2
        // 15 3-15 4-3 8-4 2-8
        // 16 16-12 16-12+1 16-12+1+4 16-12+1+4-6
        // 16  -12   +1  +4  -6
        // 16 4 5 9 3

    // TODO
    static private long[] findMaxBerlian(ArrayList<Integer> S, ArrayList<Integer> M, ArrayList<Integer> Bo, int N) {
        Long[] B = new Long[N+1];
        B[0] = (long) Bo.get(0);
        B[1] = (long) Bo.get(0);
        for(int bon = 2; bon < N+1; bon++){
            B[bon] = (long) Bo.get(bon-1) - Bo.get(bon-2);
        }
        
        long[][][] cache = new long[N][3][N+1]; // [days][siang,malam,bolos]
        long maxT = 0;
        long keyT = 0;


        // iterate days
        if(N > 1){
            for(int i = 0; i < N-1; i++){
                // instead of hashmap, taro sesuai index
                for(int j = 0; j < 3; j++){
                    // generate day one
                    if(i == 0){
                        // siang
                        if(j == 0)
                            cache[i][j][1] = S.get(i) + B[i];
                        // malam
                        else if(j == 1)
                            cache[i][j][1] = M.get(i) + B[i];
                    }

                    // not day 1
                    
                        // iterate siang
                        if(j == 0){
                            for(int k = 0; k < N; k++){
                                // for every siang, cek value dgn index day berikut di malam dan bolos
                                temp = cache[i][j][k];
                                
                                // cek malem day beikut with key added
                                if(temp + B[k+1] + M.get(i+1) > cache[i+1][1][k+1]){
                                    cache[i+1][1][k+1] = temp + B[k+1] + M.get(i+1);
                                }
                                // cek bolos day berikut, key not added
                                if(temp > cache[i+1][2][k]){
                                    cache[i+1][2][k] = temp;
                                }
                            }
                        }

                        if(j == 1){
                            for(int k = 0; k < N-1; k++){
                                temp = cache[i][j][k];
                                
                                // cek siang day beikut with key added
                                if(temp + B[k+1] + S.get(i+1) > cache[i+1][0][k+1]){
                                    cache[i+1][0][k+1] = temp + B[k+1] + S.get(i+1);
                                }
                                // cek bolos day berikut, key not added
                                if(temp > cache[i+1][2][k]){
                                    cache[i+1][2][k] = temp;
                                }
                            }
                        }

                        if(j == 2){
                            for(int k = 0; k < N-1; k++){
                                
                                temp = cache[i][j][k];
                                
                                
                                // cek siang day beikut with key added
                                if(temp + B[k+1] + S.get(i+1) > cache[i+1][0][k+1]){
                                    cache[i+1][0][k+1] = temp + B[k+1] + S.get(i+1);
                                }
                                if(temp + B[k+1] + M.get(i+1) > cache[i+1][1][k+1]){
                                    cache[i+1][1][k+1] = temp + B[k+1] + M.get(i+1);
                                }
                                // cek bolos day berikut, key not added
                                if(temp > cache[i+1][2][k]){
                                    cache[i+1][2][k] = temp;
                                }
                            }
                        }
                        
                }
            }


            for(int i = 0; i < N+1; i++){

                // iterate last day, k nya mulai dari k = 0 sampai k = n
                for(int j = 0; j < 3; j++){
                    if(cache[N-1][j][i] > maxT){
                        maxT = cache[N-1][j][i];
                        keyT = i;
                    }
                }
            }
        }
        else{
            maxT = max(S.get(0), M.get(0));
            maxT += Bo.get(0);
            keyT = 1;
        }

        // edge case analysis cheeky breeky time
        long tempSiang = 0;
        long tempMalam = 0;
        // start with siang
        for(int i = 0; i < N; i++){
            if(i % 2 == 0){
                tempSiang += S.get(i);
                tempMalam += M.get(i);
            }
            else{
                tempSiang += M.get(i);
                tempMalam += S.get(i);
            }
        }
        temp = max(tempMalam + Bo.get(N-1), tempSiang + Bo.get(N-1));
        // System.out.println(temp);
        if(temp > maxT){
            maxT = temp;
            keyT = N;
        }

        long[] result = {maxT, keyT};
        return result;
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

        long[] jawabanBerlian = findMaxBerlian(S,M,B,N);
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