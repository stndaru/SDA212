package UTS;

public class Something{
    public static void main(String[] args) throws Exception{
    int[] arr = {9, 8, 7, 6, 5, 4, 3};
    doSomething(arr);
    }
    static void cetak(int[] arr){
    for(int x : arr){
    System.out.print(x + " ");
    }
    System.out.println("");
    }
    static void doSomething(int a[]) throws Exception {
     for (int i = a.length-1; i>=0; i--) {
        boolean swapped = false;
        for (int j = 0; j<i; j++) {
            if (a[j] > a[j+1]) {
                int temp = a[j];
                a[j] = a[j+1];
                a[j+1] = temp;
                swapped = true;
            }
        }
     if (!swapped) return;
     swapped = false;
     for (int j = i-1; j > 0; j--) {
     if (a[j] < a[j-1]) {
     int temp = a[j];
     a[j] = a[j-1];
     a[j-1] = temp;
     swapped = true;
     }
     }
     cetak(a);
     if (!swapped) return;
     }
    }
    }
