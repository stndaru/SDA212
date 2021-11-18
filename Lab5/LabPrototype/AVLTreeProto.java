package Lab5.LabPrototype;
import java.util.*;

// Source for original tree 
// https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/avltree/AVLTree.java
// A lot of modification has been implemented such as Type attribute, preorder and inorder traversal, and more

class AVLTree {

    class Node {
        int key;
        int height;
        Node left;
        Node right;
        // TYPE IS THE BOX NAME
        HashSet<String> type = new HashSet<String>();
        // CANDY IS THE CANDY TYPE
        HashMap<String, Integer> candy = new HashMap<String, Integer>();

        // added type
        Node(int key, String type, int candy) {
            this.key = key;
            this.type.add(type);
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
            node.type.add(type);
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
            if(node.type.contains(type) || forced){
                node.type.remove(type);
                if(node.type.size() == 0 || forced){
                    if (node.left == null || node.right == null) {
                        node = (node.left == null) ? node.right : node.left;
                        } else {
                            Node mostLeftChild = mostLeftChild(node.right);
                            node.key = mostLeftChild.key;
                            node.type = mostLeftChild.type;
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

    public void inorderTraversal()  
    {  
        inorderTraversal(root);  
    }  
    private void inorderTraversal(Node head)  
    {  
        if (head != null)  
        {  
            inorderTraversal(head.left);  
            System.out.print(head.key+"/"+head.type+" ");  
            inorderTraversal(head.right);  
        }  
    }

    public void inorderTraversalReversed()  
    {  
        inorderTraversalReversed(root);  
    }  
    private void inorderTraversalReversed(Node head)  
    {  
        if (head != null)  
        {  
            inorderTraversalReversed(head.right);  
            System.out.print(head.key+" ");  
            inorderTraversalReversed(head.left);  
        }  
    }

    public void inorderTraversalLimitedLeft(int limit)  
    {  
        inorderTraversalLimitedLeft(root, limit);  
    }  
    private void inorderTraversalLimitedLeft(Node head, int limit)  
    {  
        if (head != null)  
        {  
            if(head.left != null && head.left.key > limit)
                inorderTraversalLimitedLeft(head.left, limit);
            else if(head.left != null)
                inorderTraversalLimitedLeft(head.left.right, limit);
            System.out.print(head.key+"/"+head.type+" ");  
            inorderTraversalLimitedLeft(head.right, limit);  
        }  
    }

    public void inorderTraversalLimitedRight(int limit)  
    {  
        inorderTraversalLimitedRight(root, limit);  
    }  
    private void inorderTraversalLimitedRight(Node head, int limit)  
    {  
        if (head != null)  
        {  
            if(head.right != null && head.right.key < limit) 
                inorderTraversalLimitedRight(head.right, limit);
            else if(head.right != null)
                inorderTraversalLimitedRight(head.right.left, limit);
            System.out.print(head.key+"/"+head.type+" "); 
            inorderTraversalLimitedRight(head.left, limit);  
        }  
    }

    public void inorderTraversalLimitedFull(int limitLeft, int limitRight)  
    {  
        inorderTraversalLimitedFull(root, limitLeft, limitRight);  
    }  
    private void inorderTraversalLimitedFull(Node head, int limitLeft, int limitRight)  
    {  
        if (head != null)  
        {  
            if(head.left != null && head.left.key >= limitLeft) 
                inorderTraversalLimitedFull(head.left, limitLeft, limitRight);
            else if(head.left != null)
                inorderTraversalLimitedFull(head.left.right, limitLeft, limitRight);

            if(limitLeft <= head.key && head.key <= limitRight)
                System.out.print(head.key+"/"+head.type+"/"+head.candy+" ");

            if(head.right != null && head.right.key <= limitRight) 
                inorderTraversalLimitedFull(head.right, limitLeft, limitRight);
            else if(head.right != null)
                inorderTraversalLimitedFull(head.right.left, limitLeft, limitRight);
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
            System.out.print(head.key+"/"+head.type+" ");  
            preorderTraversal(head.left);               
            preorderTraversal(head.right);  
        }  
    } 
}

public class AVLTreeProto {
    public static void main(String[] args) {
        AVLTree obj = new AVLTree();
        obj.insert(10, "A", 1);
        obj.insert(85, "B", 2);
        obj.insert(15, "C", 2);
        obj.insert(70, "D", 2);
        obj.insert(20, "E", 4);
        obj.insert(60, "F", 2);
        obj.insert(30, "G", 2);
        obj.insert(50, "H", 5);
        obj.insert(65, "I", 2);
        obj.insert(80, "J", 2);
        obj.insert(90, "K", 2);
        obj.insert(40, "L", 2);
        obj.insert(5, "M", 2);
        obj.insert(55, "N", 2);

        obj.insert(60, "O", 3);

        // obj.inorderTraversalLimitedLeft(12);
        // System.out.println();
        // obj.inorderTraversalLimitedRight(80);
        // System.out.println();
        obj.inorderTraversalLimitedFull(12, 80);
        System.out.println();

        System.out.println("DELETE =======");
        obj.delete(60, "F");
        obj.delete(60, "O");

        // obj.inorderTraversalLimitedLeft(12);
        // System.out.println();
        // obj.inorderTraversalLimitedRight(80);
        // System.out.println();
        obj.inorderTraversalLimitedFull(12, 80);
        System.out.println();

        // obj.inorderTraversal();
        // System.out.println("\n=======");
        // obj.preorderTraversal();
        // System.out.println("\n=======");
        // obj.inorderTraversalReversed();
        // System.out.println("\n=======");
        // obj.inorderTraversalLimitedLeft(18);
        // System.out.println("DELETE =======");
        // obj.delete(60, "F");
        // obj.delete(60, "O");
        // obj.delete(55, "N");
        //obj.inorderTraversal();
        // System.out.println("\n=======");
        // obj.preorderTraversal();
        // System.out.println("\n"+obj.find(52).key);

    }
    
}

