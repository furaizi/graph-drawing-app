package main;

import graph.*;
import graph.edges.Edge;
import graph.vertices.Vertex;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static main.GraphHelper.*;

public class View extends JFrame {

    public static final double k = 1.0 - 2*0.001 - 0*0.005 - 0.15;
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

        var BFSTree = view.BFSFast(view.initialGraph);
        var DFSTree = view.DFSFast(view.initialGraph);

        System.out.println("Directed graph matrix:");
        printMatrix(view.initialGraph.getDirectedGraphMatrix());
        System.out.println();

        var BFSTreeMatrix = getMatrixFromEdges(BFSTree, view.initialGraph.getVertices().size());
        System.out.println("BFS traversal tree matrix:");
        printMatrix(BFSTreeMatrix);
        System.out.println();

        var DFSTreeMatrix = getMatrixFromEdges(DFSTree, view.initialGraph.getVertices().size());
        System.out.println("DFS traversal tree matrix:");
        printMatrix(DFSTreeMatrix);
        System.out.println();

        System.out.println("Old vertex -> BFS tree vertex");
        graphVerticesToTreeOnes(BFSTree).forEach(edge -> System.out.printf("%d -> %d\n", edge.vertex1, edge.vertex2));
        System.out.println();

        System.out.println("Old vertex -> DFS tree vertex");
        graphVerticesToTreeOnes(DFSTree).forEach(edge -> System.out.printf("%d -> %d\n", edge.vertex1, edge.vertex2));
        System.out.println();

        var scanner = new Scanner(System.in);
        System.out.println("Choose the number:");
        System.out.println("1. BFS");
        System.out.println("2. DFS");
        System.out.println("3. BFS traversal tree");
        System.out.println("4. DFS traversal tree");
        System.out.println("\"exit\" to stop program");

        var answer = scanner.nextLine();
        while(!answer.equals("exit")) {
            try {
                Integer.parseInt(answer);
            }
            catch (Exception e) {}

            view.clear(view.initialGraph);

            switch (Integer.parseInt(answer)) {
                case 1 -> view.showBFS(view.initialGraph);
                case 2 -> view.showDFS(view.initialGraph);
                case 3 -> {
                    view.hideAllEdges(view.initialGraph);
                    view.showKistyak(view.initialGraph, BFSTree);
                }
                case 4 -> {
                    view.hideAllEdges(view.initialGraph);
                    view.showKistyak(view.initialGraph, DFSTree);
                }
            }

            answer = scanner.nextLine();
        }
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

    private void showBFS(Graph graph) {
        int start = getStartVertex(graph).getNumber();
        boolean[] visited = new boolean[graph.getVertices().size()];
        LinkedList<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.add(start);
        var startVertex = graph.getVertices().get(start);
        setVisited(startVertex);
        pause();

        while (!queue.isEmpty()) {
            start = queue.poll();
            System.out.print(start + " ");

            for (int adj : getAdjacentVertices(graph.getDirectedGraphMatrix(), start)) {
                var adjVertex = graph.getVertices().get(adj);
                var adjEdge = getEdge(graph.getDirectedEdges(), start, adj);
                setActive(adjVertex, adjEdge);

                if (!visited[adj]) {
                    visited[adj] = true;
                    queue.add(adj);

                    setVisited(adjVertex);
                    setEdgeToTree(adjEdge);
                    pause();
                }
                else {
                    setVisited(adjVertex, adjEdge);
                }
            }
        }
    }


    private void showDFS(Graph graph) {
        int start = getStartVertex(graph).getNumber();
        boolean[] visited = new boolean[graph.getVertices().size()];
        showDFSUtil(graph, start, visited);
    }


    private void showDFSUtil(Graph graph, int start, boolean[] visited) {
        visited[start] = true;

        var startVertex = graph.getVertices().get(start);
        setVisited(startVertex);
        pause();

        System.out.print(start + " ");
        for (int adj : getAdjacentVertices(graph.getDirectedGraphMatrix(), start)) {
            var adjVertex = graph.getVertices().get(adj);
            var adjEdge = getEdge(graph.getDirectedEdges(), start, adj);
            setActive(adjVertex, adjEdge);

            if (!visited[adj]) {
                System.out.print("--" + start + "-" + adj + " ");
                setEdgeToTree(adjEdge);
                showDFSUtil(graph, adj, visited);
            }
            else {
                setVisited(adjVertex, adjEdge);
            }
        }
    }

    private ArrayList<Edge> BFSFast(Graph graph) {
        ArrayList<Edge> treeEdges = new ArrayList<>();

        int start = getStartVertex(graph).getNumber();
        boolean[] visited = new boolean[graph.getVertices().size()];
        LinkedList<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            start = queue.poll();
            for (int adj : getAdjacentVertices(graph.getDirectedGraphMatrix(), start)) {
                var adjEdge = getEdge(graph.getDirectedEdges(), start, adj);
                if (!visited[adj]) {
                    treeEdges.add(adjEdge);
                    visited[adj] = true;
                    queue.add(adj);
                }
            }
        }

        return treeEdges;
    }

    private ArrayList<Edge> DFSFast(Graph graph) {
        ArrayList<Edge> treeEdges = new ArrayList<>();

        int start = getStartVertex(graph).getNumber();
        boolean[] visited = new boolean[graph.getVertices().size()];
        DFSFastUtil(graph, start, visited, treeEdges);

        return treeEdges;
    }


    private void DFSFastUtil(Graph graph, int start, boolean[] visited, ArrayList<Edge> treeEdges) {
        visited[start] = true;

        for (int adj : getAdjacentVertices(graph.getDirectedGraphMatrix(), start)) {
            var adjEdge = getEdge(graph.getDirectedEdges(), start, adj);

            if (!visited[adj]) {
                treeEdges.add(adjEdge);
                DFSFastUtil(graph, adj, visited, treeEdges);
            }
        }
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