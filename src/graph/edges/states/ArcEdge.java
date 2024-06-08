package graph.edges.states;

import graph.math.MathHelper;
import graph.math.Point;
import graph.vertices.Vertex;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ArcEdge extends EdgeState {

    private Point tangentPoint;
    private Point arcCenter;
    private double arcRadius;

    public ArcEdge(Vertex vertex1, Vertex vertex2) {
        super(vertex1, vertex2);
        initTangentPoint();
        initArcCenter();
        initArcRadius();
        initEdgePoints();

        initArrowSlopes();

        initWeightPoint();
    }

    @Override
    public void paintComponent(Graphics g) {
        drawArc((Graphics2D) g);
    }

    private void drawArc(Graphics2D g) {
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

    public void mirror() {
        startPoint = mirrorPoint(startPoint);
        endPoint = mirrorPoint(endPoint);
        tangentPoint = mirrorPoint(tangentPoint);
        arcCenter = mirrorPoint(arcCenter);
    }

    @Override
    protected void initEdgePoints() {
        ArrayList<Point> vertex1ArcPoints = MathHelper.circleIntersectsVertex(arcCenter, arcRadius, vertex1);
        ArrayList<Point> vertex2ArcPoints = MathHelper.circleIntersectsVertex(arcCenter, arcRadius, vertex2);

        startPoint = getNearestPoint(vertex1ArcPoints, vertex2);
        endPoint = getNearestPoint(vertex2ArcPoints, vertex1);
    }

    @Override
    protected void initArrowEndSlope() {
        arrowEndSlope = ((double) -(endPoint.x() - arcCenter.x())) / (endPoint.y() - arcCenter.y());
    }

    @Override
    protected void initWeightPoint() {
        /*
        get start point
        rotate it by 10 degrees в сторону второй вершины
         */

        final double angle = startPoint.x() < endPoint.x() ? Math.PI/18 : -Math.PI/18;
        weightPoint = MathHelper.rotatePoint(arcCenter, startPoint, angle);

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

    private Point mirrorPoint(Point p) {
        return MathHelper.mirrorPoint(vertex1.center, vertex2.center, p);
    }
}
