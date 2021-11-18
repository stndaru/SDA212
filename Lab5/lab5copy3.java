package Lab5;
import java.io.*;
import java.util.*;

public class lab5copy3 {
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);
    public static AVLTree obj = new AVLTree();
    public static HashMap<String, Integer> itemList = new HashMap<>();

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
            // obj.inorderTraversalLimitedFull(0, 100);
        }

        out.flush();
    }

    //TODO
    static String handleBeli(int L, int R){
        return obj.getMaxDiff(L, R);
    }

    //TODO
    static void handleStock(String nama, int harga, int tipe){
        obj.insert(harga, nama, tipe);
        itemList.put(nama, harga);
    }

    //TODO
    static void handleSoldOut(String nama){
        obj.delete(itemList.get(nama), nama);
        itemList.remove(nama);
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
        // A lot of modification has been implemented such as Type attribute, preorder and inorder traversal, and more

        // {permen:harga}
        public HashMap<Integer, Integer> candyList = new HashMap<Integer, Integer>();
    
        class Node {
            // KEY IS THE PRICE
            int key;
            int height;
            Node left;
            Node right;
            // CANDY IS THE CANDY TYPE {Box Name : Candy Type}
            HashMap<String, Integer> candy = new HashMap<String, Integer>();
    
            // added type
            Node(int key, String type, int candy) {
                this.key = key;
                this.candy.put(type, candy);
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
        public void insert(int key, String type, int candy) {
            root = insert(root, key, type, candy);
        }
    
        // added type
        public void delete(int key, String type) {
            root = delete(root, key, type, false);
        }
    
        public Node getRoot() {
            return root;
        }
    
        public int height() {
            return root == null ? -1 : root.height;
        }
    
        private Node insert(Node node, int key, String type, int candy) {
            if (node == null) {
                return new Node(key, type, candy);
            } else if (node.key > key) {
                node.left = insert(node.left, key, type, candy);
            } else if (node.key < key) {
                node.right = insert(node.right, key, type, candy);
            } else {
                node.candy.put(type, candy);
            }
            return rebalance(node);
        }
    
        private Node delete(Node node, int key, String type, boolean forced) {
            boolean clear = false;
    
            if (node == null) {
                return node;
            } else if (node.key > key) {
                node.left = delete(node.left, key, type, forced);
            } else if (node.key < key) {
                node.right = delete(node.right, key, type, forced);
            } else {
                if(node.candy.containsKey(type) || forced){
                    node.candy.remove(type);
                    if(node.candy.size() == 0 || forced){
                        if (node.left == null || node.right == null) {
                            node = (node.left == null) ? node.right : node.left;
                            } else {
                                Node mostLeftChild = mostLeftChild(node.right);
                                node.key = mostLeftChild.key;
                                node.candy = mostLeftChild.candy;
                                node.right = delete(node.right, node.key, type, true);
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
                    // HERE TO ITERATE ELEMENT
                    for(int i : head.candy.values())
                        candyList.put(i, head.key);
                }
    
                if(head.right != null && head.right.key <= limitRight) 
                    inorderTraversalLimitedFull(head.right, limitLeft, limitRight);
                else if(head.right != null)
                    inorderTraversalLimitedFull(head.right.left, limitLeft, limitRight);
            }  
        }
    
        public String getMaxDiff(int a, int b) {
            String result = "";
            int minTemp = 2147483646;
            int maxTemp = -1;
    
            this.inorderTraversalLimitedFull(a, b);
    
            

            candyList.clear();

            result += minTemp + " " + maxTemp;
            return result;
        }
    }
}