package main;

import graph.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static main.GraphHelper.*;

public class Main {

    public static final double k1 = 1.0 - 2*0.01 - 0*0.01 - 0.3;
    public static final double k2 = 1.0 - 2*0.005 - 0*0.005 - 0.27;

    private static JFrame frame = new JFrame("Graph");
    private static JButton switchGraphTypeButton = new JButton("Switch the graph type");


    private static JButton showInitialGraphButton = new JButton("Show the initial graph");
    private static JButton showNewGraphButton = new JButton("Show the new graph");

    private static Graph initialGraph = new Graph(k1);
    private static Graph newGraph = new Graph(k2);

    private static JPanel buttonPanel = new JPanel() {{
        add(switchGraphTypeButton);
        add(showInitialGraphButton);
        add(showNewGraphButton);
    }};
    private static JPanel graphPanel = new JPanel(new CardLayout()) {{
        add(initialGraph);
        add(newGraph);
    }};


    static {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 900);
        frame.setVisible(true);
        initialGraph.setVisible(true);
        newGraph.setVisible(false);

        switchGraphTypeButton.addActionListener((action) -> {
            for (var graph : graphPanel.getComponents()) {
                if (graph.isVisible()) {
                    var downcast = (Graph) graph;
                    downcast.switchEdges();
                }
            }
            frame.repaint();
        });

        showInitialGraphButton.addActionListener((action) -> {
            for (var graph : graphPanel.getComponents())
                graph.setVisible(false);
            initialGraph.setVisible(true);
        });

        showNewGraphButton.addActionListener((action) -> {
            for (var graph : graphPanel.getComponents())
                graph.setVisible(false);
            newGraph.setVisible(true);
        });

        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        frame.getContentPane().add(graphPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {

        System.out.println("Undirected graph matrix:");
        printMatrix(initialGraph.getUndirectedGraphMatrix());

        System.out.println();

        System.out.println("Directed graph matrix:");
        printMatrix(initialGraph.getDirectedGraphMatrix());

        var directedGraphDegrees = calculateDirectedGraphDegrees(initialGraph.getDirectedGraphMatrix());
        System.out.println("Degrees of vertices of directed graph:");
        directedGraphDegrees.forEach((num, degree) -> System.out.printf("Vertex %d: %d\n", num, degree.degree));

        System.out.println();

        var undirectedGraphDegrees = calculateUndirectedGraphDegrees(initialGraph.getUndirectedGraphMatrix());
        System.out.println("Degrees of vertices of undirected graph:");
        undirectedGraphDegrees.forEach((num, degree) -> System.out.printf("Vertex %d: %d\n", num, degree.degree));

        System.out.println();

        System.out.println("Semi-degrees of directed graph:");
        directedGraphDegrees.forEach((num, degree) -> System.out.printf("Vertex %d: positive - %d, negative - %d\n", num, degree.positive, degree.negative));

        System.out.println();

        System.out.println("Is graph regular? " + isGraphRegular(undirectedGraphDegrees));
        if (isGraphRegular(undirectedGraphDegrees))
            System.out.printf("Degree of regularity: %d\n", undirectedGraphDegrees.get(0).degree);

        System.out.println();

        var isolatedVertices = getIsolatedVertices(undirectedGraphDegrees);
        System.out.println("List of isolated vertices:");
        isolatedVertices.stream()
                .map(num -> String.format("Vertex %d\n", num))
                .forEach(System.out::println);

        System.out.println();

        var endVertices = getEndVertices(undirectedGraphDegrees);
        System.out.println("List of end vertices:");
        endVertices.stream()
                .map(num -> String.format("Vertex %d\n", num))
                .forEach(System.out::println);


        System.out.println("\n\n\n");


        // new dir graph
        System.out.println("New directed graph.");

        System.out.println("New directed graph matrix");
        printMatrix(newGraph.getDirectedGraphMatrix());

        System.out.println();

        var newDirectedGraphDegrees = calculateDirectedGraphDegrees(newGraph.getDirectedGraphMatrix());
        System.out.println("Semi-degrees of new directed graph:");
        newDirectedGraphDegrees.forEach((num, degree) -> System.out.printf("Vertex %d: positive - %d, negative - %d\n", num, degree.positive, degree.negative));

        System.out.println();

        var allPathsWithLength2 = allPathsWithLength2(newGraph.getDirectedGraphMatrix());
        System.out.println("All paths with length 2:");

        allPathsWithLength2.stream()
                .map( path -> path.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(" - ")))
                .map(str -> "(" + str + ")")
                .forEach(System.out::println);

        System.out.println();

        var allPathsWithLength3 = allPathsWithLength3(newGraph.getDirectedGraphMatrix(), allPathsWithLength2);
        System.out.println("All paths with length 3:");
        allPathsWithLength3.stream()
                .map( path -> path.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(" - ")))
                .map(str -> "(" + str + ")")
                .forEach(System.out::println);


        System.out.println();

        var reachabilityMatrix = calculateReachabilityMatrix(newGraph.getDirectedGraphMatrix());
        System.out.println("Matrix of reachability:");
        printMatrix(reachabilityMatrix);

        System.out.println();

        var matrixOfStrongConnectivity = calculateMatrixOfStrongConnectivity(newGraph.getDirectedGraphMatrix());
        System.out.println("Matrix of strong connectivity:");
        printMatrix(matrixOfStrongConnectivity);

        System.out.println();

        System.out.println("List of strongly connected components:");
        var componentsList = listOfStronglyConnectedComponents(matrixOfStrongConnectivity);
        printStronglyConnectedComponents(componentsList);


        var condensationGraph = generateCondensationGraph(reachabilityMatrix, componentsList);
        graphPanel.add(condensationGraph);
        condensationGraph.setVisible(false);

        var showCondensationGraphButton = new JButton("Show the condensation graph");
        showCondensationGraphButton.addActionListener((action) -> {
            for (var graph : graphPanel.getComponents())
                graph.setVisible(false);
            condensationGraph.setVisible(true);
        });
        buttonPanel.add(showCondensationGraphButton);
    }

    public static void printMatrix(int[][] matrix) {
        Arrays.stream(matrix)
                .map(Arrays::toString)
                .forEach(System.out::println);
    }

    public static void printStronglyConnectedComponents(ArrayList<ArrayList<Integer>> componentsList) {
        for (int i = 0; i < componentsList.size(); i++) {
            System.out.println(i+1 + " component:");
            System.out.print("Vertices: ");
            for (int j = 0; j < componentsList.get(i).size(); j++) {
                System.out.print(componentsList.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}