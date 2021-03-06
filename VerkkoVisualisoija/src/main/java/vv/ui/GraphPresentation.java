package vv.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    private VertexPositioner vertexPositioner;
    private final double width, height;
    private final Graph graph;
    
    private static final double VRADIUS = 30;
    private static final Vec2 RADIUSOFFSET = new Vec2(VRADIUS, VRADIUS);
    
    private final HashMap<String, VertexPresentation> vertices;
    private final ArrayList<EdgePresentation> edges;
    
    private class VertexPresentation {
        private final Circle circle;
        private final Text text;
        private final StackPane stack;
        private final ArrayList<EdgePresentation> incident;
        private final String label;
        private Vec2 pos;
        private Vec2 orgPos;
        private Vec2 orgTranslate;
        private void initEvents() {
            stack.setOnMousePressed(mouseEvent -> {
                handlePress(mouseEvent);
            });
            stack.setOnMouseDragged(mouseEvent -> {
                handleDrag(mouseEvent);
            });
        }
        private VertexPresentation(Vec2 pos, String label) {
            this.pos = pos;
            this.label = label;
            circle = new Circle(VRADIUS);
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.WHITE);
            text = new Text(label);
            
            stack = new StackPane();
            refreshTranslate();
            stack.getChildren().addAll(circle, text);
            
            initEvents();
            
            incident = new ArrayList<>();
        }
        private Vec2 mouseEventPos(MouseEvent e) {
            return new Vec2(e.getSceneX(), e.getSceneY());
        }
        private void handlePress(MouseEvent e) {
            orgPos = mouseEventPos(e);
            orgTranslate = new Vec2(stack.getTranslateX(), stack.getTranslateY());
        }
        private void refreshTranslate() {
            Vec2 rpos = pos.sub(RADIUSOFFSET);
            stack.setTranslateX(rpos.x);
            stack.setTranslateY(rpos.y);
        }
        private void handleDrag(MouseEvent e) {
            Vec2 offset = mouseEventPos(e).sub(orgPos);
            Vec2 newTranslate = offset.add(orgTranslate);
            
            pos = newTranslate.add(RADIUSOFFSET);
            vertexPositioner.addVertex(label, screenToPos(pos));
            refreshTranslate();
            
            incident.forEach(EdgePresentation::updatePos);
        }
        private void addToPane(Pane pane) {
            pane.getChildren().add(stack);
        }
        private Vec2 pos() {
            return pos;
        }
        private void addIncident(EdgePresentation ep) {
            incident.add(ep);
        }
        private void addOnClickEvent(Consumer<String> fn) {
            stack.setOnMouseClicked(e -> fn.accept(label));
        }
        private void removeOnClickEvent() {
            stack.setOnMouseClicked(e -> { });
        }
        private String label() {
            return label;
        }
        private void removeIncidentEdges() {
            ArrayList<EdgePresentation> toRemove = new ArrayList<>(incident);
            toRemove.forEach(ep -> ep.remove());
        }
        private ArrayList<EdgePresentation> incident() {
            return incident;
        }
        private void resetPosition() {
            this.pos = posToScreen(vertexPositioner.position(label));
            refreshTranslate();
        }
    }
    
    private class EdgePresentation {
        private final VertexPresentation v1, v2;
        private final Line line;
        private EdgePresentation(VertexPresentation v1, VertexPresentation v2) {
            this.v1 = v1;
            this.v2 = v2;
            Vec2 ep1 = getEndPoint(v1.pos, v2.pos);
            Vec2 ep2 = getEndPoint(v2.pos, v1.pos);
            line = new Line(ep1.x, ep1.y, ep2.x, ep2.y);
        }
        private void addToPane(Pane pane) {
            pane.getChildren().add(line);
        }
        private void updatePos() {
            Vec2 ep1 = getEndPoint(v1.pos, v2.pos);
            Vec2 ep2 = getEndPoint(v2.pos, v1.pos);
            line.setStartX(ep1.x);
            line.setStartY(ep1.y);
            line.setEndX(ep2.x);
            line.setEndY(ep2.y);
        }
        private void remove() {
            v1.incident().removeIf(ep -> ep == this);
            v2.incident().removeIf(ep -> ep == this);
            edges.remove(this);
        }
        private boolean isAdjacent(String v) {
            return v1.label().equals(v) || v2.label().equals(v);
        }
    }
    
    private Vec2 getEndPoint(Vec2 p1, Vec2 p2) {
        Segment2 sg = new Segment2(p1, p2);
        return sg.interpolate(VRADIUS / p1.dist(p2));
    }
    
    private Vec2 posToScreen(Vec2 pos) {
        return new Vec2(VRADIUS + (width - 2 * VRADIUS) * pos.x, VRADIUS + (height - 2 * VRADIUS) * pos.y);
    }
    
    private Vec2 screenToPos(Vec2 pos) {
        return new Vec2((pos.x - VRADIUS) / (width - 2 * VRADIUS), (pos.y - VRADIUS) / (height - 2 * VRADIUS));
    }
    
    private void initVertex(String vertex) {
        Vec2 pos = posToScreen(vertexPositioner.position(vertex));
        vertices.put(vertex, new VertexPresentation(pos, vertex));
    }
    
    private void initEdge(Graph.Edge edge) {
        EdgePresentation ep = new EdgePresentation(vertices.get(edge.v1), vertices.get(edge.v2));
        edges.add(ep);
        vertices.get(edge.v1).addIncident(ep);
        vertices.get(edge.v2).addIncident(ep);
    }
    
    public GraphPresentation(Graph graph, VertexPositioner vertexPositioner, double width, double height) {
        this.graph = graph;
        this.vertexPositioner = vertexPositioner;
        this.width = width;
        this.height = height;
        vertices = new HashMap<>();
        graph.vertices().forEach(this::initVertex);
        edges = new ArrayList<>();
        graph.edges().forEach(this::initEdge);
    }
    public boolean hasVertex(String vertex) {
        return vertices.containsKey(vertex);
    }
    public void addVertex(String vertex) {
        vertexPositioner.addVertex(vertex, Vec2.unitRandom());
        initVertex(vertex);
    }
    public boolean addEdge(String vertex1, String vertex2) {
        Optional<Graph.Edge> added = graph.addEdge(vertex1, vertex2);
        if (added.isPresent()) {
            if (!hasVertex(vertex1)) addVertex(vertex1);
            if (!hasVertex(vertex2)) addVertex(vertex2);
            initEdge(added.get());
            return true;
        } else {
            return false;
        }
    }
    public void drawToPane(Pane pane) {
        edges.forEach(ep -> ep.addToPane(pane));
        vertices.values().forEach(vp -> vp.addToPane(pane));
    }
    public void addOnClickEvents(Consumer<String> fn) {
        Consumer<String> resetAndDo = vertex -> {
            resetOnClickEvents();
            fn.accept(vertex);
        };
        vertices.values().forEach(vp -> {
            vp.addOnClickEvent(resetAndDo);
        });
    }
    private void resetOnClickEvents() {
        vertices.values().forEach(vp -> vp.removeOnClickEvent());
    }
    public boolean removeVertex(String vertex) {
        if (graph.removeVertex(vertex)) {
            vertices.get(vertex).removeIncidentEdges();
            vertices.remove(vertex);
            return true;
        } else {
            return false;
        }
    }
    public boolean removeEdge(String vertex1, String vertex2) {
        if (graph.removeEdge(vertex1, vertex2)) {
            for (EdgePresentation ep : edges) {
                if (ep.isAdjacent(vertex1) && ep.isAdjacent(vertex2)) {
                    ep.remove();
                    return true;
                }
            }
        }
        return false;
    }
    public void reposition(VertexPositioner vertexPositioner) {
        this.vertexPositioner = vertexPositioner;
        vertices.values().forEach(vp -> {
            vp.resetPosition();
        });
        edges.forEach(ep -> {
            ep.updatePos();
        });
    }
    public Graph graph() {
        return graph;
    }
    public VertexPositioner vertexPositioner() {
        return vertexPositioner;
    }
}
