package UTS;
import java.util.*;

public class Test2 {
    public static void main(String[] args) {
        int[] res = {8,2,1,3,7,4};
        some(res);
    }

    static void some(int[] a){
        for( int p = 1; p < a.length; p++){
            int temp = a[p];
            int q = p;
            for(; q > 0 && temp < a[q-1]; q--){
                a[q] = a[q-1];
            }
            a[q] = temp;
            //System.out.println(Arrays.toString(a));
        }
        // System.out.println(Arrays.toString(a));
        thing(a);
        System.out.println(Arrays.toString(a));
    }

    static void thing(int a[]){
        for(int i = 0; i < a.length; i++){
            int max = i;
            for(int j = i+1; j < a.length; j++){
                if (a[max] < a[j]) max = j;
            }
            int T= a[i];
            a[i] = a[max];
            a[max] = T;
            //System.out.println(Arrays.toString(a));
        }
    }
}
