package Lab4;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;


/*

######### SCRIBBLY NOTES #########

Lantai is basically a node

LIFT
Move node up or down, and after move, print the value
If command == ATAS && node.next == null, dont move and print value
If command == BAWAH && node.prev == null, dont move and print value
Else node = node.next and prints value

BANGUN
Basically the add command
void add(int val) {
      if (head == null)
        head = new Node(val, null);
      else {
        Node temp = head;
        while (temp.next != null) {
          temp = temp.next;
        }
        temp.next = new Node(val, null);
      }

HANCURKAN
If person on first floor
set next floor as head

set temp for next and previous

*/


// Class untuk Lantai
class Lantai {
    String value;
    Lantai next;
    Lantai prev;

    public Lantai(Lantai prev, String val, Lantai next){
        this.value = val;
        this.prev = prev;
        this.next = next;
    }

    public String getValue(){
        return value;
    }

}


// Class untuk Gedung
class Gedung {

    String name;
    Lantai head;
    Lantai tail;
    Lantai temp;
    Lantai agent;

    public Gedung(String name) {
        this.name = name;
        head = null;
    }

    public void bangun(String input){
        // Base, create first floor
        if(head == null){
            head = new Lantai(null, input, null);
            agent = head;
            tail = head;
        }
        // Create second floor
        else if(head.next == null){
            head.next = new Lantai(head, input, null);
            tail = head.next;
        }
        // If agent is on last floor
        else if(agent.next == null){
            agent.next = new Lantai(agent, input, null);
            tail = agent.next;
        }
        // If agent is on the middle
        else{
            temp = agent.next;
            temp.prev = new Lantai(agent, input, temp);
            agent.next = temp.prev;
        }

        // agent goes to the next floor it created
        if(head.next != null)
            agent = agent.next;
    }

    public String lift(String input){
        // Cases - If there are no next, If there are no previous, Else
        String output = "";

        if(input.toUpperCase().equals("ATAS")){
            if(agent.next != null)
                agent = agent.next;      
        }
        else if(input.toUpperCase().equals("BAWAH")){
            if(agent.prev != null)
                agent = agent.prev;
        }

        output = agent.value;

        return output;
    }

    public String hancurkan(){
        String result = agent.value;

        // If cuma ada satu lantai/base floor
        if(head.next == null){
            head = null;
            agent = null;
            tail = null;
        }
        // If ancuring paling atas
        else if(agent.next == null){
            agent = agent.prev;
            agent.next = null;
            tail = agent;
        }
        // If ancurin paling bawah
        else if(agent.prev == null){
            agent = agent.next;
            agent.prev = null;
            head = agent;
        }
        // If ancurin tengah
        else{
            // grab next and previous
            temp = agent.next;
            agent = agent.prev;
            // detach from the destroyed element
            agent.next = temp;
            temp.prev = agent;
        }

        return result;
    }

    public void timpa(Gedung input){
        // Dijamin currently ada lantai
        // If gedung ditimpa gaada lantai
        if(input.head != null){
            // If current cuma ada satu lantai
            if(head.next == null){

                // attach this head
                head.next = input.head;
                // attach input head
                input.head.prev = head;

                if(input.head.next == null)
                    tail = head.next;
                else
                    tail = input.tail;
            }
            // else 
            else{
                // attach this tail
                temp = tail;
                temp.next = input.head;
                // attach input head
                input.head.prev = temp;

                if(input.head.next == null)
                    tail = input.head;
                else
                    tail = input.tail;
            }
        }
            
    }

    public String sketsa(){
        StringBuilder output = new StringBuilder();
        temp = head;
        while(temp != null){
            output.append(temp.value);
            temp = temp.next;
        }
        return output.toString();
    }

    public String getCurrentValue(){
        return agent.value;
    }


}

public class Lab4 {
    private static InputReader in;
    public static PrintWriter out;
    public static Gedung Gedung;
    
    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Stores every gedung and the current lantai
        HashMap<String, Gedung> gedungMap = new HashMap<String, Gedung>();

        // N operations
        int N = in.nextInt();
        String cmd;

        // Handle inputs
        for (int zz = 0; zz < N; zz++) {
            
            cmd = in.next();
            
            if(cmd.equals("FONDASI")){
                String A = in.next();
                gedungMap.put(A, new Gedung(A));
            }

            else if(cmd.equals("BANGUN")){
                String A = in.next();
                String X = in.next();
                Gedung = gedungMap.get(A);
                Gedung.bangun(X);
            }

            else if(cmd.equals("LIFT")){
                String A = in.next();
                String X = in.next();
                Gedung = gedungMap.get(A);
                out.println(Gedung.lift(X));
            }

            else if(cmd.equals("SKETSA")){
                String A = in.next();
                Gedung = gedungMap.get(A);
                out.println(Gedung.sketsa());

            }

            else if(cmd.equals("TIMPA")){
                String A = in.next();
                String B = in.next();
                Gedung = gedungMap.get(A);
                Gedung.timpa(gedungMap.get(B));
                gedungMap.remove(B);
            }

            else if(cmd.equals("HANCURKAN")){
                String A = in.next();
                Gedung = gedungMap.get(A);
                out.println(Gedung.hancurkan());
            }
        }
     
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


/*

########## ARCHIVE

    // // Currently no floor
    // if(head == null){
    //     head = new Lantai(null, input, null);
    // }
    // // Only one floor, agent on first floor
    // else if(head != null && tail == null){
    //     head.next = new Lantai(head, input, null);
    //     tail = head.next;
    // }
    // // adding the next floors
    // else{
    //     tail.next = new Lantai(tail, input, null);
    //     tail = tail.next;
    // }
*/