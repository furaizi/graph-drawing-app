package main;

import graph.Graph;
import graph.edges.Edge;
import graph.vertices.Vertex;

import java.util.*;

public class GraphHelper {


    public static Vertex getStartVertex(Graph graph) {
        var matrix = graph.getDirectedGraphMatrix();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == 1)
                    return graph.getVertices().get(i);
            }
        }

        return graph.getVertices().getFirst();
    }

    public static int[][] getMatrixFromEdges(ArrayList<Edge> edges, int size) {
        int[][] matrix = new int[size][size];
        edges.forEach(edge -> matrix[edge.getVertex1().getNumber()][edge.getVertex2().getNumber()] = 1);
        return matrix;
    }

    public static ArrayList<SimpleEdge> graphVerticesToTreeOnes(ArrayList<Edge> edges) {
        HashSet<Integer> was = new HashSet<>();
        ArrayList<SimpleEdge> list = new ArrayList<>();
        int counter = 0;
        for (var edge : edges) {
            var vertex1 = edge.getVertex1().getNumber();
            var vertex2 = edge.getVertex2().getNumber();

            if (!was.contains(vertex1)) {
                was.add(vertex1);
                list.add(new SimpleEdge(vertex1, counter++));
            }

            if (!was.contains(vertex2)) {
                was.add(vertex2);
                list.add(new SimpleEdge(vertex2, counter++));
            }
        }

        return list;
    }






    public static HashMap<Integer, Degree> calculateDirectedGraphDegrees(int[][] graphMatrix) {
        HashMap<Integer, Degree> degrees = new HashMap<>();
        for (int i = 0; i < graphMatrix.length; i++)
            degrees.put(i, new Degree());

        for (int i = 0; i < graphMatrix.length; i++) {
            for (int j = 0; j < graphMatrix.length; j++) {
                if (graphMatrix[i][j] == 1) {
                    degrees.get(i).positive++;
                    degrees.get(j).negative++;
                }
            }
        }

        Degree degree;
        for (int i = 0; i < graphMatrix.length; i++) {
            degree = degrees.get(i);
            degree.degree = degree.positive + degree.negative;
        }

        return degrees;
    }

    public static HashMap<Integer, Degree> calculateUndirectedGraphDegrees(int[][] graphMatrix) {
        HashMap<Integer, Degree> degrees = new HashMap<>();
        for (int i = 0; i < graphMatrix.length; i++)
            degrees.put(i, new Degree());

        for (int i = 0; i < graphMatrix.length; i++) {
            for (int j = 0; j < graphMatrix.length; j++) {
                if (graphMatrix[i][j] == 1)
                    degrees.get(i).degree++;
            }
        }

        return degrees;
    }

    public static boolean isGraphRegular(HashMap<Integer, Degree> degrees) {
        int degreeSample = degrees.get(0).degree;

        for (var degree : degrees.values()) {
            if (degree.degree != degreeSample)
                return false;
        }
        return true;
    }

    public static ArrayList<Integer> getIsolatedVertices(HashMap<Integer, Degree> degrees) {
        ArrayList<Integer> isolatedVertices = new ArrayList<>();
        for (var pair : degrees.entrySet()) {
            if (pair.getValue().degree == 0)
                isolatedVertices.add(pair.getKey());
        }

        return isolatedVertices;
    }

    public static ArrayList<Integer> getEndVertices(HashMap<Integer, Degree> degrees) {
        ArrayList<Integer> endVertices = new ArrayList<>();
        for (var pair : degrees.entrySet()) {
            if (pair.getValue().degree == 1)
                endVertices.add(pair.getKey());
        }

        return endVertices;
    }

    public static ArrayList<SimpleEdge> allPathsWithLength2and3(int[][] graphMatrix) {
        int[][] graphMatrixSquared = multiplyMatrix(graphMatrix, graphMatrix);
        int[][] graphMatrixCubed = multiplyMatrix(graphMatrixSquared, graphMatrix);
        int[][] allPathsWithLength2and3Matrix = unionMatrices(graphMatrixSquared, graphMatrixCubed);

        ArrayList<SimpleEdge> allPathsWithLength2and3List = new ArrayList<>();
        for (int i = 0; i < graphMatrix.length; i++) {
            for (int j = 0; j < graphMatrix.length; j++) {
                if (allPathsWithLength2and3Matrix[i][j] == 1)
                    allPathsWithLength2and3List.add(new SimpleEdge(i, j));
            }
        }

        return allPathsWithLength2and3List;
    }

    public static ArrayList<ArrayList<Integer>> allPathsWithLength2(int[][] graphMatrix) {
        int[][] graphMatrixSquared = powMatrix(graphMatrix, 2);
        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        for (int i = 0; i < graphMatrix.length; i++) {
            for (int j = 0; j < graphMatrix.length; j++) {
                if (i == j)
                    continue;

                if (graphMatrixSquared[i][j] >= 1) {
                    for (int k = 0; k < graphMatrix.length; k++) {
                        if (k == i || k == j)
                            continue;

                        if (graphMatrix[i][k] == 1 && graphMatrix[k][j] == 1) {
                            ArrayList<Integer> path = new ArrayList<>();
                            path.add(i);
                            path.add(k);
                            path.add(j);
                            paths.add(path);
                        }
                    }
                }
            }
        }

        return paths;
    }

    public static ArrayList<ArrayList<Integer>> allPathsWithLength3(int[][] graphMatrix, ArrayList<ArrayList<Integer>> pathsLength2) {
        var iterator = pathsLength2.iterator();
        while (iterator.hasNext()) {
            var path = iterator.next();
            var thirdVertexInPath = path.get(2);
            for (int k = 0; k < graphMatrix.length; k++) {
                if (graphMatrix[thirdVertexInPath][k] == 1
                        && k != path.get(0)
                        && k != path.get(1)
                        && k != path.get(2)) {
                    path.add(k);
                }
            }
            if (path.size() == 3)
                iterator.remove();
        }

        return pathsLength2;
    }

    public static int[][] calculateReachabilityMatrix(int[][] graphMatrix) {
//        return unionMatrices(transitiveClosure(graphMatrix), getIdentityMatrix(graphMatrix.length));
        return unionMatrices(nonEfficientTransitiveClosure(graphMatrix), getIdentityMatrix(graphMatrix.length));
    }

    public static int[][] calculateMatrixOfStrongConnectivity(int[][] graphMatrix) {
        int[][] reachabilityMatrix = calculateReachabilityMatrix(graphMatrix);

        return unionMatrices(reachabilityMatrix, transposeMatrix(reachabilityMatrix));
    }

    public static ArrayList<ArrayList<Integer>> listOfStronglyConnectedComponents(int[][] strongConnectivity) {
        ArrayList<ArrayList<Integer>> components = new ArrayList<>();
        int[] buffer = new int[strongConnectivity.length];
        boolean wasBefore = false;

        for (int i = 0; i < strongConnectivity.length; i++) {
            System.arraycopy(strongConnectivity[i], 0, buffer, 0, strongConnectivity.length);

            for (var component : components) {
                if (Arrays.equals(strongConnectivity[component.getFirst()], buffer)) {
                    component.add(i);
                    wasBefore = true;
                    break;
                }
            }

            if (!wasBefore) {
                ArrayList<Integer> newComponent = new ArrayList<>();
                newComponent.add(i);
                components.add(newComponent);
            }
            wasBefore = false;
        }

        return components;
    }

    public static Graph generateCondensationGraph(int[][] reachability, ArrayList<ArrayList<Integer>> componentsList) {
        int[][] condensation = new int[componentsList.size()][componentsList.size()];


        if (componentsList.size() == 1) {
            Graph graph = new Graph();
            Vertex vertex = new Vertex(0, 500, 500);
            ArrayList<Vertex> vertices = new ArrayList<>();
            vertices.add(vertex);
            graph.setVertices(vertices);

            return graph;
        }

        for (int i = 0; i < componentsList.size()-1; i++) { // cycle for all components

            for (int j = i+1; j < componentsList.size(); j++) {

                for (int k = 0; k < componentsList.get(i).size(); k++) { // cycle for all vertices of the component

                    for (int l = 0; l < componentsList.get(j).size(); l++) {
                        if (reachability[componentsList.get(i).get(k)][componentsList.get(j).get(l)] == 1) {
                            condensation[componentsList.get(i).get(k)][componentsList.get(j).get(l)] = 1;
                        }
                        else if (reachability[componentsList.get(j).get(l)][componentsList.get(i).get(k)] == 1) {
                            condensation[componentsList.get(j).get(l)][componentsList.get(i).get(k)] = 1;
                        }
                    }

                }

            }

        }

        return new Graph(condensation);
    }






    private static int[][] powMatrix(int[][] matrix, int power) {
        int[][] result = matrix;
        for (int i = 2; i <= power; i++)
            result = multiplyMatrix(result, matrix);

        return result;
    }

    private static int[][] multiplyMatrix(int[][] a, int[][] b) {
        int len = a.length;
        int[][] result = new int[len][len];
        int accumulator = 0;

        for (int y = 0; y < len; y++) {
            for (int x = 0; x < len; x++) {
                for (int z = 0; z < len; z++)
                    accumulator += a[y][z] * b[z][x];

                result[y][x] = accumulator;
                accumulator = 0;
            }
        }

        return result;
    }

    private static int[][] unionMatrices(int[][] a, int[][] b) {
        int len = a.length;
        int[][] result = new int[len][len];

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++)
                result[i][j] = a[i][j] + b[i][j];
        }

        return booleanTransform(result);
    }

    private static int[][] booleanTransform(int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                newMatrix[i][j] = matrix[i][j] >= 1 ? 1 : 0;
            }
        }

        return newMatrix;
    }

    private static int[][] transitiveClosure(int[][] relationMatrix) {
        int len = relationMatrix.length;
        int[][] closure = new int[len][len];

        for (int k = 0; k < len; k++) {
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    closure[i][j] = relationMatrix[i][j] | (relationMatrix[i][k] & relationMatrix[k][j]);
                }
            }
        }

        return closure;
    }

    private static int[][] nonEfficientTransitiveClosure(int[][] relationMatrix) {
        int len = relationMatrix.length;
        int[][] transitiveClosure = getIdentityMatrix(len);
        int[][] powMatrix = new int[len][];

        for (int i = 0; i < len; i++)
            powMatrix[i] = Arrays.copyOf(relationMatrix[i], len);
        transitiveClosure = unionMatrices(transitiveClosure, powMatrix);

        for (int i = 2; i < len; i++) {
            powMatrix = multiplyMatrix(powMatrix, relationMatrix);
            transitiveClosure = unionMatrices(transitiveClosure, powMatrix);
        }

        return transitiveClosure;
    }

    private static int[][] getIdentityMatrix(int size) {
        int[][] identityMatrix = new int[size][size];
        for (int i = 0; i < size; i++)
            identityMatrix[i][i] = 1;

        return identityMatrix;
    }

    private static int[][] transposeMatrix(int[][] matrix) {
        int len = matrix.length;
        int[][] transposed = new int[len][len];

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++)
                transposed[i][j] = matrix[j][i];
        }

        return transposed;
    }


    public static class Degree {
        int degree;
        int positive;
        int negative;
    }

    public static class SimpleEdge {
        int vertex1;
        int vertex2;

        SimpleEdge(int vertex1, int vertex2) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
        }
    }
}
