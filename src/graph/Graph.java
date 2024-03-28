package graph;

import graph.edges.DirectedEdge;
import graph.edges.Edge;
import graph.math.Point;
import graph.vertices.Vertex;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Graph extends JComponent {

    private static final int NUMBER_OF_VERTICES = 12;
    private static final double MAX_RANDOM = 2.0;
    private static final double K = 1.0 - 2*0.02 - 0*0.005 - 0.25;
    private static final int SEED = 3320;
    private int[][] directedGraphMatrix;
    private int[][] undirectedGraphMatrix;
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private HashSet<Edge> undirectedEdges = new HashSet<>();
    private HashSet<Edge> directedEdges = new HashSet<>();
    private HashSet<Edge> currentEdges = undirectedEdges;
    private HashSet<Edge> drawnEdges = new HashSet<>();



    public Graph() {
        initMatrices();
        initVertices();
        initUndirectedEdges();
        initDirectedEdges();
    }

    public int[][] getDirectedGraphMatrix() {
        return directedGraphMatrix;
    }

    public int[][] getUndirectedGraphMatrix() {
        return undirectedGraphMatrix;
    }

    public void switchEdges() {
        if (currentEdges == undirectedEdges)
            currentEdges = directedEdges;
        else
            currentEdges = undirectedEdges;
    }


    @Override
    protected void paintComponent(Graphics g) {
        drawnEdges.clear();
        for (var edge : currentEdges)
            edge.draw(g);
        for (var vertex : vertices)
            vertex.draw(g);
    }

    private void initMatrices() {
        double[][] randomNumbersMatrix = initRandomMatrix();
        directedGraphMatrix = castMatrix(randomNumbersMatrix);
        undirectedGraphMatrix = initUndirectedGraphMatrix();
    }

    private void initVertices() {
        GraphStructure graphStructure = new GraphStructure();

        for (int i = 1; i <= NUMBER_OF_VERTICES; i++) {
            Point coordinates = graphStructure.getNextPosition();
            vertices.add(new Vertex(i, coordinates.x(), coordinates.y()));
        }
    }

    private void initUndirectedEdges() {
        for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
            for (int j = 0; j < NUMBER_OF_VERTICES; j++) {
                if (undirectedGraphMatrix[i][j] == 1)
                    undirectedEdges.add(new Edge(vertices.get(i), vertices.get(j), vertices));
            }
        }
    }

    private void initDirectedEdges() {
        for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
            for (int j = 0; j < NUMBER_OF_VERTICES; j++) {
                if (directedGraphMatrix[i][j] == 1)
                    directedEdges.add(new DirectedEdge(vertices.get(i), vertices.get(j), vertices, drawnEdges));
            }
        }
    }

    private double[][] initRandomMatrix() {
        Random random = new Random(SEED);
        double[][] randomMatrix = new double[NUMBER_OF_VERTICES][NUMBER_OF_VERTICES];

        for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
            for (int j = 0; j < NUMBER_OF_VERTICES; j++)
                randomMatrix[i][j] = K * random.nextDouble(MAX_RANDOM);
        }

        return randomMatrix;
    }

    private int[][] castMatrix(double[][] matrix) {
        int[][] castedMatrix = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++)
                castedMatrix[i][j] = (int) matrix[i][j];
        }

        return castedMatrix;
    }

    private int[][] initUndirectedGraphMatrix() {
        int[][] matrix = new int[NUMBER_OF_VERTICES][NUMBER_OF_VERTICES];
        for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
            for (int j = 0; j < NUMBER_OF_VERTICES; j++) {
                if (directedGraphMatrix[i][j] == 1) {
                    matrix[i][j] = 1;
                    matrix[j][i] = 1;
                }
            }
        }

        return matrix;
    }
}
