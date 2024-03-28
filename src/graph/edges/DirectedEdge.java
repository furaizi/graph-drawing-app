package graph.edges;

import graph.math.MathHelper;
import graph.math.Point;
import graph.vertices.Vertex;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class DirectedEdge extends Edge {

    private final double arrowTangent = Math.PI/12; // tgx ~ x, x -> 0
    private HashSet<Edge> drawnEdges;

    public DirectedEdge(Vertex vertex1, Vertex vertex2, List<Vertex> vertices, HashSet<Edge> drawnEdges) {
        super(vertex1, vertex2, vertices);
        this.drawnEdges = drawnEdges;
    }

    @Override
    public void draw(Graphics g) {
        paintComponent(g);
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
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        DirectedEdge complementaryEdge = new DirectedEdge(vertex2, vertex1, vertices, drawnEdges);

        if (vertex1.equals(vertex2)) {
            drawLoop(g2d);
            drawLoopArrow(g2d);
        }
        else if (drawnEdges.contains(complementaryEdge)) {
            arcFromVertex1Point = mirrorPoint(arcFromVertex1Point);
            arcFromVertex2Point = mirrorPoint(arcFromVertex2Point);
            tangentPoint = mirrorPoint(tangentPoint);
            arcCenter = mirrorPoint(arcCenter);

            drawArc(g2d);
            drawArcArrow(g2d);
        }
        else if (intersectOtherVertices()) {
            drawArc(g2d);
            drawArcArrow(g2d);
        }
        else {
            drawLine(g2d);
            drawLineArrow(g2d);
        }
        drawnEdges.add(this);
    }


    private void drawLoopArrow(Graphics2D g) {
        double tangentCircle = ((double) -(loopPoint1.x() - loopCenter.x())) / (loopPoint1.y() - loopCenter.y());

        double arrowSlope1 = (tangentCircle + 1.5*arrowTangent)/(1 - 1.5*arrowTangent*tangentCircle);
        double arrowSlope2 = (tangentCircle - arrowTangent/1.5)/(1 + arrowTangent/1.5*tangentCircle);

        drawArrowPart(g, loopPoint1, arrowSlope1);
        drawArrowPart(g, loopPoint1, arrowSlope2);
    }

    private void drawArcArrow(Graphics2D g) {
        double tangentCircle = ((double) -(arcFromVertex2Point.x() - arcCenter.x())) / (arcFromVertex2Point.y() - arcCenter.y());

        double arrowSlope1 = (tangentCircle + arrowTangent) / (1 - arrowTangent * tangentCircle);
        double arrowSlope2 = (tangentCircle - arrowTangent) / (1 + arrowTangent * tangentCircle);

        drawArrowPart(g, arcFromVertex2Point, arrowSlope1);
        drawArrowPart(g, arcFromVertex2Point, arrowSlope2);

    }

    private void drawLineArrow(Graphics2D g) {
        double lineAngle = 2*Math.PI - MathHelper.calculateLineAngle(vertex1.center, vertex2.center);
        double lineSlope = Math.tan(lineAngle);

        double arrowSlope1 = (lineSlope + arrowTangent)/(1 - arrowTangent*lineSlope);
        double arrowSlope2 = (lineSlope - arrowTangent)/(1 + arrowTangent*lineSlope);

        drawArrowPart(g, lineFromVertex2Point, arrowSlope1);
        drawArrowPart(g, lineFromVertex2Point, arrowSlope2);
    }


    private void drawArrowPart(Graphics2D g, Point p, double arrowSlope) {
        double lineAngle = Math.atan(arrowSlope);
        Point arrowPoint = calculateArrowPoint(p, lineAngle);
        if (MathHelper.pointInsideVertex(arrowPoint, vertex2))
            arrowPoint = calculateArrowPoint(p, lineAngle + Math.PI);

        g.drawLine(p.x(), p.y(), arrowPoint.x(), arrowPoint.y());
    }

    private Point calculateArrowPoint(Point arcVertex, double lineAngle) {
        final int arrowLength = 20;

        int deltaX = (int) Math.round(arrowLength*Math.cos(lineAngle));
        int deltaY = (int) Math.round(arrowLength*Math.sin(lineAngle));

        return new Point(arcVertex.x() + deltaX, arcVertex.y() + deltaY);
    }
    private Point mirrorPoint(Point p) {
        return MathHelper.mirrorPoint(vertex1.center, vertex2.center, p);
    }
}
