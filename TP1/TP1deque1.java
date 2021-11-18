package TP1;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;


public class TP1deque1 {
    private static InputReader in;
    private static PrintWriter out;

    public static int[] panutan(int n, Deque<Person> person){
        // B sits at index 0 and S sits at index 1
        int[] result = new int[2];
        char role;

        for(int i = 0; i < n; i++){
            role = person.pollFirst().getRole();
            if(role == 'B')
                result[0] += 1;
            else
                result[1] += 1;
        }

        return result;
    }

    public static String[] kompetitif(Deque<Person> person){
        String[] result = new String[2];
        String maxName = "";
        int maxAppoint = 0;

        // if the person's appointed amount suprass the highest, gets replaced
        while(!person.isEmpty()){
            Person temp = person.pollFirst();
            if(temp.getAppoint() > maxAppoint){
                maxAppoint = temp.getAppoint();
                maxName = temp.getName();
            }
        }

        result[0] = maxName;
        result[1] = Integer.toString(maxAppoint);
        return result;
    }

    public static Deque<String> evaluasi(Deque<Person> person){

        Deque<String> result = new ArrayDeque<String>();

        // if the person's early rank is not higher (if they improved then it should be higher)
        // or if the person does not stay at first place, then gets evaluated
        while(!person.isEmpty()){
            Person temp = person.pollFirst();

            if(temp.getIncreased() == false)
                result.add(temp.getName());
        }

        return result;
    }

    public static Deque<String> duo(Deque<Person> person){
        // first create array
        Deque<String> bN = new ArrayDeque<String>();
        Deque<String> sN = new ArrayDeque<String>();
        Person tempPerson;

        while(!person.isEmpty()){
            tempPerson = person.pollFirst();
            if(tempPerson.getRole() == 'B')
                bN.add(tempPerson.getName());
            else
                sN.add(tempPerson.getName());
        }
        
        // always B S
        Deque<String> result = new ArrayDeque<String>();
        String temp = "";

        while(!bN.isEmpty() && !sN.isEmpty()){
            temp = bN.pollFirst() + " " + sN.pollFirst();
            result.add(temp);
        }

        if(!bN.isEmpty() || !sN.isEmpty()){
            temp = "TIDAK DAPAT:";

            while(!bN.isEmpty()){
                temp += " " + bN.pollFirst();
            }

            while(!sN.isEmpty()){
                temp += " " + sN.pollFirst();
            }

            result.add(temp);
        }
        
        return result;
    }

    public static long deploy(){
        long result = 0;
        return result;
    }

    public static void printDeque(Deque<Person> person, int limit){
        Person temp;

        for(int i = 0; i < limit; i++){
            temp = person.pollFirst();

            // check for current rank
            if(i < temp.getEarly() || i < temp.getBefore())
                temp.addIncreased();
            temp.setBefore(i);

            out.print(temp + " ");
            person.add(temp);
        }

        out.print("\n");
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();

        Deque<Person> person = new ArrayDeque<Person>();
        Stack<Person> tempStack = new Stack<Person>();
        Person temp = null;
        Person tempPerson = null;

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

            // get command amount/how many days
            int days = in.nextInt();

            for(int j = 0; j < days; j++){

                // get how many daily evaluation
                int eval = in.nextInt();

                // daily evaluation
                for(int k = 0; k < eval; k++){
                    String name = in.next();
                    Integer status = in.nextInt();

                    while(true){

                        temp = person.pollFirst();
                        
                        // check to see if temp matches the searched name
                        if(temp.getName().equals(name)){
                            temp.addAppoint();
                            
                            if(status == 0){
                                tempPerson = temp;
                            }  
                            else  
                                person.addLast(temp);

                            break;
                        }
                        
                        // adds to temporary deque
                        tempStack.push(temp);
                        // currentIndex++;
                    }

                    // returns the position from tempDeque
                    while(!tempStack.isEmpty()){
                        temp = tempStack.pop();

                        // check for changes in rank is moved to the print
                        // if(currentIndex < temp.getEarly() || currentIndex < temp.getBefore())
                        //     temp.addIncreased();
                        //  temp.setBefore(currentIndex);

                        person.addFirst(temp);
                        // currentIndex--;
                    };

                    if(status == 0)
                        person.addFirst(tempPerson);
                }

                printDeque(person, limit);
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
                Deque<String> result = evaluasi(person);
                if(result.size() != 0){
                    while(!result.isEmpty()){
                        out.print(result.pollFirst() + " ");
                    }
                    out.print("\n");
                }
                else
                    out.println("TIDAK ADA");
            }

            else if(evaluate.toUpperCase().equals("DUO")){
                Deque<String> result = duo(person);
                while(!result.isEmpty()){
                    out.println(result.pollFirst());
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
