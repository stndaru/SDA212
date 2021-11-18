package Lab5.TreeTest2;

class AVLTree {

    class Node {
        int key;
        int height;
        Node left;
        Node right;
        String type;
        int desc = 0;

        Node(int key) {
            this.key = key;
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

    public void insert(int key) {
        root = insert(root, key);
        // System.out.println(root.key);
    }

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
            node.desc++;
        } else if (node.key < key) {
            node.right = insert(node.right, key);
            node.desc++;
        } else {
            throw new RuntimeException("duplicate Key!");
        }
        // System.out.println(node.key);
        return rebalance(node);
    }

    private Node delete(Node node, int key) {
        if (node == null) {
            return node;
        } else if (node.key > key) {
            node.left = delete(node.left, key);
        } else if (node.key < key) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null || node.right == null) {
                node = (node.left == null) ? node.right : node.left;
            } else {
                Node mostLeftChild = mostLeftChild(node.right);
                node.key = mostLeftChild.key;
                node.right = delete(node.right, node.key);
            }
        }
        if (node != null) {
            // System.out.println(node.key);
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
                // System.out.println("RL "+z.key);
                z = rotateLeft(z);
            } else {
                // System.out.println("RR "+z.key);
                z.right = rotateRight(z.right);
                // System.out.println("RL "+z.key);
                z = rotateLeft(z);
            }
        } else if (balance < -1) {
            if (height(z.left.left) > height(z.left.right)) {
                // System.out.println("RR "+z.key);
                z = rotateRight(z);
            } else {
                // System.out.println("RL "+z.key);
                z.left = rotateLeft(z.left);
                // System.out.println("RR "+z.key);
                z = rotateRight(z);
            }
        }
        return z;
    }

    private Node rotateRight(Node y) {
        Node x;
        Node z;
        x = y.left;
        if(x!=null){
            z = x.right;
            x.right = y;
            y.left = z;
            updateHeight(y);
            updateHeight(x);
            int val = (z != null) ? z.desc : -1;
            // System.out.println(val);
            // System.out.println(y.desc);
            // System.out.println(x.desc);
            // System.out.println("====");
            y.desc = y.desc - (x.desc + 1) + (val + 1);
            // System.out.println(val);
            // System.out.println(y.desc);
            // System.out.println(x.desc);
            // System.out.println("====");
            x.desc = x.desc - (val + 1) + (y.desc + 1);
            // System.out.println(val);
            // System.out.println(y.desc);
            // System.out.println(x.desc);
            // System.out.println("====");
        }
        
        return x;

        // Node x = y.left;
        // Node z = x.right;
        // x.right = y;
        // y.left = z;
        // updateHeight(y);
        // updateHeight(x);
        // return x;
    }

    private Node rotateLeft(Node y) {
        Node x;
        Node z;
        x = y.right;
        if(x!=null){
            z = x.left;
            x.left = y;
            y.right = z;
            updateHeight(y);
            updateHeight(x);
            // int val = (z != null) ? z.desc : -1;
            // y.desc = y.desc - (x.desc + 1) + (val + 1);
            // x.desc = x.desc - (val + 1) + (y.desc + 1);
        }
        
        
        return x;

        // Node x = y.right;
        // Node z = x.left;
        // x.left = y;
        // y.right = z;
        // updateHeight(y);
        // updateHeight(x);
        // return x;
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

    public void inorderTraversal()  
    {  
        inorderTraversal(root);  
    }  
    private void inorderTraversal(Node head)  
    {  
        if (head != null)  
        {  
            inorderTraversal(head.left);  
            System.out.print(head.key+" ");  
            inorderTraversal(head.right);  
        }  
    } 
    public void preorderTraversal()  
    {  
        preorderTraversal(root);  
    }  
    private void preorderTraversal(Node head)  
    {  
        if (head != null)  
        {  
            System.out.print(head.key+"/"+head.desc+" ");  
            preorderTraversal(head.left);               
            preorderTraversal(head.right);  
        }  
    } 

    public int countGreater(int x){
        int res = 0;
        Node head = root;

        while(head != null){
            int desc = (head.right != null) ? head.right.desc : -1;

            if(head.key > x){
                res = res + desc + 1 + 1;
                head = head.left;
            }
            else if(head.key < x)
                head = head.right;
            else{
                res = res + desc + 1;
                break;
            }
        }

        return res;
    }

    public int countLesser(int x){
        int res = 0;
        Node head = root;

        while(head != null){
            int desc = (head.left != null) ? head.left.desc : -1;

            if(head.key < x){
                res = res + desc + 1 + 1;
                head = head.right;
            }
            else if(head.key > x)
                head = head.left;
            else{
                res = res + desc + 1;
                break;
            }
        }
        return res;
    }
}

public class AVLTreeTest2 {
    public static void main(String[] args) {
        AVLTree obj = new AVLTree();
        // obj.insert(22);
        // obj.insert(14);
        // obj.insert(38);
        // obj.insert(10);
        // obj.insert(5);
        // obj.insert(13);
        // obj.insert(8);
        // obj.insert(12);
        // obj.insert(65);
        // obj.insert(80);
        // obj.insert(90);
        // obj.insert(40);
        // obj.insert(5);
        // obj.insert(55);

        obj.insert(6);
        obj.insert(3);
        obj.insert(5);
        obj.insert(1);
        obj.preorderTraversal();
        System.out.println();
        obj.delete(1);
        obj.delete(5);


        // private Node rotateRight(Node y) {
        //     // Basically beban y dioffload ke beban x
        //     // dan val/beban x kanan dipindah ke beban y
        //     // hmw nuker jadi beban 

        //     // y = 8
        //     Node x;
        //     Node z;
        //     x = y.left;
        //     // x = 4
        //     if(x!=null){
        //         z = x.right;
        //         // z = null
        //         x.right = y;
        //         // 4.right = 8
        //         y.left = z;
        //         // 8.left = null
        //         updateHeight(y);
        //         updateHeight(x);
        //         int val = (z != null) ? z.desc : -1;
        //         // val = -1
        //         y.desc = y.desc - (x.desc + 1) + (val + 1);
        //         // 8.desc = 2 - (1+1) = 0
        //         x.desc = x.desc - (val + 1) + (y.desc + 1);
        //         // 4.desc = 1 - (2 + 1) + 4 = 2
        //     }
            
        //     return x;



        // obj.insert(1);
        // obj.insert(12);

        // obj.inorderTraversal();
        // System.out.println("\n=======");
        obj.preorderTraversal();
        System.out.println();

        // System.out.println(obj.countGreater(4));
        // System.out.println(obj.countLesser(4));
        // System.out.println("\nDELETE =======");
        // obj.delete(60);
        // obj.inorderTraversal();
        // System.out.println("\n=======");
        // obj.preorderTraversal();
        // obj.delete(12);
        // obj.delete(1);
        // obj.preorderTraversal();
        // System.out.println();
        // obj.delete(6);
        // obj.preorderTraversal();

    }
    
}
