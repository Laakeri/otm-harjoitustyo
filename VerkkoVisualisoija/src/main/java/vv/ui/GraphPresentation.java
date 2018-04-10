package vv.ui;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import vv.domain.Graph;
import vv.domain.VertexPositioner;
import vv.utils.Segment2;
import vv.utils.Vec2;

public class GraphPresentation {
    private final Graph graph;
    private final VertexPositioner vertexPositioner;
    private final double width, height;
    
    public static final double VRADIUS = 30;
    
    private class VertexPresentation {
        private Circle circle;
        private Text text;
        private Vec2 pos;
        VertexPresentation(Vec2 pos, String label) {
            this.pos = pos;
            circle = new Circle(pos.x, pos.y, VRADIUS);
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.WHITE);
            text = new Text(pos.x, pos.y, label);
        }
        public void addToGroup(Group group) {
            group.getChildren().add(circle);
            group.getChildren().add(text);
        }
        public Vec2 pos() {
            return pos;
        }
    }
    
    private Vec2 getEndPoint(Vec2 p1, Vec2 p2) {
        Segment2 sg = new Segment2(p1, p2);
        return sg.interpolate(VRADIUS/p1.dist(p2));
    }
    
    private class EdgePresentation {
        VertexPresentation v1, v2;
        Line line;
        EdgePresentation(VertexPresentation v1, VertexPresentation v2) {
            this.v1 = v1;
            this.v2 = v2;
            Vec2 ep1 = getEndPoint(v1.pos, v2.pos);
            Vec2 ep2 = getEndPoint(v2.pos, v1.pos);
            line = new Line(ep1.x, ep1.y, ep2.x, ep2.y);
        }
        public void addToGroup(Group group) {
            group.getChildren().add(line);
        }
    }
    
    private HashMap<String, VertexPresentation> vertices;
    private ArrayList<EdgePresentation> edges;
    
    private Vec2 nodeToScreen(Vec2 pos) {
        return new Vec2(VRADIUS + (width - 2*VRADIUS)*pos.x, VRADIUS + (height - 2*VRADIUS)*pos.y);
    }
    
    public GraphPresentation(Graph graph, VertexPositioner vertexPositioner, double width, double height) {
        this.graph = graph;
        this.vertexPositioner = vertexPositioner;
        this.width = width;
        this.height = height;
        vertexPositioner.loadGraph(graph);
        vertices = new HashMap<>();
        for (String vertex : graph.vertices()) {
            Vec2 pos = nodeToScreen(vertexPositioner.position(vertex));
            vertices.put(vertex, new VertexPresentation(pos, vertex));
        }
        edges = new ArrayList<>();
        for (Graph.Edge edge : graph.edges()) {
            edges.add(new EdgePresentation(vertices.get(edge.v1), vertices.get(edge.v2)));
        }
    }
    
    void drawToPane(Group group) {
        for (VertexPresentation vp : vertices.values()) {
            vp.addToGroup(group);
        }
        for (EdgePresentation ep : edges) {
            ep.addToGroup(group);
        }
    }
}
