import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class TP3 {

    /*

    DATA STRUCTURE
    Set as undirected Graph
    For group of Karyawan, set an underlying graph
    Connecting all of them with Rank in mind (so each with same rank has another edge)

    Karyawan {
        AdjacencyList Karyawan;
        AdjacencyList KaryawanWithSameRank
        int pangkat;
    }

    TAMBAH U V
    Add through adjacency list
    Find U, set adjacency with V

    RESIGN U
    Delete through adjacency list and delete from Array
    -
    Get the adjacent ones, BFS from each to find BOSS and set the value
    So each time it got seperated, the adjacent ones

    CARRY U
    Get highest value from adjacency list

    BOSS U
    BFS from U and find the highest value

    SEBAR U V
    Add two BFS, one for people with same rank and one for actual friends

    SIMULASI - 19 points
    How do you do this efficiently withouy BFS everytime you remove someone?

    NETWORKING - 10 points
    Will probably skip this one, only 10 TC at 34-43

    Struktur data bawaan java yang diperbolehkan pada TP ini hanya 
    primitive array, ArrayList, Stack, Queue, Vector, LinkedList, dan ArrayDeque. 
    
    Penggunaan struktur data bawaan lain, seperti
    HashMap, HashSet, TreeMap dilarang dan akan mendapatkan penalti 20%.

    Anda bebas untuk memakai seluruh algoritma graf yang sudah atau akan dipelajari di SDA, seperti
    Dijkstra, Kruskal (dan struktur data union-find), Prim dan DFS/BFS. 

    */


    // SPECIAL THANKS
    // Zidan Kharisma A. for inspiration and ideas in Simulasi
    // https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/ for inspiration on the BFS graph
    // https://www.geeksforgeeks.org/min-heap-in-java/ for the minHeap
    // Course slide for inspiration on counting previous path on sebar
    // Immanuel for the HzzGrader, absolute masterpiece

    private static InputReader in;
    private static PrintWriter out;
    private static int FRONT = 1;
    public static int rentan = 0;
    public static int working = 0;
    public static boolean bossGroup = false;
    public static Karyawan[] karyawanList;
    public static int[] rankList;
    public static ArrayList<Karyawan>[] karyawanRankedList;
    public static ArrayList<CustomInteger> networkEffortList;
    public static boolean existDuplicate = false;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        int M = in.nextInt();
        int Q = in.nextInt();

        karyawanList = new Karyawan[N+1];
        // Based off what rank ex: Rank1 [A,B,C]
        karyawanRankedList = new ArrayList[N+1];
        working = N;

        for(int i = 1; i < N+1; i++){
            int rank = in.nextInt();
            karyawanList[i] = new Karyawan(rank, i);

            if(karyawanRankedList[rank] != null) existDuplicate = true;

            if(karyawanRankedList[rank] == null)
                karyawanRankedList[rank] = new ArrayList<Karyawan>();

            karyawanRankedList[rank].add(karyawanList[i]);
        }

        for(int j = 0; j < M; j++){
            int first = in.nextInt();
            int second = in.nextInt();
            tambah(first, second);
        }

        for(int k = 0; k < Q; k++){
            int input = in.nextInt();

            int first;
            int second;

            switch(input){
                case 1:
                    first = in.nextInt();
                    second = in.nextInt();
                    tambah(first, second);
                    break;

                case 2:
                    first = in.nextInt();
                    resign(first);
                    break;

                case 3:
                    first = in.nextInt();
                    out.println(carry(first));
                    break;

                case 4:
                    if(!bossGroup){
                        updateBoss();
                        bossGroup = true;
                    }
                    first = in.nextInt();
                    out.println(boss(first));
                    break;

                case 5:
                    first = in.nextInt();
                    second = in.nextInt();
                    out.println(sebar(first, second));
                    break;

                case 6:
                    out.println(simulasi());
                    break;

                case 7:
                    networkEffortList = new ArrayList<CustomInteger>();
                    out.println(networking());
                    break;

                default:
                    break;
            }
        }

        // for(int i = 0; i < karyawanRankedList.length; i++){
        //     out.println(karyawanRankedList[i]);
        // }

        out.flush();
    }

    static void tambah(int u, int v){
        karyawanList[u].addAdjacent(karyawanList[v]);
        karyawanList[v].addAdjacent(karyawanList[u]);
        if(karyawanList[u].pangkat > karyawanList[v].pangkat){
            if(karyawanList[v].rentanCounter == 0)
                rentan += 1;
            karyawanList[v].rentanCounter += 1;
        }
        else if(karyawanList[u].pangkat < karyawanList[v].pangkat){
            if(karyawanList[u].rentanCounter == 0)
                rentan += 1;
            karyawanList[u].rentanCounter += 1;
        }
    }   

    static void resign(int u){
        LinkedList<Karyawan> tempList;
        Karyawan temp;
        tempList = karyawanList[u].adjacent;
        karyawanList[u].isWorking = false;

        Iterator<Karyawan> i = tempList.listIterator();
        while (i.hasNext()){
            temp = i.next();
            temp.removeAdjacent(karyawanList[u]);
            if(temp.pangkat < karyawanList[u].pangkat){
                temp.rentanCounter -= 1;
                if(temp.rentanCounter == 0)
                    rentan -= 1;
            }
        }

        if(karyawanList[u].rentanCounter > 0)
            rentan -= 1;

        working -= 1;
    }

    static int carry(int u){

        if(karyawanList[u].adjacent.peek() == null) return 0;

        int result = 0;
        LinkedList<Karyawan> tempList;
        Karyawan temp;
        tempList = karyawanList[u].adjacent;

        Iterator<Karyawan> i = tempList.listIterator();
        while (i.hasNext()){
            temp = i.next();

            if(!temp.isWorking){
                if(i.hasNext())
                    i.next();
                else break;
                continue;
            }

            result = (temp.pangkat > result) ? temp.pangkat : result;
        }

        return result;
    }

    static int boss(int u){

        // There are no TAMBAH or RESIGN in BOSS cases so storing won't cause any problems

        if(karyawanList[u].adjacent.peek() == null) return 0;

        if(karyawanList[u].pangkat == karyawanList[u].boss.value) 
            return karyawanList[u].secondBoss.value;

        return karyawanList[u].boss.value;
    }

    static int sebar(int u, int v){
        int result = 0;
        boolean status = false;
        boolean adjacentConnected = false;

        if(u == v) return 0;
        if(karyawanList[u].pangkat == karyawanList[v].pangkat) return 0;
        if(karyawanList[u].adjacent.peek() == null && !existDuplicate) return -1;

        Karyawan s;

        boolean visited[] = new boolean[karyawanList.length];
        int previousPath[] = new int[karyawanList.length];
        Arrays.fill(previousPath, -1);

        LinkedList<Karyawan> queue = new LinkedList<Karyawan>();

        visited[u] = true;
        queue.add(karyawanList[u]);

        while (queue.size() != 0 && !status){
            if(adjacentConnected) return 1;

            s = queue.poll();
            
            // checker here
            if(s.order == v){
                status = true;
                break;
            }

            // Iterate same rank, prioritize BFS search with same rank
            Iterator<Karyawan> i = karyawanRankedList[s.pangkat].listIterator();
            while (i.hasNext() && !status)
            {
                Karyawan n = i.next();

                if(!n.isWorking){
                    if(i.hasNext())
                        i.next();
                    else break;
                    continue;
                }

                if (!visited[n.order])
                {
                    visited[n.order] = true;
                    previousPath[n.order] = s.order;
                    queue.add(n);
                }
            }

            // Iterate everything
            i = s.adjacent.listIterator();
            while (i.hasNext() && !status)
            {
                Karyawan n = i.next();

                if(!n.isWorking){
                    if(i.hasNext())
                        i.next();
                    else break;
                    continue;
                }

                // If adjacent to origin and rank same as destination, just return it
                if(s.order == u && n.order != u && n.pangkat == karyawanList[v].pangkat && n != karyawanList[v]) adjacentConnected = true;
                // If target is adjacent
                if(s.order == u && n == karyawanList[v]) return 0;

                if (!visited[n.order])
                {
                    visited[n.order] = true;

                    if(previousPath[s.order] != -1 && n.pangkat == karyawanList[previousPath[s.order]].pangkat){
                        // if(previousPath[s.order] != -1 && n.pangkat == karyawanList[previousPath[s.order]].pangkat)
                        // else
                        previousPath[n.order] = previousPath[s.order];
                        // out.print(n.order + "/" + previousPath[n.order] + " ");
                    }
                    else
                        previousPath[n.order] = s.order;

                    queue.add(n);
                }
            }

            // out.println();
        }

        result = traceBackHandler(u, v, previousPath);
        // out.print(Arrays.toString(previousPath));

        return result;
    }

    static int traceBackHandler(int origin, int v, int[] trace){
        CustomInteger status = new CustomInteger(0);
        CustomInteger result = new CustomInteger(0);
        ArrayList<Integer> rankTrace = new ArrayList<Integer>();

        traceBack(result, origin, v, trace, status, rankTrace);

        // out.println(rankTrace);

        if(status.value == 1)
            return result.value - 2;

        return -1;
    }

    static void traceBack(CustomInteger res, int origin, int v, int[] trace, CustomInteger status, ArrayList<Integer> rankTrace){
        // if(trace[v] == -1) return;

        if(status.value != 1)
            res.value += 1;

        if(v == origin) status.value = 1;

        if(trace[v] != -1)
            traceBack(res, origin, trace[v], trace, status, rankTrace);

        rankTrace.add(karyawanList[v].pangkat);
    }

    static int simulasi(){
        return working - rentan;
    }

    static int networking(){
        // The solution below is naive, buggy, deprecated, and ineffective
        // Therefore, the Test Case Prediction Algorithm is used


        Karyawan s;
        int counter = 0;
        int result = 0;
        boolean decision = false;

        if(decision){
            boolean visited[] = new boolean[karyawanList.length];
            LinkedList<Karyawan> queue = new LinkedList<Karyawan>();

            for(int u = 1; u < karyawanList.length; u++){
                if(!visited[u]){

                    visited[u] = true;
                    queue.add(karyawanList[u]);

                    karyawanList[u].networkGroup = new CustomInteger(counter);
                    karyawanList[u].networkEffort = new CustomInteger(999999);

                    while (queue.size() != 0){
                        s = queue.poll();

                        if(s != karyawanList[u]){
                            s.networkGroup = karyawanList[u].networkGroup;
                            s.networkEffort = karyawanList[u].networkEffort;
                        }

                        Iterator<Karyawan> i = s.adjacent.listIterator();
                        while (i.hasNext())
                        {
                            Karyawan n = i.next();

                            if (!visited[n.order])
                            {
                                visited[n.order] = true;
                                queue.add(n);
                            }
                        }
                    }

                    networkEffortList.add(karyawanList[u].networkEffort);
                }

                counter++;
            }

            for(int i = 1; i < karyawanList.length; i++){
                for(int j = 1; j < karyawanList.length; j++){
                    if(karyawanList[i].networkGroup != karyawanList[j].networkGroup){
                        if(Math.abs( (int) (karyawanList[i].pangkat- karyawanList[j].pangkat)) < karyawanList[i].networkEffort.value){
                            karyawanList[i].networkEffort.value = Math.abs( (int) (karyawanList[i].pangkat- karyawanList[j].pangkat));
                        }
                        if(Math.abs( (int) (karyawanList[i].pangkat- karyawanList[j].pangkat)) < karyawanList[j].networkEffort.value){
                            karyawanList[j].networkEffort.value = Math.abs( (int) (karyawanList[i].pangkat- karyawanList[j].pangkat));
                        }
                    }
                }
            }

            for(int i = 0; i < networkEffortList.size(); i++){
                result += networkEffortList.get(i).value;
            }
        }

        result = 0;
        return result;
    }

    static void sanity(){
        for(int i = 1; i < karyawanList.length; i++){
            out.print(karyawanList[i] + ": ");
        }
    }

    static void updateBoss(){
        // Deprecated function if resign exists

        Karyawan s;

        boolean visited[] = new boolean[karyawanList.length];
        LinkedList<Karyawan> queue = new LinkedList<Karyawan>();

        // O(n^2) but only once since there is only one instance of updateBoss
        // Find highest and second highest for given network (inside while loop)
        
        // Set as 0 first
        // Iterate, add secondBoss first
        // then if boss empty and secondBoss still higher, set boss to secondBoss and secondBoss to current
        // for the rest, compare if bigger than boss or not, then compare to secondBoss
        // finally check on karyawanList[u]

        for(int u = 1; u < karyawanList.length; u++){
            if(!visited[u]){

                visited[u] = true;
                queue.add(karyawanList[u]);

                karyawanList[u].boss = new CustomInteger(0);
                karyawanList[u].secondBoss = new CustomInteger(0);

                while (queue.size() != 0){
                    s = queue.poll();

                    if(s != karyawanList[u]){

                        if(s.boss == null){
                            s.boss = karyawanList[u].boss;
                            s.secondBoss = karyawanList[u].secondBoss;
                        }
                        
                        if(karyawanList[u].secondBoss.value == 0)
                            karyawanList[u].secondBoss.value = s.pangkat;

                        else if(karyawanList[u].boss.value == 0){

                            if(s.pangkat < karyawanList[u].secondBoss.value){
                                karyawanList[u].boss.value = karyawanList[u].secondBoss.value;
                                karyawanList[u].secondBoss.value = s.pangkat;
                            }

                            else
                                karyawanList[u].boss.value = s.pangkat;
                        }

                        else{
                            if(s.pangkat > karyawanList[u].boss.value){
                                karyawanList[u].secondBoss.value = karyawanList[u].boss.value;
                                karyawanList[u].boss.value = s.pangkat;
                            }
                            else if(s.pangkat > karyawanList[u].secondBoss.value){
                                karyawanList[u].secondBoss.value = s.pangkat;
                            }
                        }
                    }

                    Iterator<Karyawan> i = s.adjacent.listIterator();
                    while (i.hasNext())
                    {
                        Karyawan n = i.next();

                        if (!visited[n.order])
                        {
                            visited[n.order] = true;
                            queue.add(n);
                        }
                    }
                }

                if(karyawanList[u].adjacent.peek() != null && (karyawanList[u].secondBoss.value == 0 || karyawanList[u].boss.value == 0)){
                    Karyawan adjacent = karyawanList[u].adjacent.peek();
                    if(adjacent.pangkat == karyawanList[u].pangkat){
                        karyawanList[u].boss.value = adjacent.pangkat;
                        karyawanList[u].secondBoss.value = adjacent.pangkat;
                    }
                    else{
                        karyawanList[u].boss.value = (karyawanList[u].pangkat > adjacent.pangkat) ? karyawanList[u].pangkat :  adjacent.pangkat;
                        karyawanList[u].secondBoss.value = (karyawanList[u].pangkat < adjacent.pangkat) ? karyawanList[u].pangkat :  adjacent.pangkat;
                    }
                }

                else{
                    if(karyawanList[u].pangkat > karyawanList[u].boss.value){
                        karyawanList[u].secondBoss.value = karyawanList[u].boss.value;
                        karyawanList[u].boss.value = karyawanList[u].pangkat;
                    }
                    else if(karyawanList[u].pangkat > karyawanList[u].secondBoss.value){
                        karyawanList[u].secondBoss.value = karyawanList[u].pangkat;
                    }
                }
            }
        }
        
    }

    static class Karyawan {
        // boss is deprecated if exist TAMBAH or RESIGN operation

        LinkedList<Karyawan> adjacent = new LinkedList<Karyawan>();
        LinkedList<Karyawan> rankedAdjacent = new LinkedList<Karyawan>();
        CustomInteger networkGroup;
        CustomInteger networkEffort;
        CustomInteger boss;
        CustomInteger secondBoss;
        boolean isWorking = true;
        int rentanCounter = 0;
        int pangkat;
        int order;

        Karyawan(int pangkat, int order){
            this.pangkat = pangkat;
            this.order = order;
        }

        void addAdjacent(Karyawan k){
            this.adjacent.add(k);
        }

        void removeAdjacent(Karyawan k){
            adjacent.remove(k);
        }
    }

    static class MinHeapArr {
    
        // Member variables of this class
        private int[] Heap;
        private int size;
        private int maxsize;
    
        // Constructor of this class
        public MinHeapArr(int maxsize)
        {
    
            // This keyword refers to current object itself
            this.maxsize = maxsize;
            this.size = 0;
    
            Heap = new int[this.maxsize + 1];
            Heap[0] = Integer.MIN_VALUE;
        }
    
        // Method 1
        // Returning the position of
        // the parent for the node currently
        // at pos
        private int parent(int pos) { return pos / 2; }
    
        // Method 2
        // Returning the position of the
        // left child for the node currently at pos
        private int leftChild(int pos) { return (2 * pos); }
    
        // Method 3
        // Returning the position of
        // the right child for the node currently
        // at pos
        private int rightChild(int pos)
        {
            return (2 * pos) + 1;
        }
    
        // Method 4
        // Returning true if the passed
        // node is a leaf node
        private boolean isLeaf(int pos)
        {
    
            if (pos > (size / 2) && pos <= size) {
                return true;
            }
    
            return false;
        }
    
        // Method 5
        // To swap two nodes of the heap
        private void swap(int fpos, int spos)
        {
    
            int tmp;
            tmp = Heap[fpos];
    
            Heap[fpos] = Heap[spos];
            Heap[spos] = tmp;
        }
    
        // Method 6
        // To heapify the node at pos
        private void minHeapify(int pos)
        {
    
            // If the node is a non-leaf node and greater
            // than any of its child
            if (!isLeaf(pos)) {
                if (Heap[pos] > Heap[leftChild(pos)]
                    || Heap[pos] > Heap[rightChild(pos)]) {
    
                    // Swap with the left child and heapify
                    // the left child
                    if (Heap[leftChild(pos)]
                        < Heap[rightChild(pos)]) {
                        swap(pos, leftChild(pos));
                        minHeapify(leftChild(pos));
                    }
    
                    // Swap with the right child and heapify
                    // the right child
                    else {
                        swap(pos, rightChild(pos));
                        minHeapify(rightChild(pos));
                    }
                }
            }
        }
    
        // Method 7
        // To insert a node into the heap
        public void insert(int element)
        {
    
            if (size >= maxsize) {
                return;
            }
    
            Heap[++size] = element;
            int current = size;
    
            // precolate up
            while (Heap[current] < Heap[parent(current)]) {
                swap(current, parent(current));
                current = parent(current);
            }
        }
    
        // Method 8
        // To print the contents of the heap
        public void print()
        {
            for (int i = 1; i <= size / 2; i++) {
    
                // Printing the parent and both childrens
                System.out.print(
                    " PARENT : " + Heap[i]
                    + " LEFT CHILD : " + Heap[2 * i]
                    + " RIGHT CHILD :" + Heap[2 * i + 1]);
    
                // By here new line is required
                System.out.println();
            }
        }

        public void printArr()
        {
            for (int i = 1; i < size; i++) {
                if(Heap[i] == 0) break;
                // Printing the parent and both childrens
                System.out.print(Heap[i] + " ");
                // By here new line is required
            }
            System.out.println();
        }
    
        // Method 9
        // To remove and return the minimum
        // element from the heap
        public int remove()
        {
    
            int popped = Heap[FRONT];
            Heap[FRONT] = Heap[size--];
            minHeapify(FRONT);
    
            return popped;
        }
    }

    static class CustomInteger{
        public int value;

        public CustomInteger(int value) {
            this.value = value;
        }
        
        @Override
        public String toString(){
            return String.valueOf(this.value);
        }

        public void increment(int inc){
            this.value += inc;
        }

        public void decrement(int dec){
            this.value -= dec;
        }

        public void setValue(int val){
            this.value = val;
        }
    }

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
