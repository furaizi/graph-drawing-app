package graph.math;

import graph.vertices.Vertex;

import java.util.ArrayList;
import java.util.Arrays;

public class MathHelper {

    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;


    /**
     *
     * @param p1 The first point of the line of symmetry
     * @param p2 The second point of the line of symmetry
     * @param toMirror The point to be mirrored
     */
    public static Point mirrorPoint(Point p1, Point p2, Point toMirror) {
        long ax = p2.x() - p1.x();
        long ay = p2.y() - p1.y();
        long C1 = ax*toMirror.x() + ay*toMirror.y();
        long C2 = ay*p1.x() - ax*p1.y();

        int middleX = (int) Math.round((double) (ax*C1 + ay*C2)/(ax*ax + ay*ay));
        int middleY = (int) Math.round((double) (C1 - ax*middleX)/(ay));

        int mirroredX = 2*middleX - toMirror.x();
        int mirroredY = 2*middleY - toMirror.y();

        return new Point(mirroredX, mirroredY);
    }


    public static double distance(Point p1, Point p2) {
        return distance(p1.x(), p1.y(), p2.x(), p2.y());
    }

    public static Point calculateCircleCenter(Vertex vertex1, Vertex vertex2, Point tgP) {
        return calculateCircleCenter(vertex1.center, vertex2.center, tgP);
    }

    public static ArrayList<Point> circleIntersectsVertex(Point circle, double r, Vertex vertex) {
        return twoCirclesIntersection(circle, r, vertex.center, vertex.RADIUS);
    }

    public static boolean lineIntersectsVertex(Point p1, Point p2, Vertex vertex) {
        return lineIntersectsCircle(p1, p2, vertex.center, vertex.RADIUS);
    }

    public static boolean pointInsideVertex(Point p1, Vertex vertex) {
        return pointInsideCircle(p1, vertex.center, vertex.RADIUS);
    }

    public static double calculateLineAngle(Point p1, Point p2) {
        double deltaX = p2.x() - p1.x();
        double deltaY = p1.y() - p2.y(); // minus sign because of change of y-direction
        double slope = deltaY / deltaX;

        return Math.atan(slope);
    }

    public static Point calculateTangentPoint(Vertex vertex1, Vertex vertex2) {
        return calculateTangentPoint(vertex1.center, vertex2.center);
    }

    public static Point shiftPoint(Vertex vertex, double angle) {
//        int x = (int) Math.round( vertex.RADIUS * Math.cos(angle) );
//        int y = (int) Math.round( vertex.RADIUS * -Math.sin(angle) );
//
//        return new Point(vertex.x + x, vertex.y + y);
//
//
        return shiftPoint(vertex.center, vertex.RADIUS, angle);
    }

    public static Point shiftPoint(Point center, double radius, double angle) {
        int x = (int) Math.round( radius * Math.cos(angle) );
        int y = (int) Math.round( radius * -Math.sin(angle) );

        return new Point(center.x() + x, center.y() + y);
    }

    public static long square(long n) {
        return n*n;
    }

    public static Point rotatePoint(Point center, Point point, double angle) {
        var xShifted = point.x() - center.x();
        var yShifted = point.y() - center.y();

        var xRotated = (int) Math.round(xShifted*Math.cos(angle) + yShifted*Math.sin(angle));
        var yRotated = (int) Math.round(-xShifted*Math.sin(angle) + yShifted*Math.cos(angle));

        return new Point(xRotated, yRotated);
    }


    /**
     * Defines the circle by 2 points on it and the point of the tangent.
     * @param p1 The first point of the circle.
     * @param p2 The second point of the circle.
     * @param tgP The point through which two tangents pass.
     */
    private static Point calculateCircleCenter(Point p1, Point p2, Point tgP) {
        double C1 = (tgP.x() - p1.x())*p1.x() + (tgP.y() - p1.y())*p1.y();
        double C2 = 0.5*((long) p2.x()*p2.x() + p2.y()*p2.y() - p1.x()*p1.x() - p1.y()*p1.y());
        double xNumerator = C1*(p2.y() - p1.y()) - C2*(tgP.y() - p1.y());
        double xDenominator = (tgP.x() - p1.x())*(p2.y() - p1.y()) - (p2.x() - p1.x())*(tgP.y() - p1.y());
        double circleX = xNumerator / xDenominator;

        double yNumerator = (tgP.x() - p1.x())*(p1.x() - circleX) + (tgP.y() - p1.y())*p1.y();
        double yDenominator = tgP.y()-p1.y();
        double circleY = yNumerator / yDenominator;

        return new Point((int) Math.round(circleX), (int) Math.round(circleY));
    }

    /**
     * @param circle1 The center of the first circle.
     * @param r1 The radius of the first circle.
     * @param circle2 The center of the second circle.
     * @param r2 The radius of the second circle.
     * @return The list with points of intersection or an empty list, if there are no points of intersection.
     */
    public static ArrayList<Point> twoCirclesIntersection(Point circle1, double r1, Point circle2, double r2) {
        ArrayList<Point> points = new ArrayList<>();

        double C = 0.5*(r1*r1-r2*r2 + square(circle2.x())-square(circle1.x()) + square(circle2.y())-square(circle1.y()));
        long ax = circle2.x()-circle1.x();
        long ay = circle2.y()-circle1.y(); // ax, ay - coordinates of the vector "a" that connects circle1 and circle2 points

        long a = ax*ax + ay*ay;
        double b = 2*(circle1.y()*ay*ax - circle1.x()*ay*ay - C*ax);

        double c = square(circle1.x())*ay*ay + C*C - 2*circle1.y()*ay*C + square(circle1.y())*ay*ay - r1*r1*ay*ay;

        ArrayList<Double> xList = solveQuadraticEquation(a, b, c);
        for (double x : xList) {
            double y = (C - ax*x) / ay;
            points.add(new Point((int) Math.round(x), (int) Math.round(y)));
        }

        return points;
    }

    /**
     *
     * @param p1 The first point of the line.
     * @param p2 The second point of the line.
     * @param circle The center of the circle.
     * @param r The radius of the circle.
     */
    private static boolean lineIntersectsCircle(Point p1, Point p2, Point circle, double r) {
        if (p1.x() == p2.x()) {
            int yMax = Math.max(p1.y(), p2.y());
            int yMin = Math.min(p1.y(), p2.y());
            boolean betweenY = circle.y() < yMax && circle.y() > yMin;

            double xMax = p1.x() + r;
            double xMin = p1.x() - r;
            boolean betweenX = circle.x() < xMax && circle.x() > xMin;

            return betweenX && betweenY;
        }

        long ABx = p2.x()-p1.x();
        long ABy = p2.y()-p1.y();
        long C = ABx*p1.y() - ABy*p1.x();

        long a = ABx*ABx + ABy*ABy;
        long bOver2 = ABy*C - ABx*ABx*circle.x() - ABx*ABy*circle.y();
        double c = ABx*ABx*circle.x()*circle.x() + C*C + ABx*ABx*circle.y()*circle.y() - 2*ABx*C*circle.y() - ABx*ABx*r*r;
        double discriminantOver4 = bOver2*bOver2 - a*c;

        return discriminantOver4 >= 0;
    }

    private static Point calculateTangentPoint(Point p1, Point p2) {
        double distance = distance(p1, p2);
        double arcAngle = Math.PI/3 - Math.PI/12 * distance/(distance(0,0, WIDTH, HEIGHT)/2); // pi/6
//        double arcAngle = Math.PI/2.5;
        double shift = Math.tan(arcAngle) * distance/2;
        double ctrlX, ctrlY;

        if (p1.x() == p2.x()) {
            ctrlX = p1.x() + shift;
            ctrlY = (p1.y() + p2.y())/2.0;
        }
        else {
            ctrlX = (0.5*(p2.x()*p2.x() - p1.x()*p1.x() + p2.y()*p2.y() - p1.y()*p1.y()) - (p1.y() - shift)*(p2.y() - p1.y()))/(p2.x() - p1.x());
            ctrlY = p1.y() - shift;
        }

        return new Point((int) Math.round(ctrlX), (int) Math.round(ctrlY));
    }

    private static boolean pointInsideCircle(Point p1, Point circle, double r) {
        return distance(p1, circle) < r;
    }

    private static double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt( Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2) );
    }

    private static ArrayList<Double> solveQuadraticEquation(double a, double b, double c) {
        ArrayList<Double> solutions = new ArrayList<>();
        if (a == 0) {
            solutions.add(solveLinearEquation(b, c));
        }
        else {
            double discriminant = b*b - 4*a*c;
            if (discriminant == 0) {
                double x = -b / (2*a);
                solutions.add(x);
            }
            else if (discriminant > 0) {
                double x1 = (-b + Math.sqrt(discriminant)) / (2*a);
                double x2 = (-b - Math.sqrt(discriminant)) / (2*a);
                solutions.add(x1);
                solutions.add(x2);
            }
        }

        return solutions;
    }

    /**
    There is no need in this function because of A coefficient is always non-zero,
    but it should be for the algorithm completeness.
    */
    private static double solveLinearEquation(double a, double b) {
        return -b/a;
    }


}
