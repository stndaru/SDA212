package Lab2;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
// import static java.lang.Math.min;
// import static java.lang.Math.max;


class Lab2 {

    private static InputReader in;
    private static PrintWriter out;


    /* 
    ########## CORETAN ##########

    Linked list isi anggota ex: [56,72,1232321,23]
    Terus ada queue linkedlist nama gangnya: [satu,dua,tiga]

    if yang pertama jadi nol, remove lalu remove gang pertama
    if yang pertama jadi negatif, add ke elemen berikut lalu remove gang pertama
    

    - Store Total
    Hashmap<String, Int>
    Get String key, if hashmap contains, get then add val, if not just add
    
    #############################
    */

    // TODO
    static private int handleDatang(String Gi, int Xi, Deque<String> gL, Deque<Integer> qA, int total) {
        
        // Check if the last added is the same
        if(gL.size() > 0  && gL.getLast().equals(Gi)){
            int temp = qA.pollLast();
            temp += Xi;
            qA.add(temp);
        }

        else{
            gL.add(Gi);
            qA.add(Xi);
        }

        return total += Xi;
    }

    // TODO
    static private String handleLayani(int Yi, Deque<String> gL, Deque<Integer> qA, HashMap<String, Integer> tA) {
        // take first element
        // element - Yi
        // if negatif, turn positive, next element minus temp, repeat until not negatif?
        // 3 4 5
        // -9
        // 3-9 = -6 negatif, add stats to hashmap and remove element
        // -6 + 4 = -2 negatif, do it again
        // -2 + 5 = 3 positive, then 3 is still on queue
        // CASE NORMAL
        // 9 - 3 = 6
        // is positive, 6 is on queue (first), 9 is original (temp), 3 is taken

        String gangName;
        int first = qA.pollFirst();
        int temp = first;

        first -= Yi;

        while(first < 0){
            // if it turns out grab more than intended

            gangName = gL.pollFirst();
            if(tA.containsKey(gangName)){
                int current = tA.get(gangName) + temp;
                tA.put(gangName, current);
            }
            else tA.put(gangName, temp);

            // first contains sum
            // temp contains first element
            temp = qA.pollFirst();
            first += temp;
            // if still negative, loops back, deletes the first element again
        }

        // if result is not 0
        // first is now positive and holds left in queue, and temp holds the first element
        // so don't delete the gangName
        if(first > 0){
            qA.addFirst(first);

            gangName = gL.peekFirst();
            if(tA.containsKey(gangName)){
                int current = tA.get(gangName) + temp - first;
                tA.put(gangName, current);
            }
            else tA.put(gangName, temp-first);
        }

        // if result is 0, deletes gangname and doesnt add again to queue
        else{
            gangName = gL.pollFirst();
            if(tA.containsKey(gangName)){
                int current = tA.get(gangName) + temp;
                tA.put(gangName, current);
            }
            else tA.put(gangName, temp);
        }
        return gangName;
    }

    // TODO
    static private int handleTotal(String Gi, HashMap<String,Integer> tA) {
        if(tA.containsKey(Gi))
            return tA.get(Gi);
        return 0;
    }

    // #######################################

    public static void main(String args[]) throws IOException {

        //
        Deque<String> gangList = new ArrayDeque<>();
        Deque<Integer> queueAmount = new ArrayDeque<>();
        HashMap<String,Integer> totalAmount = new HashMap<>();

        int total = 0;
        //

        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N;

        N = in.nextInt();

        for(int tmp=0;tmp<N;tmp++) {
            String event = in.next();

            if(event.equals("DATANG")) {
                String Gi = in.next();
                int Xi = in.nextInt();

                out.println(handleDatang(Gi, Xi, gangList, queueAmount, total));
                total += Xi;
            } else if(event.equals("LAYANI")) {
                int Yi = in.nextInt();
                
                out.println(handleLayani(Yi, gangList, queueAmount, totalAmount));
                total -= Yi;
            } else {
                String Gi = in.next();

                out.println(handleTotal(Gi, totalAmount));
            }
        }
        
        // Debugging Purposes
        // System.out.println("##########");
        // System.out.println("Content of Queue Amount");
        // while(!queueAmount.isEmpty()) System.out.println(queueAmount.poll());
        // System.out.println("##########");
        // System.out.println("Content of Gang Amount");
        // while(!gangList.isEmpty()) System.out.println(gangList.poll());
        // System.out.println("##########");
        // System.out.println(totalAmount);
        // System.out.println("##########");

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