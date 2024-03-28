package main;

import graph.*;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Graph graph = new Graph();
        JButton switchGraphButton = new JButton("Switch the graph");
        switchGraphButton.addActionListener((action) -> {
                graph.switchEdges();
                frame.repaint();
            }
        );

        frame.getContentPane().add(switchGraphButton, BorderLayout.NORTH);
        frame.getContentPane().add(graph, BorderLayout.CENTER);

        frame.setSize(1600, 900);
        frame.setVisible(true);

        System.out.println("Undirected graph matrix:");
        printMatrix(graph.getUndirectedGraphMatrix());

        System.out.println("\n\n\n");

        System.out.println("Directed graph matrix:");
        printMatrix(graph.getDirectedGraphMatrix());
    }

    public static void printMatrix(int[][] matrix) {
        Arrays.stream(matrix)
                .map(Arrays::toString)
                .forEach(System.out::println);
    }
}