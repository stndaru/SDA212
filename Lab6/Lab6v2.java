package Lab6;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab6v2 {
    private static InputReader in;
    private static PrintWriter out;
    public static Dataran[] dataranList = new Dataran[500000];
    public static AVLTree dataranTree = new AVLTree();
    
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        Dataran temp;
        Dataran prev;
        Dataran next;
        Node tempNode;
        int counter = 0;
        
        int N = in.nextInt();
        for (counter = 0; counter < N; counter++) {
            int height = in.nextInt();
            if(counter == 0){
                temp = new Dataran(height, null, null);
            }
            else{
                temp = new Dataran(height, dataranList[counter-1], null);
                dataranList[counter-1].next = temp;
            }
            temp.order = counter;
            dataranList[counter] = temp;
            tempNode = dataranTree.insert(height, temp);
        }

        int Q = in.nextInt();
        while(Q-- > 0) {
            String query = in.next();

            // out.print(query);
            // dataranTree.find(6).sanityCheck();

            if (query.equals("A")) {
                int height = in.nextInt();
                temp = new Dataran(height, dataranList[counter-1], null);
                temp.order = counter;
                dataranList[counter-1].next = temp;
                dataranList[counter] = temp;
                dataranTree.insert(height, temp);
                counter++;

            } else if (query.equals("U")) {
                // Problem here, how to update AVLTree?
                // What if there exist a height that it changes?
                // How to sort?
                // O(n) approach
                int targetIndex = in.nextInt();
                int endHeight = in.nextInt();
                Dataran targetDataran = dataranList[targetIndex];

                dataranTree.delete(targetDataran.height, targetDataran);
                targetDataran.treeNext = null;
                targetDataran.treePrev = null;

                // out.println(targetDataran+"/");
                tempNode = dataranTree.find(endHeight);
                // out.println(tempNode+"/"+tempNode.tail);
                if(tempNode == null) dataranTree.insert(endHeight, targetDataran);
                else tempNode.addBasedOnOrder(targetDataran);

                targetDataran.height = endHeight;

            } else if (query.equals("SANITY")) {
                out.println("== SANITY CHECK ==");
                for(int i = 0; i < counter; i++)
                    out.print(dataranList[i].height + " ");

                out.println("\nTREE: ");
                dataranTree.preorderTraversal();
                out.println();

            } else {
                // Same problem as U
                Node lowest = dataranTree.ceil(dataranTree.root, 1);
                Dataran target = lowest.head;
                int maxHeight;

                prev = (target.order-1 > -1) ? dataranList[target.order-1] : null;
                next = (target.order+1 < counter) ? dataranList[target.order+1] : null;
                maxHeight = (prev == null) ? target.height : Math.max(target.height, prev.height);
                maxHeight = (next == null) ? maxHeight : Math.max(maxHeight, next.height);

                // tempNode = dataranTree.find(maxHeight);
                // out.println(tempNode);
                // out.println(dataranTree.find(6));
                // out.println("PRETRAV");
                // dataranTree.preorderTraversalReal();
                // out.println();

                if(prev != null && prev.height != maxHeight){
                    dataranTree.delete(prev.height, prev);
                    tempNode = dataranTree.find(maxHeight);
                    prev.height = maxHeight;
                    prev.treeNext = null;
                    prev.treePrev = null;
                    if(tempNode == null) tempNode = dataranTree.insert(maxHeight, prev);
                    else tempNode.addBasedOnOrder(prev);
                }
                if(next != null && next.height != maxHeight){
                    dataranTree.delete(next.height, next);
                    tempNode = dataranTree.find(maxHeight);
                    next.height = maxHeight;
                    next.treeNext = null;
                    next.treePrev = null;
                    if(tempNode == null) tempNode = dataranTree.insert(maxHeight, next);
                    else tempNode.addBasedOnOrder(next);
                }
                if(target.height != maxHeight){
                    dataranTree.delete(target.height, target);
                    tempNode = dataranTree.find(maxHeight);
                    target.height = maxHeight;
                    target.treeNext = null;
                    target.treePrev = null;
                    if(tempNode == null) tempNode = dataranTree.insert(maxHeight, target);
                    else tempNode.addBasedOnOrder(target);
                }


                // tempNode.sanityCheck();
                // Node tempNode2 = dataranTree.find(6);
                // out.println();
                // tempNode2.sanityCheck();
                // out.println();
                // out.println(tempNode == tempNode2);

                // out.println(tempNode);
                // out.println(dataranTree.find(6));

                out.println(maxHeight + " " + target.order);
            }

            // Core of the problem is finding the floor value of the 
            // Order within the HashLinked inside the Tree
            // Once found it can be easily inserted
            // Worst case can be O(n)
            // Example 4 4 4 4 4 4 4 4 ... 5 ... 4 4 4 4 4
            // and then change 5 into 4
            // Trying it out first I guess?

        }

        out.flush();
    }

    static class Dataran{
        int height;
        int order;
        Dataran prev;
        Dataran next;
        Dataran treeNext;
        Dataran treePrev;
        public Dataran(int height, Dataran prev, Dataran next){
            this.height = height;
            this.prev = prev;
            this.next = next;
        }
    }

    static class Node {
        int key;
        int height;
        Dataran head;
        Dataran tail;
        Dataran temp;
        Dataran tempPrev;
        Dataran tempNext;
        // {prev, current, next}
        // this HashMap is the root of all RTE problems :/
        // Maybe tone down to HashSet?
        // And then keep next and prev within Dataran class
        // HashMap<Dataran, Dataran[]> dataranMap = new HashMap<>();
        Node left;
        Node right;

        Node(int key) {
            this.key = key;
        }

        void addDataran(Dataran dataran){
            if(head == null){
                head = dataran;
            }
            else {
                tail.treeNext = dataran;
                dataran.treePrev = tail;
            }
            tail = dataran;
        }

        void addBasedOnOrder(Dataran dataran){
            temp = head;

            // out.println(key+"/"+head+"/");
            // out.println("OVER HERE "+dataran);

            // case for head
            if(head.order > dataran.order){
                head.treePrev = dataran;
                dataran.treeNext = head;

                if(head.treeNext == null)
                    tail = dataran.treeNext;
                
                head = head.treePrev;

                // out.println(key+"/"+head+"/UPDATED");

                return;
            }

            // case for body
            while(temp != null){
                if(temp.order > dataran.order){
                    // out.println(temp+"/");
                    // out.println(key+"/"+head+"/BEFORE");
                    tempPrev = temp.treePrev;
                    tempPrev.treeNext = dataran;
                    temp.treePrev = dataran;
                    dataran.treeNext = temp;
                    dataran.treePrev = tempPrev;
                    // out.println(key+"/"+head+"/UPDATED");
                    return;
                }
                if(temp.treeNext == null) break;
                temp = temp.treeNext;
            }

            // case for tail
            if(temp == tail){
                temp.treeNext = dataran;
                dataran.treePrev = temp;
                tail = dataran;
            }

            // out.println(key+"/"+head+"/UPDATED");
            return;
        }

        void removeDataran(Dataran dataran){
            if(dataran == head && head == tail){
                head = null;
                tail = null;
            }
            else if(dataran == head){
                head = dataran.treeNext;
                head.treePrev = null;
            }
            else if(dataran == tail){
                tail = dataran.treePrev;
                tail.treeNext = null;
            }
            else {
                tempPrev = dataran.treePrev;
                tempNext = dataran.treeNext;
                tempPrev.treeNext = tempNext;
                tempNext.treePrev = tempPrev;
            }
        }

        void sanityCheck(){
            temp = head;
            out.print("HEAD: "+head);
            while(temp != null){
                out.print("(" + temp.treePrev + "/" + temp + "/" + temp.treeNext + ") ");
                temp = temp.treeNext;
            }
            out.print("TAIL: "+tail);
        }
    }

    static class AVLTree {
    
        public Node root;
    
        public Node find(int key) {
            Node current = root;
            while (current != null) {
                if (current.key == key) {
                   break;
                }
                current = current.key < key ? current.right : current.left;
            }
            return current;
        }
    
        public Node insert(int key, Dataran dataran) {
            Node node = find(key);

            if(node == null)
                root = insert(root, key);
                
            node = find(key);
            node.addDataran(dataran);
            // out.println("OVER HERE "+dataran);
            return node;
        }
    
        public void delete(int key, Dataran dataran) {
            root = delete(root, key, dataran, false);
        }
    
        public Node getRoot() {
            return root;
        }
    
        public int height() {
            return root == null ? -1 : root.height;
        }
    
        private Node insert(Node node, int key) {
            if (node == null) {
                return new Node(key);
            } else if (node.key > key) {
                node.left = insert(node.left, key);
            } else if (node.key < key) {
                node.right = insert(node.right, key);
            } else {
                return node;
            }
            return rebalance(node);
        }
    
        private Node delete(Node node, int key, Dataran dataran, boolean forced) {
            boolean clear = false;
            
            if (node == null) {
                return node;
            } else if (node.key > key) {
                node.left = delete(node.left, key, dataran, forced);
            } else if (node.key < key) {
                node.right = delete(node.right, key, dataran, forced);
            } else {
                // Key pasti sesuai, tinggal membatasi
                if(node.key == key || forced){
                    if(!forced){
                        // out.println("REMOVE " + dataran);
                        node.removeDataran(dataran);
                    }
                        
                    if(node.head == null || forced){
                        if (node.left == null || node.right == null) {
                            node = (node.left == null) ? node.right : node.left;
                        } else {
                            Node mostLeftChild = mostLeftChild(node.right);
                            // out.println(mostLeftChild.key+"/");
                            node.key = mostLeftChild.key;
                            node.head = mostLeftChild.head;
                            node.tail = mostLeftChild.tail;
                            node.right = delete(node.right, node.key, dataran, true);
                        }
                        clear = true;
                    }
                }
            }
            if (node != null && clear) {
                if(node == root && node.right == null && node.left.right == null && node.left.left == null){
                    // out.println("ROOOT HERE"+node.key);
                    return root;
                } 
                node = rebalance(node);
            }
            return node;
        }
    
        private Node mostLeftChild(Node node) {
            Node current = node;
            /* loop down to find the leftmost leaf */
            while (current.left != null) {
                current = current.left;
            }
            return current;
        }
    
        private Node rebalance(Node z) {
            updateHeight(z);
            int balance = getBalance(z);
            if (balance > 1) {
                if (height(z.right.right) > height(z.right.left)) {
                    z = rotateLeft(z);
                } else {
                    z.right = rotateRight(z.right);
                    z = rotateLeft(z);
                }
            } else if (balance < -1) {
                if (height(z.left.left) > height(z.left.right)) {
                    z = rotateRight(z);
                } else {
                    z.left = rotateLeft(z.left);
                    z = rotateRight(z);
                }
            }
            return z;
        }
    
        private Node rotateRight(Node y) {
            Node x = y.left;

            // Node z = x.right;
            // x.right = y;
            // y.left = z;
            // updateHeight(y);
            // updateHeight(x);

            if(x != null){
                Node z = x.right;
                x.right = y;
                y.left = z;
                updateHeight(x);
            }

            
            updateHeight(y);
            return x;
        }
    
        private Node rotateLeft(Node y) {
            Node x = y.right;

            // Node z = x.left;
            // x.left = y;
            // y.right = z;
            // updateHeight(y);
            // updateHeight(x);

            if(x != null){
                Node z = x.left;
                x.left = y;
                y.right = z;
                updateHeight(x);
            }

            updateHeight(y);
            return x;
        }
    
        private void updateHeight(Node n) {
            n.height = 1 + Math.max(height(n.left), height(n.right));
        }
    
        private int height(Node n) {
            return n == null ? -1 : n.height;
        }
    
        public int getBalance(Node n) {
            return (n == null) ? 0 : height(n.right) - height(n.left);
        }

        public Node ceil(Node node, int input)
        {
            // Base case
            if (node == null) {
                return null;
            }
    
            // We found equal key
            if (node.key == input) {
                return node;
            }
    
            // If root's key is smaller,
            // ceil must be in right subtree
            if (node.key < input) {
                return ceil(node.right, input);
            }
    
            // Else, either left subtree or root
            // has the ceil value
            Node ceil = ceil(node.left, input);
            if(ceil == null) return node;
    
            return (ceil.key >= input) ? ceil : node;
        }

        public void preorderTraversal()  
        {  
            preorderTraversal(root);  
        }  

        private void preorderTraversal(Node head)  
        {  
            if (head != null)  
            {  
                preorderTraversal(head.left);
                out.print(head.key+" ");
                head.sanityCheck();  
                out.println();
                // preorderTraversal(head.left);               
                preorderTraversal(head.right);  
            }  
        }

        public void preorderTraversalReal()  
        {  
            preorderTraversalReal(root);  
        }  
        private void preorderTraversalReal(Node head)  
        {  
            if (head != null)  
            {  
                out.print(head.key+" ");  
                preorderTraversalReal(head.left);               
                preorderTraversalReal(head.right);  
            }  
        } 

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