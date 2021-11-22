import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;


public class Lab7v1 {
    private static InputReader in;
    private static PrintWriter out;
    static HashMap<Integer, Graph> dynamicResult = new HashMap<>();
    static HashMap<Integer, Node> nodes = new HashMap<>();
    static Graph graph = new Graph();
    static Node tempOne;
    static Node tempTwo;
    
    // TODO: method to initialize graph
    public static void createGraph(int N) {
    }

    // TODO: method to create an edge with type==T that connects vertex U and vertex V in a graph
    public static void addEdge(int U, int V, int T) {
        if(nodes.get(U) == null){
            nodes.put(U, new Node(U));
            graph.addNode(nodes.get(U));
        }
        if(nodes.get(V) == null){
            nodes.put(V, new Node(V));
            graph.addNode(nodes.get(V));
        }

        nodes.get(U).addDestination(nodes.get(V), T);
        nodes.get(V).addDestination(nodes.get(U), T);
    }

    // TODO: Handle teman X Y K
    public static int canMudik(int X, int Y, int K) {
        int result = 0;
        if(dynamicResult.get(X) == null)
            dynamicResult.put(X, Dijkstra.calculateShortestPathFromSource(graph, nodes.get(X)));
        
        if((dynamicResult.get(X).nodes).get(nodes.get(Y).name) != null && dynamicResult.get(X).nodes.get(nodes.get(Y).name).distance <= K)
            result = 1;
        
        return result;
    } 

    static void sanityCheck(){
        for(Graph i : dynamicResult.values()){
            for(Node j : i.nodes.values()){
                out.print(j.name + "/" + j.distance + " ");
            }
            out.println();
        }
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
        while(Q-- > 0) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int K = in.nextInt();
            out.println(canMudik(X, Y, K));
        }

        sanityCheck();
        out.flush();
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

    // Djikstra, Node, and Graph are from https://github.com/eugenp/tutorials/tree/master/algorithms-miscellaneous-2/src/main/java/com/baeldung/algorithms/ga/dijkstra
    // Some modification has been done

    static class Node {

        private int name;
    
        private LinkedList<Node> shortestPath = new LinkedList<>();
    
        private Integer distance = Integer.MAX_VALUE;
    
        private Map<Node, Integer> adjacentNodes = new HashMap<>();
    
        public Node(int name) {
            this.name = name;
        }

        public int getName() {
            return this.name;
        }
    
        public void addDestination(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
        }
    
        public Map<Node, Integer> getAdjacentNodes() {
            return adjacentNodes;
        }
    
        public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
            this.adjacentNodes = adjacentNodes;
        }
    
        public Integer getDistance() {
            return distance;
        }
    
        public void setDistance(Integer distance) {
            this.distance = distance;
        }
    
        public List<Node> getShortestPath() {
            return shortestPath;
        }
    
        public void setShortestPath(LinkedList<Node> shortestPath) {
            this.shortestPath = shortestPath;
        }
    
    }

    static class Graph {

        private HashMap<Integer, Node> nodes = new HashMap<>();
    
        public void addNode(Node nodeA) {
            nodes.put(nodeA.name, nodeA);
        }
    
        public HashMap<Integer, Node> getNodes() {
            return nodes;
        }
    
        public void setNodes(HashMap<Integer, Node> nodes) {
            this.nodes = nodes;
        }
    }

    static class Dijkstra {

        public static Graph calculateShortestPathFromSource(Graph graph, Node source) {
    
            source.setDistance(0);
    
            Set<Node> settledNodes = new HashSet<>();
            Set<Node> unsettledNodes = new HashSet<>();
            unsettledNodes.add(source);
    
            while (unsettledNodes.size() != 0) {
                Node currentNode = getLowestDistanceNode(unsettledNodes);
                unsettledNodes.remove(currentNode);
                for (Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                    Node adjacentNode = adjacencyPair.getKey();
                    Integer edgeWeigh = adjacencyPair.getValue();
    
                    if (!settledNodes.contains(adjacentNode)) {
                        CalculateMinimumDistance(adjacentNode, edgeWeigh, currentNode);
                        unsettledNodes.add(adjacentNode);
                    }
                }
                settledNodes.add(currentNode);
            }
            return graph;
        }
    
        private static void CalculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
            Integer sourceDistance = sourceNode.getDistance();
            if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
                evaluationNode.setDistance(sourceDistance + edgeWeigh);
                LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
                shortestPath.add(sourceNode);
                evaluationNode.setShortestPath(shortestPath);
            }
        }
    
        private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
            Node lowestDistanceNode = null;
            int lowestDistance = Integer.MAX_VALUE;
            for (Node node : unsettledNodes) {
                int nodeDistance = node.getDistance();
                if (nodeDistance < lowestDistance) {
                    lowestDistance = nodeDistance;
                    lowestDistanceNode = node;
                }
            }
            return lowestDistanceNode;
        }
    }

}