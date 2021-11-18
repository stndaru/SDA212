package TP1;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;


public class TP1linkedlist {
    private static InputReader in;
    private static PrintWriter out;

    public static int[] panutan(int n, LinkedList<Person> person){
        // B sits at index 0 and S sits at index 1
        int[] result = new int[2];
        char role;

        for(int i = 0; i < n; i++){
            role = person.get(i).getRole();
            if(role == 'B')
                result[0] += 1;
            else
                result[1] += 1;
        }

        return result;
    }

    public static String[] kompetitif(LinkedList<Person> person){
        String[] result = new String[2];
        String maxName = "";
        int maxAppoint = 0;

        // if the person's appointed amount suprass the highest, gets replaced
        for(int i = 0; i < person.size(); i++){
            Person temp = person.get(i);
            if(temp.getAppoint() > maxAppoint){
                maxAppoint = temp.getAppoint();
                maxName = temp.getName();
            }
        }

        result[0] = maxName;
        result[1] = Integer.toString(maxAppoint);
        return result;
    }

    public static ArrayList<String> evaluasi(LinkedList<Person> person){

        ArrayList<String> result = new ArrayList<String>();

        // if the person's early rank is not higher (if they improved then it should be higher)
        // or if the person does not stay at first place, then gets evaluated
        for(int i = 0; i < person.size(); i++){
            Person temp = person.get(i);

            if(temp.getIncreased() == false)
                result.add(temp.getName());
        }

        return result;
    }

    public static ArrayList<String> duo(LinkedList<Person> person){
        // first create array
        ArrayList<String> bN = new ArrayList<String>();
        ArrayList<String> sN = new ArrayList<String>();
        Person tempPerson;

        for(int i = 0; i < person.size(); i++){
            tempPerson = person.get(i);
            if(tempPerson.getRole() == 'B')
                bN.add(tempPerson.getName());
            else
                sN.add(tempPerson.getName());
        }
        
        // always B S
        ArrayList<String> result = new ArrayList<String>();
        String temp = "";
        int pointerB = 0;
        int pointerS = 0;

        while(pointerB != bN.size() && pointerS != sN.size()){
            temp = bN.get(pointerB) + " " + sN.get(pointerS);
            result.add(temp);
            pointerB++;
            pointerS++;
        }

        if(pointerB != bN.size() || pointerS != sN.size()){
            temp = "TIDAK DAPAT:";

            while(pointerB != bN.size()){
                temp += " " + bN.get(pointerB);
                pointerB++;
            }

            while(pointerS != sN.size()){
                temp += " " + sN.get(pointerS);
                pointerS++;
            }

            result.add(temp);
        }
        
        return result;
    }

    public static long deploy(){
        long result = 0;
        return result;
    }

    public static void printList(LinkedList<Person> person){
        for(int i = 0; i < person.size(); i++){
            out.print(person.get(i) + " ");
        }
        out.print("\n");
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();

        // name
        LinkedList<Person> person = new LinkedList<Person>();
        Person tempPerson;

        // things to store: name, role, early position, final position, how many times appointed

        for(int inp = 0; inp < N; inp++){

            // get member amount
            int limit = in.nextInt();

            for(int i = 0; i < limit; i++){
                // store members
                String name = in.next();
                Character role = in.next().charAt(0);

                person.add(new Person(name, role, i));
            }

            // DEBUG PURPOSE //
            // out.println(person);
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
                        Person temp = person.get(i);
                        if(temp.getName().equals(name)){
                            tempPerson = temp.getObject();
                            tempPerson.addAppoint();

                            person.remove(i);
                            
                            if(status == 0){
                                person.addFirst(tempPerson);
                            }  
                            else  
                                person.addLast(tempPerson);
                            break;
                        }
                    }

                    for(int m = 0; m < person.size(); m++){
                        tempPerson = person.get(m);
                        if(m < tempPerson.getEarly() || m < tempPerson.getBefore())
                            tempPerson.addIncreased();
                        
                        tempPerson.setBefore(m);
                    }
                }

                printList(person);
            }

            // final evaluation
            String evaluate = in.next();

            if(evaluate.toUpperCase().equals("PANUTAN")){
                int amount = in.nextInt();
                int[] result = panutan(amount, person);
                out.println(result[0] + " " + result[1]);
            }

            else if(evaluate.toUpperCase().equals("KOMPETITIF")){
                String[] result = kompetitif(person);
                out.println(result[0] + " " + result[1]);
            }

            else if(evaluate.toUpperCase().equals("EVALUASI")){
                ArrayList<String> result = evaluasi(person);
                if(result.size() != 0){
                    for(int i = 0; i < result.size(); i++){
                        out.print(result.get(i) + " ");
                    }
                    out.print("\n");
                }
                else
                    out.println("TIDAK ADA");
            }

            else if(evaluate.toUpperCase().equals("DUO")){
                ArrayList<String> result = duo(person);
                for(int i = 0; i < result.size(); i++){
                    out.println(result.get(i));
                }
            }

            // DEBUG PURPOSE //
            //out.println(person);
            //out.println(personRole);
            ///////////////////

            person.clear();
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

    static class Person {

        private String name;
        private char role;
        private int earlyPos;
        private int beforePos;
        private int appointed = 0;
        private boolean hasIncreased = false;
        
        public Person(String name, char role, int earlyPosition){
            this.name = name;
            this.role = role;
            this.earlyPos = earlyPosition;
            this.beforePos = earlyPosition;
        }
    
        public int getEarly(){
            return this.earlyPos;
        }
    
        public char getRole(){
            return this.role;
        }
    
        public void addAppoint(){
            this.appointed += 1;
        }
    
        public int getAppoint(){
            return this.appointed;
        }

        public void addIncreased(){
            this.hasIncreased = true;
        }

        public boolean getIncreased(){
            return this.hasIncreased;
        }

        public void setBefore(int n){
            this.beforePos = n;
        }

        public int getBefore(){
            return this.beforePos;
        }
    
        public String getName(){
            return this.name;
        }
    
        @Override
        public String toString() {
            return this.name;
        }
    
        public Person getObject() {
            return this;
        }
    }
    
}
