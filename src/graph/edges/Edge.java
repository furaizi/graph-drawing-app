package graph.edges;

import graph.edges.states.ArcEdge;
import graph.edges.states.EdgeState;
import graph.edges.states.LineEdge;
import graph.edges.states.LoopEdge;
import graph.math.MathHelper;
import graph.vertices.Vertex;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.List;

public class Edge extends JComponent {

    protected Vertex vertex1;
    protected Vertex vertex2;

    protected List<Vertex> vertices;
    protected Color color = Color.BLACK;
    protected int numberOfVertices;
    protected EdgeState state;


    public Edge(Vertex vertex1, Vertex vertex2, List<Vertex> vertices) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertices = vertices;
        numberOfVertices = vertices.size();
//        initState();
    }

    @Override
    public void paintComponent(Graphics g) {
        initState();
        g.setColor(color);
        state.paintComponent(g);
    }

    public Vertex getVertex1() {
        return vertex1;
    }

    public Vertex getVertex2() {
        return vertex2;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(vertex1, edge.vertex1) && Objects.equals(vertex2, edge.vertex2)
                || Objects.equals(vertex1, edge.vertex2) && Objects.equals(vertex2, edge.vertex1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex1, vertex2) + Objects.hash(vertex2, vertex1);
    }


    protected void initState() {
        if (vertex1.equals(vertex2))
            state = new LoopEdge(vertex1, vertex2);
        else if (lineIntersectsOtherVertices())
            state = new ArcEdge(vertex1, vertex2);
        else
            state = new LineEdge(vertex1, vertex2);
    }

    protected boolean lineIntersectsOtherVertices() {
        for (Vertex vertex : vertices) {
            if (vertex.equals(vertex1) || vertex.equals(vertex2))
                continue;
            if (MathHelper.lineIntersectsVertex(vertex1.center, vertex2.center, vertex))
                return true;
        }

        return false;
    }
}
