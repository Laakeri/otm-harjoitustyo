package vv.domain;

import vv.utils.Vec2;
import vv.utils.Segment2;
import java.util.ArrayList;
import java.util.HashMap;

public class RandPositioner implements VertexPositioner {
    private HashMap<String, Vec2> coordinates;
    
    public RandPositioner(Graph graph) {
        optimizeCoordinates(graph);
    }
    
    private HashMap<String, Vec2> randomCoordinates(ArrayList<String> vertices) {
        HashMap<String, Vec2> r = new HashMap<>();
        for (String vertex : vertices) {
            r.put(vertex, Vec2.unitRandom());
        }
        return r;
    }
    
    private Segment2 edgeSegment(Graph.Edge e) {
        return new Segment2(coordinates.get(e.v1), coordinates.get(e.v2));
    }
    
    private double distCost(Graph graph) {
        double value = 0;
        for (Graph.Edge e : graph.edges()) {
            for (String v : graph.vertices()) {
                if (e.isEndPoint(v)) continue;
                value += 1. / edgeSegment(e).dist(coordinates.get(v));
            }
        }
        return value;
    }
    private int crossings(Graph graph) {
        int cross = 0;
        for (int i = 0; i < graph.edges().size(); i++) {
            for (int ii = i + 1; ii < graph.edges().size(); ii++) {
                Graph.Edge e1 = graph.edges().get(i);
                Graph.Edge e2 = graph.edges().get(ii);
                if (e1.hasCommonEndPoint(e2)) continue;
                if (edgeSegment(e1).intersects(edgeSegment(e2))) cross++;
            }
        }
        return cross;
    }
    private double getCost(Graph graph, double crossWeight) {
        return distCost(graph) + (double) crossings(graph) * crossWeight;
    }
    private void optimizeCoordinates(Graph graph) {
        long startTime = System.currentTimeMillis();
        // some magic constants here
        double bestVal = 1e9;
        HashMap<String, Vec2> bestCoordinates = new HashMap<>();
        for (int it = 0; it < 1000; it++) {
            coordinates = randomCoordinates(graph.vertices());
            double value = getCost(graph, 100);
            if (value < bestVal) {
                bestVal = value;
                bestCoordinates = coordinates;
            }
        }
        coordinates = bestCoordinates;
        System.out.println("Best value " + bestVal);
        System.out.println("Used " + (System.currentTimeMillis() - startTime) + " ms");
    }
    
    @Override
    public Vec2 position(String vertex) {
        return coordinates.get(vertex);
    }
    
    @Override
    public void addVertex(String vertex, Vec2 position) {
        coordinates.put(vertex, position);
    }
}
