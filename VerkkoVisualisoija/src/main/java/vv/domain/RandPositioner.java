package vv.domain;

import vv.utils.Vec2;
import vv.utils.Segment2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandPositioner implements VertexPositioner {
    private HashMap<String, Vec2> coordinates;
    Random random;
    
    public RandPositioner() {
        random = new Random(System.currentTimeMillis());
    }
    
    private HashMap<String, Vec2> randomCoordinates(ArrayList<String> vertices) {
        HashMap<String, Vec2> r = new HashMap<>();
        for (String vertex : vertices) {
            r.put(vertex, new Vec2(random.nextDouble(), random.nextDouble()));
        }
        return r;
    }
    
    private double getCost(HashMap<String, Vec2> coordinates, Graph graph) {
        double value = 0;
        for (int i = 0; i < graph.vertices().size(); i++) {
            for (int ii = i + 1; ii < graph.vertices().size(); ii++) {
                value += 1. / coordinates.get(graph.vertices().get(i)).dist(coordinates.get(graph.vertices().get(ii)));
            }
        }
        for (int i = 0; i < graph.edges().size(); i++) {
            for (int ii = i + 1; ii < graph.edges().size(); ii++) {
                Graph.Edge e1 = graph.edges().get(i);
                Graph.Edge e2 = graph.edges().get(ii);
                if (e1.hasCommonEndPoint(e2)) continue;
                Segment2 s1 = new Segment2(coordinates.get(e1.v1), coordinates.get(e1.v2));
                Segment2 s2 = new Segment2(coordinates.get(e2.v1), coordinates.get(e2.v2));
                if (s1.intersects(s2)) value += 5;
            }
        }
        return value;
    }
    
    private void optimizeCoordinates(Graph graph) {
        HashMap<String, Vec2> best;
        // some magic constants here
        double bestVal = 1e9;
        HashMap<String, Vec2> bestCoordinates;
        for (int it = 0; it < 1000; it++) {
            HashMap<String, Vec2> testCoordinates = randomCoordinates(graph.vertices());
            double value = getCost(testCoordinates, graph);
            if (value < bestVal) {
                bestVal = value;
                coordinates = testCoordinates;
            }
        }
        System.out.println("Best value " + bestVal);
    }
    
    @Override
    public Vec2 position(String vertex) {
        return coordinates.get(vertex);
    }
    
    @Override
    public void loadGraph(Graph graph) {
        optimizeCoordinates(graph);
    }
}
