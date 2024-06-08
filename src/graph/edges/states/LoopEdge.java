package graph.edges.states;

import graph.math.MathHelper;
import graph.math.Point;
import graph.vertices.Vertex;

import java.awt.*;

public class LoopEdge extends EdgeState {

    private static final int LOOP_SIZE_COEFFICIENT = 2;

    // костыль
    private final int NUMBER_OF_VERTICES = 12;

    private Point loopCenter;
    private int loopRadius;

    public LoopEdge(Vertex vertex1, Vertex vertex2) {
        super(vertex1, vertex2);
        initEdgePoints();
        initLoopCenter();
        initLoopRadius();

        initArrowSlopes();

        initWeightPoint();
    }

    @Override
    public void paintComponent(Graphics g) {
        drawLoop(g);
    }

    private void drawLoop(Graphics g)  {
        g.drawOval(loopCenter.x() - loopRadius, loopCenter.y() - loopRadius, 2*loopRadius, 2*loopRadius);
    }

    @Override
    protected void initEdgePoints() {
        double angle = (vertex1.getNumber() - 1) * 2 * Math.PI / NUMBER_OF_VERTICES;

        startPoint = MathHelper.shiftPoint(vertex1, angle + Math.PI/4);
        endPoint = MathHelper.shiftPoint(vertex1, angle - Math.PI/4);
    }

    @Override
    protected void initArrowEndSlope() {
        arrowEndSlope = ((double) -(startPoint.x() - loopCenter.x())) / (startPoint.y() - loopCenter.y());
    }

    @Override
    protected void initArrowPartsSlope() {
        arrowPart1Slope = (arrowEndSlope + 1.5*ARROW_TANGENT) / (1 - 1.5*ARROW_TANGENT * arrowEndSlope);
        arrowPart2Slope = (arrowEndSlope - 1.5*ARROW_TANGENT) / (1 + 1.5*ARROW_TANGENT * arrowEndSlope);
    }

    @Override
    protected void initWeightPoint() {
        double angle = MathHelper.calculateLineAngle(vertex1.center, loopCenter);
        weightPoint = MathHelper.shiftPoint(loopCenter, loopRadius, angle);
    }

    private void initLoopCenter() {
        int circleX = getLoopCircleCoordinate(startPoint.x(), endPoint.x(), vertex1.x);
        int circleY = getLoopCircleCoordinate(startPoint.y(), endPoint.y(), vertex1.y);

        loopCenter = new Point(circleX, circleY);
    }

    private void initLoopRadius() {
        loopRadius = (int) Math.round(MathHelper.distance(loopCenter, startPoint));
    }


    private int getLoopCircleCoordinate(double loopCoordinate1, double loopCoordinate2, int vertexCoordinate) {
        double mean = (loopCoordinate1 + loopCoordinate2)/2.0;
        double vectorCoordinate = mean - vertexCoordinate;

        int circleCoordinate = (int) Math.round(LOOP_SIZE_COEFFICIENT*vectorCoordinate + vertexCoordinate);
        return circleCoordinate;
    }
}
