package Latihan;
import java.util.*;

public class Latihan23Sep {
    public static void main(String[] args) {
        int[] arr1 = { 4, 5, 9 };
        int[] arr2 = { 1, 3, 7, 8 };
        int[] arr3 = { 2, 6 };
        int[] result = merge3(arr1, arr2, arr3);
        System.out.println(Arrays.toString(result));
      }
      
      static int[] merge3(int[] arr1, int[] arr2, int[] arr3) {
        int[] result = new int[arr1.length + arr2.length + arr3.length];
        int p1 = 0;
        int p2 = 0;
        int p3 = 0;
        // int temp1, temp2, temp3 = 0;
        int min = 0;

        for(int i = 0; i < (arr1.length + arr2.length + arr3.length); i++){
            min = 0;
            // ketiga masih ada
            if(p1 < arr1.length && p2 < arr2.length && p3 < arr3.length){
              if(arr1[p1] < arr2[p2] && arr1[p1] < arr3[p3]){
                  min = arr1[p1];
                  p1++;
              }
              else if(arr2[p2] < arr3[p3]){
                min = arr2[p2];
                p2++;
              }
              else{
                min = arr3[p3];
                p3++;
              }
            }

            // p1 p2
            else if(p1 < arr1.length && p2 < arr2.length){
                if(arr1[p1] < arr2[p2]){
                    min = arr1[p1];
                    p1++;
                }
                else{
                    min = arr2[p2];
                    p2++;
                }
            }

            // p1 p3
            else if(p1 < arr1.length && p3 < arr3.length){
                if(arr1[p1] < arr3[p3]){
                    min = arr1[p1];
                    p1++;
                }
                else{
                    min = arr3[p3];
                    p3++;
                }
            }

            // p2 p3
            else if(p2 < arr2.length && p3 < arr3.length){
                if(arr2[p2] < arr2[p2]){
                    min = arr2[p2];
                    p1++;
                }
                else{
                    min = arr3[p3];
                    p3++;
                }
            }

            // p1
            else if(p1 < arr1.length){
                min = arr1[p1];
                p1++;
            }

            // p2
            else if(p2 < arr2.length){
                min = arr2[p2];
                p2++;
            }

            // p3
            else if(p3 < arr3.length){
                min = arr3[p3];
                p3++;
            }
            
            result[i] = min;
        }
    
      return result;
    }
}
