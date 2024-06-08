package graph.edges;

import graph.edges.states.ArcEdge;
import graph.edges.states.EdgeState;
import graph.edges.states.LineEdge;
import graph.edges.states.LoopEdge;
import graph.math.MathHelper;
import graph.math.Point;
import graph.vertices.Vertex;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class DirectedEdge extends Edge {
    private HashSet<Edge> drawnEdges;
    private boolean wasMirrored = false;

    public DirectedEdge(Vertex vertex1, Vertex vertex2, List<Vertex> vertices, HashSet<Edge> drawnEdges) {
        super(vertex1, vertex2, vertices);
        this.drawnEdges = drawnEdges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(vertex1, edge.vertex1) && Objects.equals(vertex2, edge.vertex2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex1, vertex2);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        state.drawArrow((Graphics2D) g);
    }

    @Override
    protected void initState() {
        DirectedEdge complementaryEdge = new DirectedEdge(vertex2, vertex1, vertices, drawnEdges);

        if (vertex1.equals(vertex2))
            state = new LoopEdge(vertex1, vertex2);
        else if (drawnEdges.contains(complementaryEdge)) {
            state = new ArcEdge(vertex1, vertex2);
            if (!wasMirrored) {
                ((ArcEdge) state).mirror();
                wasMirrored = true;
            }
        }
        else if (lineIntersectsOtherVertices())
            state = new ArcEdge(vertex1, vertex2);
        else
            state = new LineEdge(vertex1, vertex2);
        drawnEdges.add(this);
    }
}
