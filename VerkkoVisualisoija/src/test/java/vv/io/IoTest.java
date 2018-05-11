package vv.io;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;
import vv.domain.Graph;
import vv.domain.TrivialPositioner;
import vv.domain.VertexPositioner;
import vv.utils.Vec2;

public class IoTest {
    private Io.ReadResult readInput(String input) {
        return Io.readGraph(new Scanner(input));
    }
    
    @Test
    public void readGraphSuccessTest1() {
        Io.ReadResult res = readInput("e: 1 2\n e: 2 3\n");
        assertEquals(true, res.graph.isPresent());
        assertEquals(false, res.vertexPositioner.isPresent());
        Graph g = res.graph.get();
        assertEquals("[1, 2, 3]", g.vertices().toString());
        assertEquals("[(1 - 2), (2 - 3)]", g.edges().toString());
        assertEquals("Ladattu onnistuneesti", res.errorMessage);
    }
    
    @Test
    public void readGraphSuccessTest2() {
        Io.ReadResult res = readInput("p 3 2\ne 1 2\ne 2 3\n");
        assertEquals(true, res.graph.isPresent());
        assertEquals(false, res.vertexPositioner.isPresent());
        Graph g = res.graph.get();
        assertEquals("[1, 2, 3]", g.vertices().toString());
        assertEquals("[(1 - 2), (2 - 3)]", g.edges().toString());
        assertEquals("Ladattu onnistuneesti", res.errorMessage);
    }
    
    @Test
    public void readGraphSuccessTest3() {
        Io.ReadResult res = readInput("p 3 2\nc 1 0.2 0.2\nc 2 0.4 0.2\nc 3 0.1 0.6\ne 1 2\ne 2 3\n");
        assertEquals(true, res.graph.isPresent());
        assertEquals(true, res.vertexPositioner.isPresent());
        Graph g = res.graph.get();
        VertexPositioner vp = res.vertexPositioner.get();
        assertEquals("[1, 2, 3]", g.vertices().toString());
        assertEquals("[(1 - 2), (2 - 3)]", g.edges().toString());
        assertEquals("Ladattu onnistuneesti", res.errorMessage);
        assertEquals("(0.2, 0.2)", vp.position("1").toString());
        assertEquals("(0.4, 0.2)", vp.position("2").toString());
        assertEquals("(0.1, 0.6)", vp.position("3").toString());
    }
    
    @Test
    public void readGraphFailTest1() {
        Io.ReadResult res = readInput("asdf lol");
        assertEquals(false, res.graph.isPresent());
        assertEquals(false, res.vertexPositioner.isPresent());
        assertEquals("Väärä tiedostomuoto", res.errorMessage);
    }
    
    @Test
    public void readGraphFailTest2() {
        Io.ReadResult res = readInput("p 10 20\na\nb\n");
        assertEquals(false, res.graph.isPresent());
        assertEquals(false, res.vertexPositioner.isPresent());
        assertEquals("Väärä tiedostomuoto", res.errorMessage);
    }
    
    @Test
    public void saveGraphTest() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        Graph g = new Graph();
        g.addEdge("a", "b");
        g.addEdge("c", "b");
        g.addEdge("b", "a");
        VertexPositioner vp = new TrivialPositioner();
        vp.addVertex("a", new Vec2(0.2, 0.1));
        vp.addVertex("b", new Vec2(0.3, 0.6));
        vp.addVertex("c", new Vec2(0.5, 0.8));
        Io.writeGraph(pw, g, vp);
        assertEquals("p 3 2\nc a 0.2 0.1\nc b 0.3 0.6\nc c 0.5 0.8\ne a b\ne b c\n", sw.toString());
    }
}