package Lab7;
import java.util.*;
import java.util.Map.Entry;

public class Lab7test {

    public static void main(String[] args) {
        // Node node1 = new Node("1");
        // Node node2 = new Node("2");
        // Node node3 = new Node("3");
        // Node node4 = new Node("4"); 
        // Node node5 = new Node("5");
        // Node node6 = new Node("6");
        // Node node7 = new Node("7");
        // Node node8 = new Node("8");
        // Node node9 = new Node("9");

        // node1.addDestination(node2, 0);
        // node1.addDestination(node4, 0);

        // node2.addDestination(node3, 0);
        // node2.addDestination(node1, 0);

        // node3.addDestination(node2, 0);
        // node3.addDestination(node4, 0);

        // node4.addDestination(node1, 0);
        // node4.addDestination(node3, 0);
        // node4.addDestination(node5, 1);

        // node5.addDestination(node4, 1);
        // node5.addDestination(node6, 1);
        // node5.addDestination(node7, 0);

        // node6.addDestination(node5, 1);

        // node7.addDestination(node5, 0);
        // node7.addDestination(node8, 0);
        // node7.addDestination(node9, 1);

        // node8.addDestination(node7, 0);
        // node8.addDestination(node9, 0);

        // node9.addDestination(node7, 1);
        // node9.addDestination(node8, 0);

        // Graph graph = new Graph();

        // graph.addNode(node1);
        // graph.addNode(node2);
        // graph.addNode(node3);
        // graph.addNode(node4);
        // graph.addNode(node5);
        // graph.addNode(node6);
        // graph.addNode(node7);
        // graph.addNode(node8);
        // graph.addNode(node9);

        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D"); 
        Node nodeE = new Node("E");
        Node nodeF = new Node("F");

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);

        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);

        nodeC.addDestination(nodeE, 10);

        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);

        nodeF.addDestination(nodeE, 5);

        Graph graph = new Graph();

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);

        // Graph resGraph = Dijkstra.calculateShortestPathFromSource(graph, nodeA);
        // for(Node i : resGraph.nodes){
        //     System.out.println(i.name +"/"+ i.distance);
        // }

        graph = Dijkstra.calculateShortestPathFromSource(graph, nodeA);
        System.out.println("====================================");
        for(Node i : graph.nodes.values()){
            System.out.println(i.name +"/"+ i.distance);
        }
        graph = Dijkstra.calculateShortestPathFromSource(graph, nodeB);
        System.out.println("====================================");
        for(Node i : graph.nodes.values()){
            System.out.println(i.name +"/"+ i.distance);
        }
    }

    static class Node {

        private String name;
    
        private LinkedList<Node> shortestPath = new LinkedList<>();
    
        private Integer distance = Integer.MAX_VALUE;
    
        private Map<Node, Integer> adjacentNodes = new HashMap<>();
    
        public Node(String name) {
            this.name = name;
        }
    
        public void addDestination(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
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

        // private Set<Node> nodes = new HashSet<>();
    
        // public void addNode(Node nodeA) {
        //     nodes.add(nodeA);
        // }
    
        // public Set<Node> getNodes() {
        //     return nodes;
        // }
    
        // public void setNodes(Set<Node> nodes) {
        //     this.nodes = nodes;
        // }

        private HashMap<String, Node> nodes = new HashMap<>();
    
        public void addNode(Node nodeA) {
            nodes.put(nodeA.name, nodeA);
        }
    
        public HashMap<String, Node> getNodes() {
            return nodes;
        }
    
        public void setNodes(HashMap<String, Node> nodes) {
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
