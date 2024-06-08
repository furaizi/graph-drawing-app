package graph.edges;

import graph.math.Point;
import graph.vertices.Vertex;

import java.awt.*;
import java.util.List;

public class WeightedEdge extends Edge implements Comparable<WeightedEdge> {

    private int weight;

    public WeightedEdge(Vertex vertex1, Vertex vertex2, int weight, List<Vertex> vertices) {
        super(vertex1, vertex2, vertices);
        this.weight = weight;
    }

    public WeightedEdge(Edge edge, int weight) {
        super(edge.getVertex1(), edge.getVertex2(), edge.getVertices());
        this.weight = weight;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        state.drawWeight(g);
    }

    @Override
    public int compareTo(WeightedEdge o) {
        return this.weight - o.weight;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    protected void initState() {
        super.initState();
        state.setWeight(weight);
    }
}
