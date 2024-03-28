package graph;

import graph.math.Point;

public class GraphStructure {

    private final int NUMBER_OF_VERTICES = 12;
    private int width = 1600;
    private int height = 900;
    private int currentIndex = 0;
    private Point center = new Point(width/2, height/2);
    private int radius = 300;

    public Point getNextPosition() {
        double angle = currentIndex * 2 * Math.PI / NUMBER_OF_VERTICES;
        currentIndex++;

        return shiftPoint(angle);
    }

    private Point shiftPoint(double angle) {
        int x = (int) Math.round( radius * Math.cos(angle) );
        int y = (int) Math.round( radius * -Math.sin(angle) );

        return new Point(center.x() + x, center.y() + y);
    }
}
