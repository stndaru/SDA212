package Lab6;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

/* 
    Huge Shoutout to Adrian Ardizza for the idea on how to solve this lab and 
    Immanuel for the amazing grader and testcases
*/

public class Lab6 {
    private static InputReader in;
    private static PrintWriter out;
    public static Dataran[] dataranList = new Dataran[200000];
    public static HashMap<Integer, DDLNode> dataranMap = new HashMap<>();
    public static MinHeapArr minHeap = new MinHeapArr(200000);
    
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int counter = 0;
        int N = in.nextInt();
        for (counter = 0; counter < N; counter++) {
            int height = in.nextInt();
            dataranList[counter] = new Dataran(height, counter);
            if(dataranMap.get(height) == null){
                dataranMap.put(height, new DDLNode(height, dataranList[counter]));
                minHeap.insert(height);
            }
            else{
                dataranMap.get(height).addDataran(dataranList[counter]);
            }
        }

        int Q = in.nextInt();
        while(Q-- > 0) {
            String query = in.next();
            if (query.equals("A")) {
                int height = in.nextInt();
                dataranList[counter] = new Dataran(height, counter);
                if(dataranMap.get(height) == null){
                    dataranMap.put(height, new DDLNode(height, dataranList[counter]));
                    minHeap.insert(height);
                }
                else{
                    dataranMap.get(height).addDataran(dataranList[counter]);
                }
                counter++;

            } else if (query.equals("U")) {
                int targetIndex = in.nextInt();
                int endHeight = in.nextInt();
                Dataran targetDataran = dataranList[targetIndex];
                DDLNode startingNode = dataranMap.get(targetDataran.height);
                DDLNode targetNode = dataranMap.get(endHeight);

                if(targetDataran.height != endHeight){
                    startingNode.removeDataran(targetDataran);

                    if(targetNode == null || targetNode.head == null){
                        dataranMap.put(endHeight, new DDLNode(endHeight, targetDataran));
                        minHeap.insert(endHeight);
                    }
                    else targetNode.addBasedOnOrder(targetDataran);

                    if(targetDataran.height == minHeap.getFirst() && startingNode.head == null){
                        minHeap.remove();
                        dataranMap.remove(targetDataran.height);
                    }

                    targetDataran.height = endHeight;
                }

            } else if (query.equals("SANITY")) {
                out.println("== SANITY CHECK ==");
                for(int i = 0; i < counter; i++)
                    out.print(dataranList[i].height + " ");
                    out.println();
                for(DDLNode i : dataranMap.values()){
                    out.print(i.height);
                    Dataran temp = i.head;
                    out.print(" HEAD: "+i.head+" ");
                    while(temp != null){
                        out.print("("+temp.prev+"/"+temp+"/"+temp.next + ") ");
                        temp = temp.next;
                    }
                    out.println();
                }
                out.println("MIN HEAP: "+minHeap.getFirst());

            } else {
                int targetHeight = minHeap.getFirst();
                while(dataranMap.get(targetHeight) == null || dataranMap.get(targetHeight).head == null){
                    targetHeight = minHeap.getFirst();
                    if(dataranMap.get(targetHeight) != null && dataranMap.get(targetHeight).head != null) break;
                    minHeap.remove();
                    dataranMap.remove(targetHeight);
                }

                // out.println(targetHeight);

                int maxHeight;

                Dataran targetDataran = dataranMap.get(targetHeight).head;
                // out.println(targetDataran);
                Dataran prev = (targetDataran.order-1 < 0 || dataranList[targetDataran.order-1] == null) ? null : dataranList[targetDataran.order-1];
                Dataran next = (targetDataran.order+1 > 199999 || dataranList[targetDataran.order+1] == null) ? null : dataranList[targetDataran.order+1];

                maxHeight = (prev == null) ? targetDataran.height : Math.max(targetDataran.height, prev.height);
                maxHeight = (next == null) ? maxHeight : Math.max(maxHeight, next.height);

                DDLNode tempNode;
                DDLNode targetNode = dataranMap.get(maxHeight);
                if(targetNode == null) minHeap.insert(maxHeight);

                if(prev != null && prev.height != maxHeight){
                    tempNode = dataranMap.get(prev.height);
                    tempNode.removeDataran(prev);
                    prev.height = maxHeight;

                    if(targetNode == null) dataranMap.put(maxHeight, new DDLNode(maxHeight, prev));
                    else targetNode.addBasedOnOrder(prev);
                }
                if(next != null && next.height != maxHeight){
                    tempNode = dataranMap.get(next.height);
                    tempNode.removeDataran(next);
                    next.height = maxHeight;

                    if(targetNode == null) dataranMap.put(maxHeight, new DDLNode(maxHeight, next));
                    else targetNode.addBasedOnOrder(next);
                }
                if(targetDataran.height != maxHeight){
                    tempNode = dataranMap.get(targetDataran.height);
                    tempNode.removeDataran(targetDataran);
                    targetDataran.height = maxHeight;

                    if(targetNode == null) dataranMap.put(maxHeight, new DDLNode(maxHeight, targetDataran)); 
                    else targetNode.addBasedOnOrder(targetDataran);
                }

                out.println(maxHeight + " " + targetDataran.order);
            }
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

    static class Dataran{
        int height;
        int order;
        Dataran next;
        Dataran prev;
        public Dataran(int height, int order){
            this.height = height;
            this.order = order;
        }
    }

    static class DDLNode {
        Dataran head;
        Dataran tail;
        Dataran tempPrev;
        Dataran tempNext;
        int height;
        public DDLNode(int height, Dataran head){
            this.height = height;
            this.head = head;
            this.tail = head;
        }

        public void addDataran(Dataran dataran){
            if(head == null){
                head = dataran;
                tail = dataran;
            }
            else{
                tail.next = dataran;
                dataran.prev = tail;
                tail = tail.next;
            }
            
        }

        public void removeDataran(Dataran dataran){
            if(dataran == head && head == tail){
                head = null;
                tail = null;
            }
            else if(dataran == head){
                head = dataran.next;
                head.prev = null;
            }
            else if(dataran == tail){
                tail = dataran.prev;
                tail.next = null;
            }
            else {
                tempPrev = dataran.prev;
                tempNext = dataran.next;
                tempPrev.next = tempNext;
                tempNext.prev = tempPrev;
            }

            dataran.next = null;
            dataran.prev = null;
        }

        void addBasedOnOrder(Dataran dataran){
            Dataran temp = head;

            // out.println(key+"/"+head+"/");
            // out.println("OVER HERE "+dataran);

            // case for head
            if(head.order > dataran.order){
                head.prev = dataran;
                dataran.next = head;

                if(head.next == null)
                    tail = dataran.next;
                
                head = head.prev;

                // out.println(key+"/"+head+"/UPDATED");

                return;
            }

            // case for body
            while(temp != null){
                if(temp.order > dataran.order){
                    // out.println(temp+"/");
                    // out.println(key+"/"+head+"/BEFORE");
                    tempPrev = temp.prev;
                    tempPrev.next = dataran;
                    temp.prev = dataran;
                    dataran.next = temp;
                    dataran.prev = tempPrev;
                    // out.println(key+"/"+head+"/UPDATED");
                    return;
                }
                if(temp.next == null) break;
                temp = temp.next;
            }

            // case for tail
            if(temp == tail){
                temp.next = dataran;
                dataran.prev = temp;
                tail = dataran;
            }

            // out.println(key+"/"+head+"/UPDATED");
            return;
        }
    }

    // MinHeap obtained from GFG
    // https://www.geeksforgeeks.org/min-heap-in-java/

    static class MinHeapArr {
    
        // Member variables of this class
        private int[] Heap;
        private int size;
        private int maxsize;
    
        // Initializaing front as static with unity
        private final int FRONT = 1;
    
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

        public int getFirst()
        {
            return Heap[FRONT];
        }
    }

}