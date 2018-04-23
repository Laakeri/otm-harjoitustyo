package vv.ui;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
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
    
    private final HashMap<String, VertexPresentation> vertices;
    private final ArrayList<EdgePresentation> edges;
    
    private class VertexPresentation {
        private final Circle circle;
        private final Text text;
        private final StackPane stack;
        private ArrayList<EdgePresentation> incident;
        private Vec2 pos;
        private Vec2 dragOrgPos;
        private Vec2 dragOrgTranslate;
        VertexPresentation(Vec2 pos, String label) {
            this.pos = pos;
            circle = new Circle(VRADIUS);
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.WHITE);
            text = new Text(label);
            
            stack = new StackPane();
            stack.setTranslateX(pos.x - VRADIUS);
            stack.setTranslateY(pos.y - VRADIUS);
            stack.getChildren().addAll(circle, text);
            
            stack.setOnMousePressed(mouseEvent -> {
                handlePress(mouseEvent);
            });
            stack.setOnMouseDragged(mouseEvent -> {
                handleDrag(mouseEvent);
            });
            
            incident = new ArrayList<>();
        }
        private Vec2 mouseEventPos(MouseEvent e) {
            return new Vec2(e.getSceneX(), e.getSceneY());
        }
        private void handlePress(MouseEvent e) {
            dragOrgPos = mouseEventPos(e);
            dragOrgTranslate = new Vec2(stack.getTranslateX(), stack.getTranslateY());
        }
        private void handleDrag(MouseEvent e) {
            Vec2 offset = mouseEventPos(e).sub(dragOrgPos);
            Vec2 newTranslate = offset.add(dragOrgTranslate);
            
            pos = new Vec2(newTranslate.x + VRADIUS, newTranslate.y + VRADIUS);
            
            stack.setTranslateX(newTranslate.x);
            stack.setTranslateY(newTranslate.y);
            
            for (EdgePresentation ep : incident) {
                ep.updatePos();
            }
        }
        public void addToGroup(Group group) {
            group.getChildren().add(stack);
        }
        public Vec2 pos() {
            return pos;
        }
        public void addIncident(EdgePresentation ep) {
            incident.add(ep);
        }
    }
    
    private Vec2 getEndPoint(Vec2 p1, Vec2 p2) {
        Segment2 sg = new Segment2(p1, p2);
        return sg.interpolate(VRADIUS / p1.dist(p2));
    }
    
    private class EdgePresentation {
        private final VertexPresentation v1, v2;
        private final Line line;
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
        public void updatePos() {
            Vec2 ep1 = getEndPoint(v1.pos, v2.pos);
            Vec2 ep2 = getEndPoint(v2.pos, v1.pos);
            line.setStartX(ep1.x);
            line.setStartY(ep1.y);
            line.setEndX(ep2.x);
            line.setEndY(ep2.y);
        }
    }
    
    private Vec2 nodeToScreen(Vec2 pos) {
        return new Vec2(VRADIUS + (width - 2 * VRADIUS) * pos.x, VRADIUS + (height - 2 * VRADIUS) * pos.y);
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
            EdgePresentation ep = new EdgePresentation(vertices.get(edge.v1), vertices.get(edge.v2));
            edges.add(ep);
            vertices.get(edge.v1).addIncident(ep);
            vertices.get(edge.v2).addIncident(ep);
        }
    }
    
    void drawToGroup(Group group) {
        for (EdgePresentation ep : edges) {
            ep.addToGroup(group);
        }
        for (VertexPresentation vp : vertices.values()) {
            vp.addToGroup(group);
        }
    }
}
