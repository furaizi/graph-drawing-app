package graph.edges;

import graph.math.MathHelper;
import graph.math.Point;
import graph.vertices.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

public class Edge extends JComponent {


    protected List<Vertex> vertices;
    protected Vertex vertex1;
    protected Point lineFromVertex1Point;
    protected Point arcFromVertex1Point;


    protected Vertex vertex2;
    protected Point lineFromVertex2Point;
    protected Point arcFromVertex2Point;
    protected Point tangentPoint;
    protected Point arcCenter;
    protected double arcRadius;


    protected Point loopPoint1;
    protected Point loopPoint2;
    protected Point loopCenter;
    protected int loopRadius;


    final int NUMBER_OF_VERTICES = 12;
    final int LOOP_SIZE_COEFFICIENT = 2;



    public Edge(Vertex vertex1, Vertex vertex2, List<Vertex> vertices) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertices = vertices;
        initLinePoints();

        initTangentPoint();
        initArcCenter();
        initArcRadius();
        initArcPoints();

        initLoopPoints();
        initLoopCenter();
        initLoopRadius();
    }

    public void draw(Graphics g) {
        paintComponent(g);
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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (vertex1.equals(vertex2))
            drawLoop(g2d);
        else if (intersectOtherVertices())
            drawArc(g2d);
        else
            drawLine(g2d);
    }

    protected void drawLine(Graphics2D g) {
        g.drawLine(lineFromVertex1Point.x(), lineFromVertex1Point.y(),
                lineFromVertex2Point.x(), lineFromVertex2Point.y());
    }

    protected void drawArc(Graphics2D g) {
        Point2D vertex1Center = new Point2D.Double(vertex1.x, vertex1.y);
        Point2D tangentPoint = new Point2D.Double(this.tangentPoint.x(), this.tangentPoint.y());
        Point2D vertex2Center = new Point2D.Double(vertex2.x, vertex2.y);

        Arc2D arc = new Arc2D.Double();
        arc.setArcByTangent(vertex1Center,
                tangentPoint,
                vertex2Center,
                arcRadius);
        g.draw(arc);
    }

    protected void drawLoop(Graphics2D g)  {
        g.drawOval(loopCenter.x() - loopRadius, loopCenter.y() - loopRadius, 2*loopRadius, 2*loopRadius);
    }

    private int getLoopCircleCoordinate(double loopCoordinate1, double loopCoordinate2, int vertexCoordinate) {
        double mean = (loopCoordinate1 + loopCoordinate2)/2.0;
        double vectorCoordinate = mean - vertexCoordinate;

        int circleCoordinate = (int) Math.round(LOOP_SIZE_COEFFICIENT*vectorCoordinate + vertexCoordinate);
        return circleCoordinate;
    }

    protected boolean intersectOtherVertices() {
        for (Vertex vertex : vertices) {
            if (vertex.equals(vertex1) || vertex.equals(vertex2))
                continue;
            if (MathHelper.lineIntersectsVertex(lineFromVertex1Point, lineFromVertex2Point, vertex))
                return true;
        }

        return false;
    }

    private void initLinePoints() {
        double lineAngle = MathHelper.calculateLineAngle(vertex1.center, vertex2.center);

        if (vertex1.x > vertex2.x) {
            lineFromVertex1Point = MathHelper.shiftPoint(vertex1, lineAngle + Math.PI);
            lineFromVertex2Point = MathHelper.shiftPoint(vertex2, lineAngle);
        }
        else {
            lineFromVertex1Point = MathHelper.shiftPoint(vertex1, lineAngle);
            lineFromVertex2Point = MathHelper.shiftPoint(vertex2, lineAngle + Math.PI);
        }
    }


    private void initTangentPoint() {
        Point ctrlPoint1 = MathHelper.calculateTangentPoint(vertex1, vertex2);
        Point ctrlPoint2 = MathHelper.calculateTangentPoint(vertex2, vertex1);

        if (Math.abs(vertex1.getNumber()-vertex2.getNumber()) >= 5
                && Math.abs(vertex1.getNumber()-vertex2.getNumber()) <= 7) // vertices are opposite or almost opposite
            tangentPoint = MathHelper.distance(vertex1.center, ctrlPoint1) > MathHelper.distance(vertex1.center, ctrlPoint2) ?
                    ctrlPoint2 : ctrlPoint1;
        else
            tangentPoint = MathHelper.distance(vertex1.center, ctrlPoint1) > MathHelper.distance(vertex1.center, ctrlPoint2) ?
                    ctrlPoint1 : ctrlPoint2;
    }

    private void initArcCenter() {
        arcCenter = MathHelper.calculateCircleCenter(vertex1, vertex2, tangentPoint);
    }

    private void initArcRadius() {
        arcRadius = MathHelper.distance(vertex1.center, arcCenter);
    }

    private void initArcPoints() {
        ArrayList<Point> vertex1ArcPoints = MathHelper.circleIntersectsVertex(arcCenter, arcRadius, vertex1);
        ArrayList<Point> vertex2ArcPoints = MathHelper.circleIntersectsVertex(arcCenter, arcRadius, vertex2);

        arcFromVertex1Point = getNearestPoint(vertex1ArcPoints, vertex2);
        arcFromVertex2Point = getNearestPoint(vertex2ArcPoints, vertex1);
    }

    private void initLoopPoints() {
        double angle = (vertex1.getNumber() - 1) * 2 * Math.PI / NUMBER_OF_VERTICES;

        loopPoint1 = MathHelper.shiftPoint(vertex1, angle + Math.PI/4);
        loopPoint2 = MathHelper.shiftPoint(vertex1, angle - Math.PI/4);
    }

    private void initLoopCenter() {
        int circleX = getLoopCircleCoordinate(loopPoint1.x(), loopPoint2.x(), vertex1.x);
        int circleY = getLoopCircleCoordinate(loopPoint1.y(), loopPoint2.y(), vertex1.y);

        loopCenter = new Point(circleX, circleY);
    }

    private void initLoopRadius() {
        loopRadius = (int) Math.round(MathHelper.distance(loopCenter, loopPoint1));
    }

    private Point getNearestPoint(List<Point> points, Vertex anotherVertex) {
        if (points.isEmpty())
            return null;

        if (points.size() == 1)
            return points.get(0);

        Point p1 = points.get(0);
        Point p2 = points.get(1);

        return MathHelper.distance(p1, anotherVertex.center) < MathHelper.distance(p2, anotherVertex.center) ?
                p1 : p2;
    }
}
