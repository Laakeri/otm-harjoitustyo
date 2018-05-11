package vv.domain;

import java.util.ArrayList;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GraphTest {
    Graph graph;
    
    @Before
    public void setUp() {
        graph = new Graph();
    }
    
    private void assertEdge(Graph.Edge edge, String v1, String v2) {
        assertEquals(v1, edge.v1);
        assertEquals(v2, edge.v2);
    }
    
    @Test
    public void edgeAddition() {
        graph.addEdge("a", "b");
        graph.addEdge("c", "b");
        graph.addEdge("c", "a");
        graph.addEdge("d", "e");
        ArrayList<Graph.Edge> edges = graph.edges();
        assertEdge(edges.get(0), "a", "b");
        assertEdge(edges.get(1), "b", "c");
        assertEdge(edges.get(2), "a", "c");
        assertEdge(edges.get(3), "d", "e");
    }
    
    @Test
    public void hasCommonEndPoint() {
        graph.addEdge("a", "b");
        graph.addEdge("b", "c");
        graph.addEdge("c", "d");
        ArrayList<Graph.Edge> edges = graph.edges();
        assertEquals(true, edges.get(0).hasCommonEndPoint(edges.get(1)));
        assertEquals(true, edges.get(0).hasCommonEndPoint(edges.get(0)));
        assertEquals(false, edges.get(0).hasCommonEndPoint(edges.get(2)));
    }
    
    @Test
    public void edgeRemoval() {
        graph.addEdge("a", "b");
        graph.addEdge("c", "b");
        graph.addEdge("c", "a");
        assertEquals(true, graph.removeEdge("b", "a"));
        ArrayList<Graph.Edge> edges = graph.edges();
        assertEdge(edges.get(0), "b", "c");
        assertEdge(edges.get(1), "a", "c");
    }
    
    @Test
    public void edgeAddFail() {
        Optional<Graph.Edge> a1 = graph.addEdge("a", "a");
        assertEquals(false, a1.isPresent());
        Optional<Graph.Edge> a2 = graph.addEdge("b", "a");
        assertEquals(true, a2.isPresent());
        assertEquals("(a - b)", a2.get().toString());
        Optional<Graph.Edge> a3 = graph.addEdge("b", "a");
        assertEquals(false, a3.isPresent());
        ArrayList<Graph.Edge> edges = graph.edges();
        assertEquals(1, edges.size());
        assertEdge(edges.get(0), "a", "b");
    }
    
    @Test
    public void vertexRemoval() {
        graph.addEdge("a", "b");
        graph.addEdge("a", "c");
        graph.addEdge("c", "b");
        graph.removeVertex("a");
        assertEquals("[b, c]", graph.vertices().toString());
        assertEquals("[(b - c)]", graph.edges().toString());
    }
}