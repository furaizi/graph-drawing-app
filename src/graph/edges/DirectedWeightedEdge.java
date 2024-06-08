package graph.edges;

import graph.vertices.Vertex;

import java.awt.*;
import java.util.HashSet;
import java.util.List;

public class DirectedWeightedEdge extends DirectedEdge implements Comparable<DirectedWeightedEdge> {

    private int weight;
    private Color textColor = Color.BLACK;

    public DirectedWeightedEdge(Vertex vertex1, Vertex vertex2, List<Vertex> vertices, HashSet<Edge> drawnEdges, int weight) {
        super(vertex1, vertex2, vertices, drawnEdges);
        this.weight = weight;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        state.drawWeight(g);
    }

    @Override
    public int compareTo(DirectedWeightedEdge o) {
        return this.weight - o.weight;
    }

    public int getWeight() {
        return weight;
    }
}
