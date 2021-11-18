package Lab5;
import java.io.*;
import java.util.*;

public class lab5copy4 {
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);
    // Candy Type and their price tree
    public static HashMap<Integer, AVLTree> candyTypes = new HashMap<>();
    // Boxes and their data {Box : [type, price]}
    public static HashMap<String, Integer[]> boxes = new HashMap<>();

    public static void main(String[] args) {
       
        //Menginisialisasi kotak sebanyak N
        int N = in.nextInt();
        for(int i = 0; i < N; i++){
            String nama = in.next();
            int harga = in.nextInt();
            int tipe = in.nextInt();
            handleStock(nama, harga, tipe);
        }

        //Query 
        //(method dan argumennya boleh diatur sendiri, sesuai kebutuhan)
        int NQ = in.nextInt();
        for(int i = 0; i < NQ; i++){
            String Q = in.next();
            if (Q.equals("BELI")){
                int L = in.nextInt();
                int R = in.nextInt();
                out.println(handleBeli(L, R));

            }else if(Q.equals("STOCK")){
                String nama = in.next();
                int harga = in.nextInt();
                int tipe = in.nextInt();
                handleStock(nama, harga, tipe);

            }else{ //SOLD_OUT
                String nama = in.next();
                handleSoldOut(nama);

            }
        }

        out.flush();
    }

    //TODO
    static String handleBeli(int L, int R){
        try {
            /*
             CURRENT IDEA: Currently im able to find lowest and highest value of candies
             Now its just a matter of being able to iterate and combine the lowest and highest candy value
             with differing type in order to gain highest difference
            */

            int maxVal = -1;
            int minVal = 2147483646;
            int tempDif = 0;
            HashMap<Integer, Integer[]> minMax = new HashMap<Integer, Integer[]>();

            for(int i : candyTypes.keySet()){
                // candyTypes.get(i).inorderTraversalLimitedFull(0,120);
                int tempMax = candyTypes.get(i).maxvalue(L,R);
                int tempMin = candyTypes.get(i).minvalue(L,R);

                tempMax = (tempMax == -1) ? tempMin : tempMax;
                tempMax = (tempMax < L) ? -1 : tempMax;

                tempMin = (tempMin == 2147483646) ? tempMax : tempMin;
                tempMin = (tempMin > R) ? 2147483646 : tempMin;

                tempMax = (tempMin == 2147483646) ? -1 : tempMax;

                
                // out.println(i + " VALUE :"+tempMin+"/"+tempMax);
                // candyTypes.get(i).inorderTraversalLimitedFull(0,100);
                // out.println();

                minMax.put(i, new Integer[]{tempMin, tempMax});
            }

            for(int i : minMax.keySet()){
                out.println(i+ " " +Arrays.toString(minMax.get(i)));
            }
            
            if(maxVal == -1 || minVal == 2147483646) return "-1 -1";
            return minVal+" "+maxVal;
        }

        catch (Exception e){
        }
        
        return "";
    }

    //TODO
    static void handleStock(String nama, int harga, int tipe){
        if(!candyTypes.containsKey(tipe))
            candyTypes.put(tipe, new AVLTree());

        candyTypes.get(tipe).insert(harga);
        boxes.put(nama, new Integer[]{tipe, harga});
    }

    //TODO
    static void handleSoldOut(String nama){
        candyTypes.get(boxes.get(nama)[0]).delete(boxes.get(nama)[1]);
        if(candyTypes.get(boxes.get(nama)[0]).isEmpty())
            candyTypes.remove(boxes.get(nama)[0]);
        boxes.remove(nama);
    }


    // taken from https://codeforces.com/submissions/Petr
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

    static class AVLTree {
        // Source for original tree 
        // https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/avltree/AVLTree.java
        // https://www.javatpoint.com/avl-tree-program-in-java
        // A lot of modification has been implemented such as Type attribute, preorder and inorder traversal, and more

    
        class Node {
            // KEY IS THE PRICE
            int key;
            int height;
            Node left;
            Node right;
            int amount;
    
            // added type
            Node(int key) {
                this.key = key;
                this.amount = 1;
            }
        }
    
        private Node root;
    
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
    
        // added type
        public void insert(int key) {
            root = insert(root, key);
        }

        public boolean isEmpty(){
            if(root == null) return true;
            return false;
        }
    
        // added type
        public void delete(int key) {
            root = delete(root, key);
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
                node.amount += 1;
            }
            return rebalance(node);
        }
    
        private Node delete(Node node, int key) {
            boolean clear = false;
    
            if (node == null) {
                return node;
            } else if (node.key > key) {
                node.left = delete(node.left, key);
            } else if (node.key < key) {
                node.right = delete(node.right, key);
            } else {
                node.amount -= 1;
                if(node.amount == 0){
                    if (node.left == null || node.right == null) {
                        node = (node.left == null) ? node.right : node.left;
                        } else {
                            Node mostLeftChild = mostLeftChild(node.right);
                            node.key = mostLeftChild.key;
                            node.right = delete(node.right, node.key);
                        }
                    clear = true;
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
    
        ////////////////// THE TRAVERSALS //////////////////
    
        public void inorderTraversalLimitedFull(int limitLeft, int limitRight)  
        {  
            inorderTraversalLimitedFull(root, limitLeft, limitRight);  
        }  
        public void inorderTraversalLimitedFull(Node head, int limitLeft, int limitRight)  
        {  
            if (head != null)  
            {  
                if(head.left != null && head.left.key >= limitLeft) 
                    inorderTraversalLimitedFull(head.left, limitLeft, limitRight);
                else if(head.left != null)
                    inorderTraversalLimitedFull(head.left.right, limitLeft, limitRight);
    
                if(limitLeft <= head.key && head.key <= limitRight){
                    out.print(head.key+" ");
                }
    
                if(head.right != null && head.right.key <= limitRight) 
                    inorderTraversalLimitedFull(head.right, limitLeft, limitRight);
                else if(head.right != null)
                    inorderTraversalLimitedFull(head.right.left, limitLeft, limitRight);
            }  
        }
    
        // https://www.geeksforgeeks.org/find-the-minimum-element-in-a-binary-search-tree/?ref=lbp
        public int minvalue(int limitLeft, int limitRight) {
            Node current = root;

            if(current.key < limitLeft)
                return 2147483646;
     
            /* loop down to find the leftmost leaf */
            while (current.left != null && current.left.key >= limitLeft) {
                current = current.left;
            }

            return (current.key);
        }

        public int maxvalue(int limitLeft, int limitRight) {
            Node current = root;

            if(current.key > limitRight)
                return -1;
     
            /* loop down to find the leftmost leaf */
            while (current.right != null && current.right.key <= limitRight) {
                current = current.right;
            }

            return (current.key);
        }
    }
}