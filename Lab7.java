import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;


public class Lab7 {
    private static InputReader in;
    private static PrintWriter out;
    static int[][] graph;
    static int[][] resultArray;
    final static int INF = 147483647;
    
    // TODO: method to initialize graph
    public static void createGraph(int N) {
        graph =  new int[N][N];
        // sanityCheck();
        for (int[] row: graph)
            Arrays.fill(row, INF);
    }

    // TODO: method to create an edge with type==T that connects vertex U and vertex V in a graph
    public static void addEdge(int U, int V, int T) {
        graph[U-1][V-1] = T;
        graph[V-1][U-1] = T;
    }

    // TODO: Handle teman X Y K
    public static int canMudik(int X, int Y, int K) {
        if(resultArray[X-1][Y-1] == INF || resultArray[X-1][Y-1] > K)
            return 0;
        return 1;
    } 

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        int M = in.nextInt();
        int Q = in.nextInt();
        createGraph(N);
        
        for (int i=0;i < M;i++) {
            int U = in.nextInt();
            int V = in.nextInt();
            int T = in.nextInt();
            addEdge(U, V, T);
        }

        // sanityCheck();

        AllPairShortestPath result = new AllPairShortestPath();
        resultArray = result.floydWarshall(graph, N);

        while(Q-- > 0) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int K = in.nextInt();
            out.println(canMudik(X, Y, K));
        }

        // sanityCheck(resultArray);

        out.flush();
    }

    static void sanityCheck(int[][] graph){
        for (int i=0; i<graph.length; ++i)
            {
                for (int j=0; j<graph.length; ++j)
                {
                    if (graph[i][j]==INF)
                        out.print("INF ");
                    else
                        out.print(graph[i][j]+"   ");
                }
                out.println();
            }
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;
 
        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }
 
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
 
        public int nextInt() {
            return Integer.parseInt(next());
        }
 
    }

    // Source: https://www.geeksforgeeks.org/floyd-warshall-algorithm-dp-16/
    // Information credit goes to Muhammad Kenshin Himura
    
    static class AllPairShortestPath
    {
        final static int INF = 2147483646;
     
        int[][] floydWarshall(int graph[][], int amount)
        {
            int dist[][] = new int[amount][amount];
            int i, j, k;
     
            /* Initialize the solution matrix
               same as input graph matrix.
               Or we can say the initial values
               of shortest distances
               are based on shortest paths
               considering no intermediate
               vertex. */
            for (i = 0; i < amount; i++)
                for (j = 0; j < amount; j++)
                    dist[i][j] = graph[i][j];
     
            /* Add all vertices one by one
               to the set of intermediate
               vertices.
              ---> Before start of an iteration,
                   we have shortest
                   distances between all pairs
                   of vertices such that
                   the shortest distances consider
                   only the vertices in
                   set {0, 1, 2, .. k-1} as
                   intermediate vertices.
              ----> After the end of an iteration,
                    vertex no. k is added
                    to the set of intermediate
                    vertices and the set
                    becomes {0, 1, 2, .. k} */
            for (k = 0; k < amount; k++)
            {
                // Pick all vertices as source one by one
                for (i = 0; i < amount; i++)
                {
                    // Pick all vertices as destination for the
                    // above picked source
                    for (j = 0; j < amount; j++)
                    {
                        // If vertex k is on the shortest path from
                        // i to j, then update the value of dist[i][j]
                        if (dist[i][k] + dist[k][j] < dist[i][j])
                            dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
     
            // Print the shortest distance matrix
            // printSolution(dist, amount);
            return dist;
        }
     
        void printSolution(int dist[][], int amount)
        {
            System.out.println("The following matrix shows the shortest "+
                             "distances between every pair of vertices");
            for (int i=0; i<amount; ++i)
            {
                for (int j=0; j<amount; ++j)
                {
                    if (dist[i][j]==INF)
                        System.out.print("INF ");
                    else
                        System.out.print(dist[i][j]+"   ");
                }
                System.out.println();
            }
        }
    }
}