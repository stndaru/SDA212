package Lab6;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class Lab6v1 {
    private static InputReader in;
    private static PrintWriter out;
    public static List<Dataran> dataranList = new ArrayList<>();
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
        
        int N = in.nextInt();
        for (int i = 0; i < N; i++) {
            int height = in.nextInt();
            if(i == 0){
                temp = new Dataran(height, null, null);
            }
            else{
                temp = new Dataran(height, dataranList.get(i-1), null);
                dataranList.get(i-1).next = temp;
            }
            temp.order = i;
            dataranList.add(temp);
            tempNode = dataranTree.insert(height, temp);
        }

        int Q = in.nextInt();
        while(Q-- > 0) {
            String query = in.next();
            if (query.equals("A")) {
                int height = in.nextInt();
                temp = new Dataran(height, dataranList.get(dataranList.size()-1), null);
                temp.order = dataranList.size();
                dataranList.get(dataranList.size()-1).next = temp;
                dataranList.add(temp);
                dataranTree.insert(height, temp);

            } else if (query.equals("U")) {
                // Problem here, how to update AVLTree?
                // What if there exist a height that it changes?
                // How to sort?
                // O(n) approach
                int targetIndex = in.nextInt();
                int endHeight = in.nextInt();
                Dataran targetDataran = dataranList.get(targetIndex);

                dataranTree.delete(targetDataran.height, targetDataran);

                tempNode = dataranTree.find(endHeight);
                // out.println(tempNode+"/"+tempNode.tail);
                if(tempNode == null) dataranTree.insert(endHeight, targetDataran);
                else tempNode.addBasedOnOrder(targetDataran);

                targetDataran.height = endHeight;

            } else if (query.equals("SANITY")) {
                out.println("== SANITY CHECK ==");
                for(int i = 0; i < dataranList.size(); i++)
                    out.print(dataranList.get(i).height + " ");

                out.println("\nTREE: ");
                dataranTree.preorderTraversal();
                out.println();

            } else {
                // Same problem as U
                Node lowest = dataranTree.ceil(dataranTree.root, 1);
                Dataran target = lowest.head;
                int maxHeight;

                prev = (target.order-1 > -1) ? dataranList.get(target.order-1) : null;
                next = (target.order+1 < dataranList.size()) ? dataranList.get(target.order+1) : null;
                maxHeight = (prev == null) ? target.height : Math.max(target.height, prev.height);
                maxHeight = (next == null) ? maxHeight : Math.max(maxHeight, next.height);

                tempNode = dataranTree.find(maxHeight);

                if(prev != null && prev.height != maxHeight){
                    dataranTree.delete(prev.height, prev);
                    prev.height = maxHeight;
                    if(tempNode == null) tempNode = dataranTree.insert(maxHeight, prev);
                    else tempNode.addBasedOnOrder(prev);
                }
                if(next != null && next.height != maxHeight){
                    dataranTree.delete(next.height, next);
                    next.height = maxHeight;
                    if(tempNode == null) tempNode = dataranTree.insert(maxHeight, next);
                    else tempNode.addBasedOnOrder(next);
                }
                if(target.height != maxHeight){
                    dataranTree.delete(target.height, target);
                    target.height = maxHeight;
                    if(tempNode == null) tempNode = dataranTree.insert(maxHeight, target);
                    else tempNode.addBasedOnOrder(target);
                }

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
        HashMap<Dataran, Dataran[]> dataranMap = new HashMap<>();
        Node left;
        Node right;

        Node(int key) {
            this.key = key;
        }

        void addDataran(Dataran dataran){
            if(head == null){
                dataranMap.put(dataran, new Dataran[] {null, dataran, null});
                head = dataran;
            }
            else {
                dataranMap.put(dataran, new Dataran[] {tail, dataran, null});
                dataranMap.get(tail)[2] = dataran;
            }
            tail = dataran;
        }

        void addBasedOnOrder(Dataran dataran){
            temp = head;

            // case for head
            if(head.order > dataran.order){
                dataranMap.get(head)[0] = dataran;
                dataranMap.put(dataran, new Dataran[] {null, dataran, head});
                
                head = dataran;
                return;
            }

            // case for body
            while(temp != null){
                if(temp.order > dataran.order){
                    tempPrev = dataranMap.get(temp)[0];
                    dataranMap.get(tempPrev)[2] = dataran;
                    dataranMap.get(temp)[0] = dataran;
                    dataranMap.put(dataran, new Dataran[] {tempPrev, dataran, temp});
                    return;
                }
                if(dataranMap.get(temp)[2] == null) break;
                temp = dataranMap.get(temp)[2];
            }

            // case for tail
            if(temp == tail){
                dataranMap.put(dataran, new Dataran[] {temp, dataran, null});
                dataranMap.get(temp)[2] = dataran;
                tail = dataran;
            }
            return;
        }

        void removeDataran(Dataran dataran){
            if(dataran == head && head == tail){
                head = null;
                tail = null;
            }
            else if(dataran == head){
                head = dataranMap.get(dataran)[2];
                dataranMap.get(head)[0] = null;
            }
            else if(dataran == tail){
                tail = dataranMap.get(dataran)[0];
                dataranMap.get(tail)[2] = null;
            }
            else {
                tempPrev = dataranMap.get(dataran)[0];
                tempNext = dataranMap.get(dataran)[2];
                dataranMap.get(tempPrev)[2] = tempNext;
                dataranMap.get(tempNext)[0] = tempPrev;
            }
            dataranMap.remove(dataran);
        }

        void sanityCheck(){
            out.print("HEAD: " + head + " ");
            for(Dataran[] i : dataranMap.values()){
                out.print("(" + i[0] + "/" + i[1] + "/" + i[2] + ") ");
            }
            out.print("TAIL: " + tail + " ");
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
            root = insert(root, key);
            Node node = find(key);
            node.addDataran(dataran);
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
                if(node.dataranMap.get(dataran) != null || forced){
                    node.removeDataran(dataran);
                    if(node.head == null || forced){
                        if (node.left == null || node.right == null) {
                            node = (node.left == null) ? node.right : node.left;
                        } else {
                            Node mostLeftChild = mostLeftChild(node.right);
                            node.key = mostLeftChild.key;
                            node.head = mostLeftChild.head;
                            node.tail = mostLeftChild.tail;
                            node.dataranMap = mostLeftChild.dataranMap;
                            node.right = delete(node.right, node.key, dataran, true);
                        }
                        clear = true;
                    }  
                }
            }
            if (node != null && clear) {
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
            Node z = x.right;
            x.right = y;
            y.left = z;
            updateHeight(y);
            updateHeight(x);
            return x;
        }
    
        private Node rotateLeft(Node y) {
            Node x = y.right;
            Node z = x.left;
            x.left = y;
            y.right = z;
            updateHeight(y);
            updateHeight(x);
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