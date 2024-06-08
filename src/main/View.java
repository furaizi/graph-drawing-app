package main;

import graph.algorithms.*;
import graph.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class View extends JFrame {

    public static final double K = 1.0 - 2*0.01 - 0*0.005 - 0.05;
    private Graph graph = new Graph(K);
    private JButton switchGraphTypeButton, BFSAlgorithmButton, DFSAlgorithmButton, kruskalAlgorithmButton, nextStepButton, clearButton, resetButton;
    private Algorithm kruskalAlgorithm, BFSAlgorithm, DFSAlgorithm, currentAlgorithm;
    private JPanel buttonPanel, graphPanel;


    public View(String title) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
        graph.setVisible(true);

        initButtons();
        initAlgorithms();
        addActionListenersToButtons();

        buttonPanel = new JPanel() {{
            add(switchGraphTypeButton);
            add(BFSAlgorithmButton);
            add(DFSAlgorithmButton);
            add(kruskalAlgorithmButton);
            add(nextStepButton);
            add(clearButton);
            add(resetButton);
        }};

        graphPanel = new JPanel(new CardLayout()) {{
            add(graph);
        }};

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(graphPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void initButtons() {
        switchGraphTypeButton = new JButton("Switch the graph type");
        nextStepButton = new JButton("Next step");
        kruskalAlgorithmButton = new JButton("Start Kruskal algorithm");
        BFSAlgorithmButton = new JButton("Start BFS algorithm");
        DFSAlgorithmButton = new JButton("Start DFS algorithm");
        clearButton = new JButton("Clear");
        resetButton = new JButton("Reset");
    }

    private void initAlgorithms() {
        kruskalAlgorithm = new KruskalAlgorithm(this);
        BFSAlgorithm = new BFSAlgorithm(this);
        DFSAlgorithm = new DFSAlgorithm(this);
    }

    private void addActionListenersToButtons() {
        switchGraphTypeButton.addActionListener((action) -> {
            Arrays.stream(graphPanel.getComponents())
                    .filter(Component::isVisible)
                    .map(component -> (Graph) component)
                    .forEach(Graph::switchEdges);
            repaint();
        });
        kruskalAlgorithmButton.addActionListener((action) -> {
            currentAlgorithm = kruskalAlgorithm;
            currentAlgorithm.execute();
        });
        BFSAlgorithmButton.addActionListener((action) -> {
            currentAlgorithm = BFSAlgorithm;
            currentAlgorithm.execute();
        });
        DFSAlgorithmButton.addActionListener((action) -> {
            currentAlgorithm = DFSAlgorithm;
            currentAlgorithm.execute();
        });
        nextStepButton.addActionListener((action) -> new NextStepAlgorithm(this).execute());
        clearButton.addActionListener((action) -> {
            clear(graph);
            repaint();
        });
        resetButton.addActionListener((action) -> {
            currentAlgorithm.cancel(true);
            Algorithm.latch = null;
            clear(graph);
            repaint();
        });
    }

    public void clear(Graph graph) {
        graph.getVertices().forEach(vertex -> vertex.setFillColor(Color.WHITE));
        graph.getUndirectedEdges().forEach(edge -> edge.setColor(Color.BLACK));
        graph.getDirectedEdges().forEach(edge -> edge.setColor(Color.BLACK));
        graph.getWeightedUndirectedEdges().forEach(edge -> edge.setColor(Color.BLACK));
        repaint();
    }

    public Graph getGraph() {
        return graph;
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new View("Graph"));
//
//        System.out.println("Undirected graph matrix:");
//        printMatrix(view.initialGraph.getUndirectedGraphMatrix());
//        System.out.println();
//
//        System.out.println("Weights matrix:");
//        printMatrix(view.initialGraph.getWeightsMatrix());
//        System.out.println();

//        var MSTGraph = view.kruskalAlgorithm(view.initialGraph);
//        System.out.printf("Total minimum spanning tree weight: %d", getTotalEdgeWeight(MSTGraph));
    }




    private void hideAllEdges(Graph graph) {
        graph.getDirectedEdges().forEach(edge -> edge.setColor(Color.LIGHT_GRAY));
        repaint();
    }

    private void showAllEdges(Graph graph) {
        graph.getDirectedEdges().forEach(edge -> edge.setVisible(true));
        repaint();
    }


//    private void showKistyak(Graph graph, ArrayList<Edge> kistyakEdges) {
//        kistyakEdges.forEach(edge -> {
//            setEdgeToTree(edge);
//            edge.setVisible(true);
//        });
//        graph.getVertices().forEach(vertex -> {
//            setVisited(vertex);
//            vertex.setVisible(true);
//        });
//    }




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