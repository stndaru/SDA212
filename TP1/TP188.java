package TP1;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;



public class TP188 {
    private static Map<Integer, Long[]> memoisation = new HashMap<Integer, Long[]>();
    private static InputReader in;
    private static PrintWriter out;

    public static int[] panutan(int n, Map<String, Person[]> person, String[] edgeElement){
        // B sits at index 0 and S sits at index 1
        Person temp = null;
        String next = null;
        int[] result = new int[2];
        char role;

        // only one person
        if(person.size() == 1){
            if(person.get(edgeElement[0])[0].getRole() == 'B')
                result[0] += 1;
            else
                result[1] += 1;

            return result;
        }

        // iterate through map
        for(int i = 0; i < n; i++){
            if(i == 0){
                temp = person.get(edgeElement[0])[0];
                next = person.get(edgeElement[0])[2].getName();
            }
            else if(i == person.size()-1){
                temp = person.get(next)[0];
            }
            else{
                temp = person.get(next)[0];
                next = person.get(next)[2].getName();
            }

            // add to respective role
            role = temp.getRole();
            if(role == 'B')
                result[0] += 1;
            else
                result[1] += 1;
        }

        return result;
    }

    public static String[] kompetitif(Map<String, Person[]> person, String[] edgeElement){
        Person temp = null;
        String next = null;
        String[] result = new String[2];
        String maxName = "";
        int maxAppoint = 0;

        // only one person
        if(person.size() == 1){
            result[0] = person.get(edgeElement[0])[0].getName();
            result[1] = Integer.toString(person.get(edgeElement[0])[0].getAppoint());
            return result;
        }

        // if the person's appointed amount suprass the highest, gets replaced
        for(int i = 0; i < person.size(); i++){
            if(i == 0){
                temp = person.get(edgeElement[0])[0];
                next = person.get(edgeElement[0])[2].getName();
            }
            else if(i == person.size()-1){
                temp = person.get(next)[0];
            }
            else{
                temp = person.get(next)[0];
                next = person.get(next)[2].getName();
            }

            if(temp.getAppoint() > maxAppoint){
                maxAppoint = temp.getAppoint();
                maxName = temp.getName();
            }
        }
       
        result[0] = maxName;
        result[1] = Integer.toString(maxAppoint);
        return result;
    }

    public static Deque<String> evaluasi(Map<String, Person[]> person, String[] edgeElement){
        Person temp = null;
        String next = null;

        Deque<String> result = new ArrayDeque<String>();

        // only 1 person
        if(person.size() == 1){
            next = person.get(edgeElement[0])[0].getName();
            result.add(next);
            return result;
        }

        // if the person's early rank is not higher (if they improved then it should be higher)
        // or if the person does not stay at first place, then gets evaluated
        for(int i = 0; i < person.size(); i++){
            if(i == 0){
                temp = person.get(edgeElement[0])[0];
                next = person.get(edgeElement[0])[2].getName();
            }
            else if(i == person.size()-1){
                temp = person.get(next)[0];
            }
            else{
                temp = person.get(next)[0];
                next = person.get(next)[2].getName();
            }

            if(temp.getIncreased() == false)
                result.add(temp.getName());
        }

        return result;
    }

    public static Deque<String> duo(Map<String, Person[]> person, String[] edgeElement){
        // first create array
        Deque<String> bN = new ArrayDeque<String>();
        Deque<String> sN = new ArrayDeque<String>();
        Person temp = null;
        String next = null;

        // always B S, use pointer logic to implement
        Deque<String> result = new ArrayDeque<String>();
        String tempStr = "";

        if(person.size() == 1){
            next = "TIDAK DAPAT: " + person.get(edgeElement[0])[0];
            result.add(next);
            return result;
        }

        // iterate through map
        for(int i = 0; i < person.size(); i++){
            if(i == 0){
                temp = person.get(edgeElement[0])[0];
                next = person.get(edgeElement[0])[2].getName();
            }
            else if(i == person.size()-1){
                temp = person.get(next)[0];
            }
            else{
                temp = person.get(next)[0];
                next = person.get(next)[2].getName();
            }

            if(temp.getRole() == 'B')
                bN.add(temp.getName());
            else
                sN.add(temp.getName());
        }

        // pointer if both is available
        while(!bN.isEmpty() && !sN.isEmpty()){
            tempStr = bN.pollFirst() + " " + sN.pollFirst();
            result.add(tempStr);
        }

        // if only one more role remain
        if(!bN.isEmpty() || !sN.isEmpty()){
            tempStr = "TIDAK DAPAT:";

            while(!bN.isEmpty()){
                tempStr += " " + bN.pollFirst();
            }

            while(!sN.isEmpty()){
                tempStr += " " + sN.pollFirst();
            }

            result.add(tempStr);
        }
        
        return result;
    }

    public static long deploy(int amount, Map<String, Person[]> person, String[] edgeElement){
        char[] arr = new char[person.size()];
        Person temp = null;
        String next = null;
        long result = 0;

        // iterate map
        for(int i = 0; i < person.size(); i++){
            if(i == 0){
                temp = person.get(edgeElement[0])[0];
                next = person.get(edgeElement[0])[2].getName();
            }
            else if(i == person.size()-1){
                temp = person.get(next)[0];
            }
            else{
                temp = person.get(next)[0];
                next = person.get(next)[2].getName();
            }
            arr[i] = temp.getRole();
        }

        result = deployRec(amount, arr);
        memoisation.clear();

        return result;
    }

    public static long deployRec(int amount, char[] arr){
        long result = 0;

        // logic obtained from Zidan Kharisma
        // reference https://docs.google.com/document/d/1Lx9k3PfaAtI9ofDxCkgs9n7hEEFrLb8jGMY1E8TDEYg/edit
        
        // base case 1, need to make one more group
        if(amount == 1){
            if(arr.length < 2)
                return 0;
            else if(arr[0] == arr[arr.length-1])
                return 1;
            return 0;
        }

        // base case 2, not enough members remaining
        else if(arr.length < amount*2){
            return 0;
        }

        // base case 3, check memoization
        else if(memoisation.containsKey(arr.length))
            if(memoisation.get(arr.length)[0] == amount)
                return memoisation.get(arr.length)[1];
        
        // recursion case, create array
        for(int i = 1; i < arr.length; i++){
            if(arr[0] == arr[i]){
                char[] newArr = new char[arr.length-i-1];
                for(int j = i+1; j < arr.length; j++){
                    newArr[j-i-1] = arr[j];
                }
                result += deployRec(amount-1, newArr);
            }
            
            memoisation.put(arr.length, new Long[] {(long) amount, result});
        }
        return result % (1000000007);
    }

    public static void printList(Map<String, Person[]> person, int limit, String[] edgeElement){
        Person temp = null;
        String next = null;

        // iterate/traverse through map
        // check if there's only 1 person
        if(person.size() > 1){
            for(int i = 0; i < limit; i++){
                if(i == 0){
                    temp = person.get(edgeElement[0])[0];
                    next = person.get(edgeElement[0])[2].getName();
                }
                else if(i == limit-1){
                    temp = person.get(next)[0];
                }
                else{
                    temp = person.get(next)[0];
                    next = person.get(next)[2].getName();
                }

                // check and set for current rank
                if(i < temp.getEarly() || i < temp.getBefore())
                    temp.addIncreased();
                temp.setBefore(i);

                out.print(temp + " ");
            }
        }
        else{
            out.print(person.get(edgeElement[0])[0]);
        }


        out.print("\n");
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();

        // {this, previous, next}
        Map<String, Person[]> person = new HashMap<String, Person[]>();
        String[] edgeElement = new String[2];
        String prev = null;
        // String tempString = null;

        // things to store: name, role, early position, final position, how many times appointed

        /*
            Pointer Logic from Muhammad Agil Ghifari

            For selecting, use hashmap and student, dont use deque
            {name, murid[current, prev, next]}
            and kept an array to store first and last element

            example [A,B,C]
            {"A":[A,null,B]}{"B":[B,A,C]}{"C":[C,B,null]}

            remove B, so get B value[0], store to temp
            get B value[1], set value[2] (next) of the prev from B to value[2] (next) of b
            so {A,..,C}
            {"A":[A,null,C]}{"C":[C,A,null]}

            get B value[2], set value[1] (prev) of the next from B to value[1] (prev) of b
            so {C,..,A}

            if add to front, get front element (get the key from arr[0])
            set the value[1] of front element from null to B 
            set value[2] of B to front element, and set value[1] of B to null
            
            {"B:"[B,null,A]}{"A":[A,B,C]}{"C":[C,A,null]}

            remove front element and replace it with B in front array

            same logic from behind

        */

        for(int inp = 0; inp < N; inp++){

            // get member amount
            int limit = in.nextInt();

            for(int i = 0; i < limit; i++){
                // store members
                String name = in.next();
                Character role = in.next().charAt(0);

                // edge case first person
                if(i == 0){
                    person.put(name, new Person[] {new Person(name, role, i), null, null});
                    edgeElement[0] = name;
                }
                // edge case last person
                else if(i == limit-1){
                    person.put(name, new Person[] {
                        new Person(name, role, i), 
                        person.get(prev)[0], 
                        null});

                    // updates previous element
                    person.put(prev, new Person[] {
                        person.get(prev)[0], 
                        person.get(prev)[1], 
                        person.get(name)[0]});

                    edgeElement[1] = name;
                }
                else{
                    person.put(name, new Person[] {
                        new Person(name, role, i), 
                        person.get(prev)[0], 
                        null});

                    // updates previous element
                    person.put(prev, new Person[] {
                        person.get(prev)[0], 
                        person.get(prev)[1], 
                        person.get(name)[0]});
                }

                prev = name;
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

                    // check if there's only 1 person
                    if(person.size() > 1){

                        // detach element
                        // previous element of selected element
                        if(person.get(name)[1] != null){
                            if(person.get(name)[2] != null)
                            person.put(person.get(name)[1].getName(), new Person[] {
                                person.get(person.get(name)[1].getName())[0], 
                                person.get(person.get(name)[1].getName())[1], 
                                person.get(person.get(name)[2].getName())[0]} );
                            // if selected is in last place, there's no next element
                            else if(person.get(name)[2] == null && !(status == 1))
                            person.put(person.get(name)[1].getName(), new Person[] {
                                person.get(person.get(name)[1].getName())[0], 
                                person.get(person.get(name)[1].getName())[1], 
                                null} );
                        }

                        // next element of selected element
                        if(person.get(name)[2] != null){
                            if(person.get(name)[1] != null)
                            person.put(person.get(name)[2].getName(), new Person[] {
                                person.get(person.get(name)[2].getName())[0], 
                                person.get(person.get(name)[1].getName())[0], 
                                person.get(person.get(name)[2].getName())[2]} );
                            // if selected is in first place, there's no previous element
                            else if(person.get(name)[1] == null && !(status == 0))
                            person.put(person.get(name)[2].getName(), new Person[] {
                                person.get(person.get(name)[2].getName())[0], 
                                null, 
                                person.get(person.get(name)[2].getName())[2]} );
                        }
                        
                        
                        // does evaluation
                        
                        person.get(name)[0].addAppoint();
                        
                        // if person already in first place, doesn't update
                        if(status == 0 && !(edgeElement[0].equals(person.get(name)[0].getName()))){

                            // if person is in last and going to first
                            if(edgeElement[1].equals(person.get(name)[0].getName())){
                                edgeElement[1] = person.get(name)[1].getName();
                            }

                            // updates previous first element
                            person.put(edgeElement[0], new Person[] {
                                person.get(edgeElement[0])[0], 
                                person.get(name)[0], 
                                person.get(edgeElement[0])[2]});
                            
                            // updates selected element
                            person.put(name, new Person[] {
                                person.get(name)[0], 
                                null,
                                person.get(edgeElement[0])[0]});

                            edgeElement[0] = name;
                        }  
                        // if person already in last place, doesn't update
                        else if(status == 1 && !(edgeElement[1].equals(person.get(name)[0].getName()))){

                            // if person is in first and going to last
                            if(edgeElement[0].equals(person.get(name)[0].getName())){
                                edgeElement[0] = person.get(name)[2].getName();
                            }

                            // updates previous last element
                            person.put(edgeElement[1], new Person[] {
                                person.get(edgeElement[1])[0], 
                                person.get(edgeElement[1])[1],
                                person.get(name)[0]});

                            // updates selected element
                            person.put(name, new Person[] {
                                person.get(name)[0],
                                person.get(edgeElement[1])[0],
                                null});

                            edgeElement[1] = name;
                        }
                    } 
                    else{
                        // salah implement harusnya add appoint
                        continue;
                    }
                }

                // prints rank of current day
                printList(person, limit, edgeElement);
            }

            // final evaluation

            String evaluate = in.next();

            if(evaluate.toUpperCase().equals("PANUTAN")){
                int amount = in.nextInt();
                int[] result = panutan(amount, person, edgeElement);
                out.println(result[0] + " " + result[1]);
            }

            else if(evaluate.toUpperCase().equals("KOMPETITIF")){
                String[] result = kompetitif(person, edgeElement);
                out.println(result[0] + " " + result[1]);
            }

            else if(evaluate.toUpperCase().equals("EVALUASI")){
                Deque<String> result = evaluasi(person, edgeElement);
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
                Deque<String> result = duo(person, edgeElement);
                while(!result.isEmpty()){
                    out.println(result.pollFirst());
                }
            }

            else if(evaluate.toUpperCase().equals("DEPLOY")){
                int amount = in.nextInt();
                long result = deploy(amount, person, edgeElement);
                out.println(result);
            }

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
