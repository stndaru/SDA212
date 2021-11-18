import java.util.*;

public class Test4 {
    public static void main(String[] args) {

        Scanner inp = new Scanner(System.in);

        int N = inp.nextInt();
        int[][] result = new int[N][N+1];

        String op = inp.next();

        for(int i = 0; i < N; i++){
            for(int j = 0; j < N+1; j++){

                if(j == 0)
                    result[i][j] = i+1;

                else{
                    switch(op){
                        case "+":
                            result[i][j] = (i+1)+j;
                            break;
                        case "-":
                            result[i][j] = (i+1)-j;
                            break;
                        case "*":
                            result[i][j] = (i+1)*j;
                            break;
                        case "/":
                            result[i][j] = (i+1)/j;
                            break;
                    }
                }
            }
        }

        
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N+1; j++){
                System.out.print(result[i][j]);
            }
            System.out.println();
        }

        inp.close();
    }
}
