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


class Lab3scrapped {

    private static InputReader in;
    private static PrintWriter out;

    // 
    static int findMax(int ind, ArrayList<Integer> S, ArrayList<Integer> M){
        // returns 1 if siang is greatest
        return (S.get(ind) > M.get(ind)) ? 1 : 0;
    }


    // TODO
    static private long[] findMaxBerlian(ArrayList<Integer> S, ArrayList<Integer> M, ArrayList<Integer> B, int N) {
        
        // result used to find the sum from taking 0 to N elements
        HashMap<Integer, Long> result = new HashMap<Integer, Long>();
        long sum = 0;

        // temp holds key : state
        HashMap<Integer, Long> temp = new HashMap<Integer, Long>();
        long tempMax = 0;
        long tempMaxState = 0;
        int tempMaxKey = 0;

        long tempCur = 0;
        int tempState = 0;

        // damn n^3

        for(int i = 0; i < N+1; i++){ //0,N+1
            
            // taking i much elements
            for(int k = 0; k < i; k++){

                tempMax = 0;

                // iterate through elements finding biggest value
                for(int j = 0; j < N; j++){

                    // jangan langsung get max, cek dulu kalo beda state lebih gede dari tempmax atoga
                    // if index mepet, ambil state beda, cek lebih gede dri maxsum or no
                    // if index ga mepet, ambil maxnya, cek lebih gede dri maxsum or no

                    
                    // get siang/malam

                    // check if index is valid or not
                    if(!temp.containsKey(j)){

                        tempState = findMax(j, S, M);
                        
                        // check if prev or next elemnt is the same state (mepet)
                        if((temp.containsKey(j-1) && (temp.get(j-1) == tempState)) || 
                            (temp.containsKey(j+1) && (temp.get(j+1) == tempState))){
                                // switch to other array
                                // switching from siang
                                if(tempState == 1 && M.get(j) > tempMax){
                                    tempMax = M.get(j);
                                    tempMaxState = 0;
                                    tempMaxKey = j;
                                }
                                else if(S.get(j) > tempMax){
                                    tempMax = S.get(j);
                                    tempMaxState = 1;
                                    tempMaxKey = j;
                                }
                        }
                        // if not mepet, just check max
                        // get val
                        else{
                            tempCur = (tempState == 1) ? S.get(j) : M.get(j);

                            // check
                            if(tempCur > tempMax){
                                tempMax = tempCur;
                                tempMaxState = tempState;
                                tempMaxKey = j;
                            }
                        }
                    }

                }

                // done iterate, add data to temp res
                temp.put(tempMaxKey, tempMaxState);
                // System.out.println(tempMaxKey + " " + tempMaxState);

            }
            for(int ts : temp.keySet()){
                if(temp.get(ts) == 1){
                    sum += (long) S.get(ts);
                    // System.out.println(S.get(ts));
                }
                else{
                    sum += M.get(ts);
                    // System.out.println(M.get(ts));
                }
            }
            if(i == 0)
                result.put(i, (long) (sum + B.get(0)));
            else
                result.put(i, (long) (sum + B.get(i-1)));

            temp.clear();
            sum = 0;
        }

        tempMax = 0;
        tempMaxKey = 0;
        for(int i : result.keySet()){
            if(result.get(i) > tempMax){
                tempMax = result.get(i);
                tempMaxKey = i;
            }  
        }

        // System.out.println(result);

        long[] res = {tempMax, tempMaxKey};
        return res;
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

        // modified template
        // int jawabanBerlian = findMaxBerlian(S,M,B);
        // int jawabanGalian = findBanyakGalian(S,M,B);

        // out.print(jawabanBerlian + " " + jawabanGalian);

        long[] res = findMaxBerlian(S,M,B,N);

        out.print(res[0] + " " + res[1]);

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