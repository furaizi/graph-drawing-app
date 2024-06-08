package graph.algorithms;

import graph.graph.Graph;
import main.View;

import java.util.LinkedList;

import static graph.graph.GraphHelper.getAdjacentVertices;
import static graph.graph.GraphHelper.getStartVertex;

public class BFSAlgorithm extends Algorithm {

    public BFSAlgorithm(View view) {
        super(view);
    }

    @Override
    protected Void doInBackground() throws Exception {
        view.clear(view.getGraph());
        showBFS(view.getGraph());
        return null;
    }

    private void showBFS(Graph graph) {
        int start = getStartVertex(graph).getNumber();
        boolean[] visited = new boolean[graph.getVertices().size()];
        LinkedList<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            start = queue.poll();
//            System.out.print(start + " ");

            for (int adj : getAdjacentVertices(graph.getDirectedGraphMatrix(), start)) {
                var adjEdge = getEdge(graph.getDirectedEdges(), start, adj);
                setActive(adjEdge);

                if (!visited[adj]) {
                    visited[adj] = true;
                    queue.add(adj);

                    setAddedToTree(adjEdge);
                } else
                    setVisited(adjEdge);
            }
        }
    }
}
