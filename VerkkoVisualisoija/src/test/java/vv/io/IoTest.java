package vv.io;

import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;
import vv.domain.Graph;
import vv.domain.VertexPositioner;

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
}