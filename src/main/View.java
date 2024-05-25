package main;

import graph.*;
import graph.edges.Edge;
import graph.edges.WeightedEdge;
import graph.vertices.Vertex;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static main.GraphHelper.*;

public class View extends JFrame {

    public static final double k = 1.0 - 2*0.01 - 0*0.005 - 0.05;
    public boolean nextStep = false;
    private Graph initialGraph = new Graph(k);
    private JButton switchGraphTypeButton, nextStepButton;
    private JPanel buttonPanel, graphPanel;


    public View(String title) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        initialGraph.setVisible(true);

        switchGraphTypeButton = new JButton("Switch the graph type");
        nextStepButton = new JButton("Next step");

        switchGraphTypeButton.addActionListener((action) -> {
            Arrays.stream(graphPanel.getComponents())
                    .filter(Component::isVisible)
                    .map(component -> (Graph) component)
                    .forEach(Graph::switchEdges);
            repaint();
        });
        nextStepButton.addActionListener((action) -> nextStep = true);

        buttonPanel = new JPanel() {{
            add(switchGraphTypeButton);
            add(nextStepButton);
        }};

        graphPanel = new JPanel(new CardLayout()) {{
            add(initialGraph);
        }};

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(graphPanel, BorderLayout.CENTER);
        setVisible(true);
    }




    public static void main(String[] args) {
        var view = new View("Graph");

        System.out.println("Undirected graph matrix:");
        printMatrix(view.initialGraph.getUndirectedGraphMatrix());
        System.out.println();

        System.out.println("Weights matrix:");
        printMatrix(view.initialGraph.getWeightsMatrix());
        System.out.println();

        var MSTGraph = view.kruskalAlgorithm(view.initialGraph);
        System.out.printf("Total minimum spanning tree weight: %d", getTotalEdgeWeight(MSTGraph));
    }

    private Graph kruskalAlgorithm(Graph graph) {
        var weightedEdgesIterator = new ArrayList<>(graph.getWeightedUndirectedEdges()
                .stream()
                .map(edge -> (WeightedEdge) edge)
                .sorted()
                .toList())
                .iterator();
        var MSTGraph = new Graph();

        while (weightedEdgesIterator.hasNext()) {
            var edge = weightedEdgesIterator.next();
            weightedEdgesIterator.remove();
            setActive(edge);

            MSTGraph.addEdge(edge);
            if (hasCycle(MSTGraph))
                MSTGraph.deleteEdge(edge);

            if (MSTGraph.getWeightedUndirectedEdges().contains(edge))
                setAddedToMST(edge);
            else
                setDefault(edge);
        }

        return MSTGraph;
    }


    private void setActive(WeightedEdge edge) {
        edge.getVertex1().setFillColor(Color.RED);
        edge.getVertex2().setFillColor(Color.RED);
        edge.setColor(Color.RED);
        edge.setTextColor(Color.RED);
        repaint();
        pause();
    }

    private void setDefault(WeightedEdge edge) {
        edge.getVertex1().setFillColor(Color.BLUE);
        edge.getVertex2().setFillColor(Color.BLUE);
        edge.setColor(Color.BLACK);
        edge.setTextColor(Color.BLACK);
        repaint();
        pause();
    }

    private void setAddedToMST(WeightedEdge edge) {
        edge.getVertex1().setFillColor(Color.BLUE);
        edge.getVertex2().setFillColor(Color.BLUE);
        edge.setColor(Color.BLUE);
        edge.setTextColor(Color.BLUE);
        repaint();
        pause();
    }

    private boolean hasCycle(Graph graph) {
        for (var vertex : graph.getVertices()) {
            var visited = new boolean[graph.getVertices().size()];
            if (newDFS(graph, vertex.getNumber(), -1, visited))
                return true;
        }

        return false;
    }

    private boolean newDFS(Graph graph, int start, int from, boolean[] visited) {
        visited[start] = true;

        for (var to : getAdjacentVertices(graph.getUndirectedGraphMatrix(), start)) {
            if (!visited[to]) {
                if (newDFS(graph, to, start, visited))
                    return true;
            }
            else if (to != from)
                return true;
        }

        return false;
    }

    private static int getTotalEdgeWeight(Graph graph) {
        return graph.getWeightedUndirectedEdges()
                .stream()
                .map(edge -> ((WeightedEdge) edge).getWeight())
                .reduce(0, Integer::sum);
    }









    private void clear(Graph graph) {
        graph.getVertices().forEach(vertex -> vertex.setFillColor(Color.WHITE));
        graph.getDirectedEdges().forEach(edge -> edge.setColor(Color.BLACK));
        repaint();
    }

    private void hideAllEdges(Graph graph) {
        graph.getDirectedEdges().forEach(edge -> edge.setColor(Color.LIGHT_GRAY));
        repaint();
    }

    private void showAllEdges(Graph graph) {
        graph.getDirectedEdges().forEach(edge -> edge.setVisible(true));
        repaint();
    }




    private void showKistyak(Graph graph, ArrayList<Edge> kistyakEdges) {
        kistyakEdges.forEach(edge -> {
            setEdgeToTree(edge);
            edge.setVisible(true);
        });
        graph.getVertices().forEach(vertex -> {
            setVisited(vertex);
            vertex.setVisible(true);
        });
    }



    private static ArrayList<Integer> getAdjacentVertices(int[][] graphMatrix, int vertex) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < graphMatrix.length; i++) {
            if (graphMatrix[vertex][i] == 1)
                list.add(i);
        }

        return list;
    }

    private static Edge getEdge(HashSet<Edge> edges, int vertex1, int vertex2) {
        return edges.stream()
                .filter((edge) -> edge.getVertex1().getNumber() == vertex1 && edge.getVertex2().getNumber() == vertex2)
                .findFirst()
                .orElseThrow();
    }


    private void setActive(Vertex vertex, Edge edge) {
        setActive(edge);
        setActive(vertex);
        pause();
    }

    private void setVisited(Vertex vertex, Edge edge) {
        setVisited(vertex);
        setVisited(edge);
        pause();
    }

    private void setActive(Vertex vertex) {
        vertex.setFillColor(Color.RED);
        repaint();
    }

    private void setActive(Edge edge) {
        edge.setColor(Color.RED);
        repaint();
    }

    private void setVisited(Vertex vertex) {
        vertex.setFillColor(Color.BLUE);
        repaint();
    }

    private void setVisited(Edge edge) {
        edge.setColor(Color.BLACK);
        repaint();
    }

    private void setEdgeToTree(Edge edge) {
        edge.setColor(Color.BLUE);
        repaint();
    }

    void pause() {
        while (!nextStep) {
            pause(50);
        }
        nextStep = false;
    }

    private static void pause(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (Exception e) {}
    }



    public static void printMatrix(int[][] matrix) {
        Arrays.stream(matrix)
                .map(Arrays::toString)
                .forEach(System.out::println);
    }

    public static void printMatrix(double[][] matrix) {
        Arrays.stream(matrix)
                .map(Arrays::toString)
                .forEach(System.out::println);
    }

    public static void printStronglyConnectedComponents(ArrayList<ArrayList<Integer>> componentsList) {
        for (int i = 0; i < componentsList.size(); i++) {
            System.out.println(i + 1 + " component:");
            System.out.print("Vertices: ");
            for (int j = 0; j < componentsList.get(i).size(); j++) {
                System.out.print(componentsList.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}