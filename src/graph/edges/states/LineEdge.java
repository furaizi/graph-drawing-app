package graph.edges.states;

import graph.math.MathHelper;
import graph.math.Point;
import graph.vertices.Vertex;

import java.awt.*;

public class LineEdge extends EdgeState {

    public LineEdge(Vertex vertex1, Vertex vertex2) {
        super(vertex1, vertex2);
        initEdgePoints();

        initArrowSlopes();

        initWeightPoint();
    }


    @Override
    public void paintComponent(Graphics g) {
        drawLine(g);
    }

    private void drawLine(Graphics g) {
        g.drawLine(startPoint.x(), startPoint.y(),
                endPoint.x(), endPoint.y());
    }

    @Override
    protected void initEdgePoints() {
        double lineAngle = MathHelper.calculateLineAngle(vertex1.center, vertex2.center);

        if (vertex1.x > vertex2.x) {
            startPoint = MathHelper.shiftPoint(vertex1, lineAngle + Math.PI);
            endPoint = MathHelper.shiftPoint(vertex2, lineAngle);
        }
        else {
            startPoint = MathHelper.shiftPoint(vertex1, lineAngle);
            endPoint = MathHelper.shiftPoint(vertex2, lineAngle + Math.PI);
        }
    }

    @Override
    protected void initArrowEndSlope() {
        double lineAngle = 2*Math.PI - MathHelper.calculateLineAngle(vertex1.center, vertex2.center);
        arrowEndSlope = Math.tan(lineAngle);
    }

    @Override
    protected void initWeightPoint() {
        int xShift = vertex1.x > vertex2.x ? -50 : 50;
        int yShift = vertex1.y > vertex2.y ? -50 : 50;

        int x = startPoint.x() + xShift;

        int ax = vertex2.x - vertex1.x;
        int ay = vertex2.y - vertex1.y;
        int y = ax == 0 ? startPoint.x() + yShift : (ay*(x-vertex1.x) + ax*vertex1.y)/ax;

        weightPoint = new Point(x, y);
    }
}
