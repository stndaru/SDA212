import java.util.*;

public class UAS {
    public static void main(String[] args) {

        int[] heap = new int[] {85, 20, 70, 18, 10, 12, 60};
        System.out.println(mystery(heap));
        
    }

    static class Node {
        int data;
        Node left;
        Node right;
    }
    public void cetakSisiKiri(Node n){
        Stack<Integer> stack = new Stack<Integer>();
        int temp = -1;
        Node tempNode = n;

        while(n != null){
            stack.push(n.data);
            n = n.left;
        }

        temp = stack.pop();

        while(temp != tempNode.data){
            System.out.println(temp);
            temp = stack.pop();
        }

        System.out.println(temp);
    }


    static boolean mystery(int[] heap) {
        for (int i = 0; i < heap.length / 2; i++) {
          int j = (i * 2) + 1;
          System.out.println(heap[i] + "/" + heap[j] + "/" + heap[++j]);
          j = (i * 2) + 1;
          if (j < heap.length && heap[i] < heap[j++])
            return false;
          if (j < heap.length && heap[i] < heap[j])
            return false;
        }
        return true;
    }

    // HashEntry temp = array[currentPos];

    // if(temp != null){
    //     while(temp.next != null){
    //         temp = temp.next;
    //     } 

    //     temp.next = new HashEntry(x);
    // }

    // else{
    //     array[currentPos] = new HashEntry(x);
    // }
}
