import java.lang.reflect.Array;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        // Map<String,String> myMap = new HashMap<String,String>();
        // myMap.put("123","Fasilkom");
        // myMap.put("234","Fasilkom");
        // myMap.put("123", "FISIP");
        // myMap.get("123");
        // myMap.put("345","FEB");
        // myMap.remove("234");
        // System.out.println(myMap.size());

        int data = 10;

        // Stack<Integer> res = new Stack<Integer>();
        // while(data > 0){
        //     res.push(data%2);
        //     data /= 2;
        // }

        //while(!res.isEmpty()) System.out.println(res.pop());

        String data2 = "strukturdataalgoritma";

        Stack<Map<String,String>> stackMap = new Stack<Map<String,String>>();
        ArrayList<Integer> lst = new ArrayList<>();
        lst.add(5);
        lst.add(7);
        System.out.println(lst);

        // Stack<Character> res1 = new Stack<Character>();
        // for(int i = 0; i < data2.length(); i++){
        //     res1.push(data2.charAt(i));
        // }
        // while(!res1.isEmpty()) System.out.print(res1.pop());

        Stack<String> strStack = new Stack<String>();
        strStack.push("One");
        strStack.push("Two");
        strStack.push("One");
        while(!strStack.isEmpty()) System.out.println(strStack.pop());
    }

    // Time Complexity: O(n)
    void mystery(String[] myData, Map<String,String> myMap) {
        for (int i = 0; i < myData.length; i++) 
            myMap.put(myData[i], myData[i]);
    }

    // Time Complexity: O(n^2)
    void second(String[] myData, String[] secondData){
        for(String i : myData){
            for(String a : secondData){
                continue;
            }
        }
    }


}



