package graph;

import graph.edges.DirectedEdge;
import graph.edges.Edge;
import graph.edges.WeightedEdge;
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
    private static final int SEED = 3320; // 3320
    private int[][] directedGraphMatrix;
    private int[][] undirectedGraphMatrix;
    private int[][] weightsMatrix;
    private double k;
    private ArrayList<Vertex> vertices = new ArrayList<>();
    private HashSet<Edge> undirectedEdges = new HashSet<>();
    private HashSet<Edge> directedEdges = new HashSet<>();
    private HashSet<Edge> weightedUndirectedEdges = new HashSet<>();
    private HashSet<Edge> currentEdges = weightedUndirectedEdges;
    private HashSet<Edge> drawnEdges = new HashSet<>();



    public Graph() {
        undirectedGraphMatrix = new int[NUMBER_OF_VERTICES][NUMBER_OF_VERTICES];
        weightsMatrix = new int[NUMBER_OF_VERTICES][NUMBER_OF_VERTICES];
        initVertices();
    }

    public Graph(double k) {
        this.k = k;
        initMatrices();
        initVertices();
        initUndirectedEdges();
        initDirectedEdges();
        initWeightedUndirectedEdges();
    }

    public Graph(int[][] directedGraphMatrix) {
        this.directedGraphMatrix = directedGraphMatrix;
        undirectedGraphMatrix = calculateUndirectedGraphMatrix();
        initVertices();
        initUndirectedEdges();
        initDirectedEdges();
        initWeightedUndirectedEdges();
    }


    public void addEdge(WeightedEdge edge) {
        weightedUndirectedEdges.add(edge);

        var v1 = edge.getVertex1().getNumber();
        var v2 = edge.getVertex2().getNumber();
        var weight = edge.getWeight();

        undirectedGraphMatrix[v1][v2] = undirectedGraphMatrix[v2][v1] = 1;
        weightsMatrix[v1][v2] = weightsMatrix[v2][v1] = weight;
    }

    public void deleteEdge(WeightedEdge edge) {
        weightedUndirectedEdges.remove(edge);

        var v1 = edge.getVertex1().getNumber();
        var v2 = edge.getVertex2().getNumber();
        var weight = edge.getWeight();

        undirectedGraphMatrix[v1][v2] = undirectedGraphMatrix[v2][v1] = 0;
        weightsMatrix[v1][v2] = weightsMatrix[v2][v1] = 0;
    }





    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public HashSet<Edge> getDirectedEdges() {
        return directedEdges;
    }

    public HashSet<Edge> getWeightedUndirectedEdges() {
        return weightedUndirectedEdges;
    }

    public int[][] getDirectedGraphMatrix() {
        return directedGraphMatrix;
    }

    public int[][] getUndirectedGraphMatrix() {
        return undirectedGraphMatrix;
    }

    public int[][] getWeightsMatrix() {
        return weightsMatrix;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public void switchEdges() {
        if (currentEdges == weightedUndirectedEdges)
            currentEdges = directedEdges;
        else
            currentEdges = weightedUndirectedEdges;
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
        double[][] randomNumbersMatrix = getRandomMatrix();
        directedGraphMatrix = castMatrix(randomNumbersMatrix);
        undirectedGraphMatrix = calculateUndirectedGraphMatrix();
        weightsMatrix = calculateWeightsMatrix();
    }

    private void initVertices() {
        GraphStructure graphStructure = new GraphStructure();

        for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
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

    private void initWeightedUndirectedEdges() {
        for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
            for (int j = 0; j < NUMBER_OF_VERTICES; j++) {
                if (undirectedGraphMatrix[i][j] == 1)
                    weightedUndirectedEdges.add(new WeightedEdge(vertices.get(i), vertices.get(j), weightsMatrix[i][j], vertices));
            }
        }

    }

    private double[][] getRandomMatrix() {
        Random random = new Random(SEED);
        double[][] randomMatrix = new double[NUMBER_OF_VERTICES][NUMBER_OF_VERTICES];

        for (int i = 0; i < NUMBER_OF_VERTICES; i++) {
            for (int j = 0; j < NUMBER_OF_VERTICES; j++)
                randomMatrix[i][j] = k * random.nextDouble(MAX_RANDOM);
        }

        return randomMatrix;
    }

    private int[][] initUpperTriangularMatrix() {
        int[][] tr = new int[NUMBER_OF_VERTICES][NUMBER_OF_VERTICES];
        for (int i = 0; i < tr.length; i++) {
            for (int j = 0; j < tr.length; j++)
                tr[i][j] = i < j ? 1 : 0;
        }

        return tr;
    }

    private int[][] castMatrix(double[][] matrix) {
        int[][] castedMatrix = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++)
                castedMatrix[i][j] = (int) matrix[i][j];
        }

        return castedMatrix;
    }

    private int[][] calculateUndirectedGraphMatrix() {
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

    private int[][] calculateWeightsMatrix() {
        double[][] B = getRandomMatrix();
        double[][] C = new double[B.length][B.length];
        int[][] D = new int[B.length][B.length];
        int[][] H = new int[B.length][B.length];

        for (int i = 0; i < B.length; i++) {
            for (int j = 0; j < B.length; j++) {
                C[i][j] = Math.ceil(B[i][j] * 100 * undirectedGraphMatrix[i][j]);
                D[i][j] = C[i][j] > 0 ? 1 : 0;
            }
        }

        for (int i = 0; i < D.length; i++) {
            for (int j = 0; j < D.length; j++)
                H[i][j] = D[i][j] != D[j][i] ? 1 : 0;
        }

        int[][] tr = initUpperTriangularMatrix();
        int[][] W = new int[B.length][B.length];

        for (int i = 0; i < W.length; i++) {
            for (int j = 0; j < W.length; j++) {
                if (i > j)
                    continue;
                W[i][j] = W[j][i] = (int) Math.round( (D[i][j] + H[i][j] * tr[i][j]) * C[i][j] );
            }
        }

        return W;
    }
}
