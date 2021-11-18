package CF;

import java.util.*;

public class Codeforces1251A {
    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);

        int N = inp.nextInt();
        inp.nextLine();

        String[] result = new String[N];

        for(int i = 0; i < N; i++){
            String check = inp.nextLine();
            result[i] = checkResult(check);
            System.out.println(result[i]);
        }

        // for(String str : result) System.out.println(str);

        inp.close();
    }

    public static String checkResult(String in){
        int stay = 0;
        int calc;
        char a;

        // track is i
        // when different char found, stay will go to track
        // and then track will move
        // when it is different, calculate diff
        // if even then it is broken
        // stay then go to track

        Set<Character> tempChar = new HashSet<Character>();

        for(int i = 0; i < in.length(); i++){
            a = in.charAt(i);

            if(in.length() == 1) tempChar.add(Character.valueOf(in.charAt(i)));

            else if(i == in.length()-1){
                if( a != in.charAt(i-1) || ((i+1) - stay) % 2 != 0)
                    tempChar.add(Character.valueOf(in.charAt(i)));
            }

            else if(i > 0 && a != in.charAt(i-1)){
                calc = i - stay;

                if(calc % 2 != 0){
                    tempChar.add(Character.valueOf(in.charAt(i-1)));
                }

                stay = i;
            }

        }

        Character[] tempCharArr = new Character[tempChar.size()];
        tempChar.toArray(tempCharArr);
        Arrays.sort(tempCharArr);

        String res = "";

        for(Character chars : tempCharArr){
            res += chars;
        }

        return res;
    }
}
