package graph.edges.states;

import graph.math.MathHelper;
import graph.math.Point;
import graph.vertices.Vertex;

import javax.swing.*;
import java.awt.*;

public abstract class EdgeState extends JComponent {

    protected static final double ARROW_TANGENT = Math.PI/12; // tgx ~ x, x -> 0
    private static final float FONT_SIZE = 12.0f;

    protected Vertex vertex1;
    protected Vertex vertex2;

    protected Point startPoint;
    protected Point endPoint;
    protected Point weightPoint;

    protected int weight;
    protected double arrowEndSlope;
    protected double arrowPart1Slope;
    protected double arrowPart2Slope;

    public EdgeState(Vertex vertex1, Vertex vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }

    public EdgeState(Vertex vertex1, Vertex vertex2, int weight) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
    }

    @Override
    public abstract void paintComponent(Graphics g);
    public void drawArrow(Graphics2D g) {
        drawArrowPart(g, endPoint, arrowPart1Slope);
        drawArrowPart(g, endPoint, arrowPart2Slope);
    }
    public void drawWeight(Graphics g) {
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(FONT_SIZE);
        g.setFont(newFont);
        g.drawString(String.valueOf(weight), weightPoint.x(), weightPoint.y());
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    protected abstract void initEdgePoints();
    protected void initArrowSlopes() {
        initArrowEndSlope();
        initArrowPartsSlope();
    }
    protected abstract void initArrowEndSlope();
    protected void initArrowPartsSlope() {
        arrowPart1Slope = (arrowEndSlope + ARROW_TANGENT) / (1 - ARROW_TANGENT * arrowEndSlope);
        arrowPart2Slope = (arrowEndSlope - ARROW_TANGENT) / (1 + ARROW_TANGENT * arrowEndSlope);
    }
    protected abstract void initWeightPoint();

    protected void drawArrowPart(Graphics2D g, graph.math.Point p, double arrowPartSlope) {
        double lineAngle = Math.atan(arrowPartSlope);
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
}
