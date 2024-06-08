package graph.vertices;

import graph.math.Point;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Vertex extends JComponent {


    public static final int RADIUS = 25;
    public static final int DIAMETER = 2 * RADIUS;
    public int x;
    public int y;
    public Point center;

    public Point startPoint;
    private int number;
    private Color fillColor = Color.WHITE;
    private Color strokeColor = Color.BLUE;

    public Vertex(int number, int x, int y) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.center = new Point(x, y);
        this.startPoint = new Point(x - RADIUS, y - RADIUS);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(fillColor);
        g.fillOval(startPoint.x(), startPoint.y(), DIAMETER, DIAMETER);

        g.setColor(strokeColor);
        g.drawOval(startPoint.x(), startPoint.y(), DIAMETER, DIAMETER);

        int magicNumber = 6;
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(number), x - magicNumber, y + magicNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return x == vertex.x && y == vertex.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getNumber() {
        return number;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
}
