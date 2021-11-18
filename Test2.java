import java.util.*;
public class Test2 {
    public static void main(String[] args) {
        //System.out.println(7/2);
        LinkedList<String> t = new LinkedList<String>();
        
        // for(int i = 1; i < 17; i++){
        // yes(i,t);
        // System.out.println(t.size());
        // t.clear();
        // }
        // t.add("One");
        // t.add("Two");
        // t.add("Three");
        // Iterator<String> iter = t.iterator();

        // while(iter.hasNext())
        //     System.out.println(iter.next());


        // // Create a LinkedList
        // LinkedList<String> linkedlist = new LinkedList<String>();
    
        // // Add elements to LinkedList
        // linkedlist.add("Delhi");
        // linkedlist.add("Agra");
        // linkedlist.add("Mysore");
        // linkedlist.add("Chennai");
        // linkedlist.add("Pune");
    
        // // Obtaining Iterator
        // Iterator<String> it = linkedlist.iterator();
    
        // // Iterating the list in forward direction
        // System.out.println("LinkedList elements:");
        // while(it.hasNext()){
        // System.out.println(it.next());
        // }


        Map<String, String[]> hm = new HashMap<String, String[]>();
        hm.put("1", new String[] {"one", "two"});
        hm.put("2", new String[] {"three", "four"});
        hm.put("1", new String[] {"five", "six"});
        String[] a = hm.get("7");
        System.out.println(a);
    }

    // Time Complexity: O(n)
    static void yes(int n, LinkedList<String> t){
        if(n > 1){
            yes(n/2,t);
            yes(n/2,t);
        }
        t.add("*");
    }
}
