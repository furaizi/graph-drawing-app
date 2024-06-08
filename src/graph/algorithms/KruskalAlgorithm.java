package graph.algorithms;

import graph.graph.Graph;
import graph.edges.Edge;
import graph.edges.WeightedEdge;
import main.View;

import java.awt.*;
import java.util.ArrayList;

import static graph.graph.GraphHelper.getAdjacentVertices;

public class KruskalAlgorithm extends Algorithm {

    public KruskalAlgorithm(View view) {
        super(view);
    }

    @Override
    protected Void doInBackground() throws Exception {
        view.clear(view.getGraph());
        kruskalAlgorithm(view.getGraph());
        return null;
    }

//    @Override
//    protected void setActiveAdditional(Edge edge) {
//        var weightedEdge = (WeightedEdge) edge;
//        weightedEdge.setTextColor(Color.RED);
//    }
//
//    @Override
//    protected void setVisitedAdditional(Edge edge) {
//        var weightedEdge = (WeightedEdge) edge;
//        weightedEdge.setTextColor(Color.BLACK);
//    }
//
//    @Override
//    protected void setAddedToTreeAdditional(Edge edge) {
//        var weightedEdge = (WeightedEdge) edge;
//        weightedEdge.setTextColor(Color.BLUE);
//    }


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
                setAddedToTree(edge);
            else
                setVisited(edge);
        }

        return MSTGraph;
    }

    private boolean hasCycle(Graph graph) {
        for (var vertex : graph.getVertices()) {
            var visited = new boolean[graph.getVertices().size()];
            if (DFSVisitVisitedVertex(graph, vertex.getNumber(), -1, visited))
                return true;
        }

        return false;
    }

    private boolean DFSVisitVisitedVertex(Graph graph, int start, int from, boolean[] visited) {
        visited[start] = true;

        for (var to : getAdjacentVertices(graph.getUndirectedGraphMatrix(), start)) {
            if (!visited[to]) {
                if (DFSVisitVisitedVertex(graph, to, start, visited))
                    return true;
            }
            else if (to != from)
                return true;
        }

        return false;
    }
}
