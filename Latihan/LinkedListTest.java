package Latihan;
// Part of Latihan 30 September 2021 SDA

public class LinkedListTest {
    public static void main(String[] args) {
      // default testcase
      MyList list1 = new MyList();
      list1.add(1); list1.add(2); list1.add(3); list1.add(4); list1.add(5);
      System.out.println(list1);
  
      MyList list2 = new MyList();
      list2.add(3); list2.add(5); list2.add(6); list2.add(7);
      System.out.println(list2);

      MyList diff = myFunc(list1, list2);
      System.out.println(diff);

      // // secondary testcase
      // MyList list3 = new MyList();
      // list3.add(5); list3.add(6);
      // System.out.println("\n" + list3);
      // System.out.println(list2);
  
      // MyList diff2 = myFunc(list3, list2);
      // System.out.println(diff2);

      // // third testcase
      // MyList list4 = new MyList();
      // list4.add(5); list4.add(7); list4.add(9); list4.add(12);
      // System.out.println("\n" + list4);
      // System.out.println(list2);
  
      // MyList diff3 = myFunc(list4, list2);
      // System.out.println(diff3);

      // // fourth testcase
      // MyList list5 = new MyList();
      // list5.add(1); list5.add(2); 
      // System.out.println("\n" + list5);
      // System.out.println(list2);
  
      // MyList diff4 = myFunc(list5, list2);
      // System.out.println(diff4);

      // // fifth testcase
      // MyList list6 = new MyList();
      // list6.add(4);
      // System.out.println("\n" + list6);
      // System.out.println(list2);
  
      // MyList diff5 = myFunc(list6, list2);
      // System.out.println(diff5);

      // // sixth testcase
      // MyList list7 = new MyList();
      // list7.add(8); list7.add(9); list7.add(10);
      // System.out.println("\n" + list7);
      // System.out.println(list2);
  
      // MyList diff6 = myFunc(list7, list2);
      // System.out.println(diff6);
    }
  
    static MyList myFunc(MyList list1, MyList list2) {
      // Implementation here
      // assume already sorted

      // if A reached final, end
      // if B reached final, add the rest of A until empty
      // if A == B, don't add and increase A pointer
      // if A != B && A > B, don't add and kept increase B pointer
      // if A != B && A < B, add A, don't increase pointer B, increase pointer A

      MyList result = new MyList();
      Node ls1Node = list1.head;
      Node ls2Node = list2.head;

      while(ls1Node != null){

        if(ls2Node == null){
          result.add(ls1Node.value);
          ls1Node = ls1Node.next;
        }

        else if(ls1Node.value == ls2Node.value){
          ls1Node = ls1Node.next;
        }

        else if(ls1Node.value > ls2Node.value){
          ls2Node = ls2Node.next;
        }

        else if(ls1Node.value < ls2Node.value){
          result.add(ls1Node.value);
          ls1Node = ls1Node.next;
        }
      
      }

      return result;
    }
  }
  
  class MyList {
    Node head;
    
    MyList() {
      head = null;
    }
  
    void add(int val) {
      if (head == null)
        head = new Node(val, null);
      else {
        Node temp = head;
        while (temp.next != null) {
          temp = temp.next;
        }
        temp.next = new Node(val, null);
      }
    }
  
    public String toString() {
      String s = "";
      Node temp = head;
      while (temp != null) {
        s += temp.value;
        temp = temp.next;
        if (temp != null)
          s += (" ");
      }
      return s;
    }
  }
  
  class Node {
    int value;
    Node next;
  
    Node(int v, Node n) {
      value = v;
      next = n;
    }
  }