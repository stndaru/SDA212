package Lab5.LabTesting;

import java.util.*;

// Source for original tree 
// https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/avltree/AVLTree.java
// A lot of modification has been implemented such as Type attribute, preorder and inorder traversal, and more

class AVLTree {
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
                // HERE TO ITERATE ELEMENT
                System.out.print(head.key+" ");
            }

            if(head.right != null && head.right.key <= limitRight) 
                inorderTraversalLimitedFull(head.right, limitLeft, limitRight);
            else if(head.right != null)
                inorderTraversalLimitedFull(head.right.left, limitLeft, limitRight);
        }  
    }

    // https://www.geeksforgeeks.org/find-the-minimum-element-in-a-binary-search-tree/?ref=lbp
    public int minvalue() {
        Node current = root;
 
        /* loop down to find the leftmost leaf */
        while (current.left != null) {
            current = current.left;
        }
        return (current.key);
    }

    public int maxvalue() {
        Node current = root;
 
        /* loop down to find the leftmost leaf */
        while (current.right != null) {
            current = current.right;
        }
        return (current.key);
    }
}

public class AVLTreeTesting {
    // [{Price : Candies}]
    public static void main(String[] args) {
        AVLTree obj = new AVLTree();
        obj.insert(10);
        obj.insert(85);
        obj.insert(15);
        obj.insert(70);
        obj.insert(20);
        obj.insert(60);
        obj.insert(30);
        obj.insert(50);
        obj.insert(65);
        obj.insert(80);
        obj.insert(90);
        obj.insert(40);
        obj.insert(5);
        obj.insert(55);

        obj.insert(60);

        obj.inorderTraversalLimitedFull(0, 120);
        System.out.println();

        obj.delete(60);
        obj.inorderTraversalLimitedFull(0, 120);
        System.out.println();
        
        obj.delete(60);
        obj.inorderTraversalLimitedFull(0, 120);
        System.out.println();

    }
    
}