package graph.algorithms;

import graph.edges.Edge;
import graph.edges.WeightedEdge;
import main.View;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;

public abstract class Algorithm extends SwingWorker<Void, Void> {

    public static volatile CountDownLatch latch;
    protected View view;

    public Algorithm(View view) {
        this.view = view;
    }

    protected Edge getEdge(HashSet<Edge> edges, int vertex1, int vertex2) {
        return edges.stream()
                .filter((edge) -> edge.getVertex1().getNumber() == vertex1 && edge.getVertex2().getNumber() == vertex2)
                .findFirst()
                .orElseThrow();
    }

    protected void pause() {
        latch = new CountDownLatch(1);
        try {
            latch.await(); // Ждем, пока счетчик не станет нулем
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void setActive(Edge edge) {
        setVerticesColor(edge, Color.RED);
        edge.setColor(Color.RED);
        setActiveAdditional(edge);
        pause();
    }

    protected void setVisited(Edge edge) {
        setVerticesColor(edge, Color.BLUE);
        edge.setColor(Color.BLACK);
        setVisitedAdditional(edge);
        pause();
    }

    protected void setAddedToTree(Edge edge) {
        setVerticesColor(edge, Color.BLUE);
        edge.setColor(Color.BLUE);
        setAddedToTreeAdditional(edge);
        pause();
    }

    protected void setActiveAdditional(Edge edge) {
    }
    protected void setVisitedAdditional(Edge edge) {
    }
    protected void setAddedToTreeAdditional(Edge edge) {
    }

    private void setVerticesColor(Edge edge, Color color) {
        edge.getVertex1().setFillColor(color);
        edge.getVertex2().setFillColor(color);
    }
}
