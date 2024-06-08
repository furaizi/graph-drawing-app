package graph.algorithms;

import graph.graph.Graph;
import main.View;

import static graph.graph.GraphHelper.getAdjacentVertices;
import static graph.graph.GraphHelper.getStartVertex;

public class DFSAlgorithm extends Algorithm {

    public DFSAlgorithm(View view) {
        super(view);
    }

    @Override
    protected Void doInBackground() throws Exception {
        view.clear(view.getGraph());
        showDFS(view.getGraph());
        return null;
    }

    private void showDFS(Graph graph) {
        int start = getStartVertex(graph).getNumber();
        boolean[] visited = new boolean[graph.getVertices().size()];
        showDFSUtil(graph, start, visited);
    }


    private void showDFSUtil(Graph graph, int start, boolean[] visited) {
        visited[start] = true;

//        System.out.print(start + " ");
        for (int adj : getAdjacentVertices(graph.getDirectedGraphMatrix(), start)) {
            var adjEdge = getEdge(graph.getDirectedEdges(), start, adj);
            setActive(adjEdge);

            if (!visited[adj]) {
//                System.out.print("--" + start + "-" + adj + " ");
                setAddedToTree(adjEdge);
                showDFSUtil(graph, adj, visited);
            }
            else
                setVisited(adjEdge);
        }
    }
}
