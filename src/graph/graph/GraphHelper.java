package graph.graph;

import graph.edges.Edge;
import graph.edges.WeightedEdge;
import graph.vertices.Vertex;
import static graph.math.MatrixHelper.*;

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
        int[][] graphMatrixSquared = multiply(graphMatrix, graphMatrix);
        int[][] graphMatrixCubed = multiply(graphMatrixSquared, graphMatrix);
        int[][] allPathsWithLength2and3Matrix = union(graphMatrixSquared, graphMatrixCubed);

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
        int[][] graphMatrixSquared = pow(graphMatrix, 2);
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
        return union(nonEfficientTransitiveClosure(graphMatrix), identityMatrix(graphMatrix.length));
    }

    public static int[][] calculateMatrixOfStrongConnectivity(int[][] graphMatrix) {
        int[][] reachabilityMatrix = calculateReachabilityMatrix(graphMatrix);

        return union(reachabilityMatrix, transpose(reachabilityMatrix));
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

    public static ArrayList<Integer> getAdjacentVertices(int[][] graphMatrix, int vertex) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < graphMatrix.length; i++) {
            if (graphMatrix[vertex][i] == 1)
                list.add(i);
        }

        return list;
    }

    public static int getTotalEdgeWeight(Graph graph) {
        return graph.getWeightedUndirectedEdges()
                .stream()
                .map(edge -> ((WeightedEdge) edge).getWeight())
                .reduce(0, Integer::sum);
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
