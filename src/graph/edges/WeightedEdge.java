package graph.edges;

import graph.math.Point;
import graph.vertices.Vertex;

import java.awt.*;
import java.util.List;

public class WeightedEdge extends Edge implements Comparable<WeightedEdge> {

    private static final float FONT_SIZE = 12.0f;

    private int weight;
    private Point weightPoint;
    private Color textColor = Color.BLACK;

    public WeightedEdge(Vertex vertex1, Vertex vertex2, int weight, List<Vertex> vertices) {
        super(vertex1, vertex2, vertices);
        this.weight = weight;
        initWeightPoint();
    }

    public WeightedEdge(Edge edge, int weight) {
        super(edge.getVertex1(), edge.getVertex2(), edge.getVertices());
        this.weight = weight;
        initWeightPoint();
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(FONT_SIZE);
        g.setFont(newFont);
        g.drawString(String.valueOf(weight), weightPoint.x(), weightPoint.y());
    }

    @Override
    public int compareTo(WeightedEdge o) {
        return this.weight - o.weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }





    private void initWeightPoint() {
        int xShift = vertex1.x > vertex2.x ? -50 : 50;
        int yShift = vertex1.y > vertex2.y ? -50 : 50;

        int x = lineFromVertex1Point.x() + xShift;

        int ax = vertex2.x - vertex1.x;
        int ay = vertex2.y - vertex1.y;
        int y = ax == 0 ? lineFromVertex1Point.x() + yShift : (ay*(x-vertex1.x) + ax*vertex1.y)/ax;

        weightPoint = new Point(x, y);
    }
}
