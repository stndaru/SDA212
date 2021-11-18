package TP1;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class TP1earlybuild {
    private static InputReader in;
    private static PrintWriter out;


    public static int[] panutan(int n){
        int[] result = new int[2];
        return result;
    }

    public static String[] kompetitif(){
        String[] result = new String[2];
        return result;
    }

    public static String[] evaluasi(){
        String[] result = new String[2];
        return result;
    }

    public static String[] duo(){
        String[] result = new String[2];
        return result;
    }

    public static long deploy(){
        long result = 0;
        return result;
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();

        // name, role + sequence
        Map<String, String> personRole = new HashMap<String, String>();
        // name
        LinkedList<String> person = new LinkedList<String>();
        String temp = "";

        // things to store: name, role, early position, final position, how many times appointed

        for(int inp = 0; inp < N; inp++){

            // get member amount
            int limit = in.nextInt();

            for(int i = 0; i < limit; i++){
                // store members
                String name = in.next();
                Character role = in.next().charAt(0);

                temp = role + Integer.toString(i);
                personRole.put(name, temp);
                person.add(name);
            }

            // DEBUG PURPOSE //
            out.println(person);
            out.println(personRole);
            ///////////////////

            // get command amount/how many days
            limit = in.nextInt();

            for(int j = 0; j < limit; j++){

                // get how many days of daily evaluation
                int eval = in.nextInt();

                // daily evaluation
                for(int k = 0; k < eval; k++){
                    String name = in.next();
                    Integer status = in.nextInt();
                    

                    for(int i = 0; i < person.size(); i++){
                        if(person.get(i).equals(name)){
                            person.remove(i);
                            if(status == 0)
                                person.addFirst(name);
                            else  
                                person.addLast(name);
                            break;
                        }
                    }

                    out.println(person);
                }
            }

            // final evaluation

            // DEBUG PURPOSE //
            //out.println(person);
            //out.println(personRole);
            ///////////////////
        }

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
