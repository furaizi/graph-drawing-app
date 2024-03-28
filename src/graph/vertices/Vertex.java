package graph.vertices;

import graph.math.Point;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Vertex extends JComponent {


    public static final int RADIUS = 50;
    public static final int DIAMETER = 2 * RADIUS;
    public int x;
    public int y;
    public Point center;

    public Point startPoint;
    private int number;

    public Vertex(int number, int x, int y) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.center = new Point(x, y);
        this.startPoint = new Point(x - RADIUS, y - RADIUS);
    }

    public void draw(Graphics g) {
        paintComponent(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(startPoint.x(), startPoint.y(), DIAMETER, DIAMETER);

        g.setColor(Color.BLUE);
        g.drawOval(startPoint.x(), startPoint.y(), DIAMETER, DIAMETER);

        int magicNumber = 3;
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
}
