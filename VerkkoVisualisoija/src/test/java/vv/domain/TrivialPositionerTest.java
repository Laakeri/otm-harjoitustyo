package vv.domain;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import vv.utils.Vec2;

public class TrivialPositionerTest {
    VertexPositioner vp;
    
    @Before
    public void setUp() {
        vp = new TrivialPositioner();
    }
    
    @Test
    public void positionerTest() {
        vp.addVertex("a", new Vec2(0.2, 0.3));
        vp.addVertex("b", new Vec2(0.1, 0.9));
        assertEquals("(0.2, 0.3)", vp.position("a").toString());
        assertEquals("(0.1, 0.9)", vp.position("b").toString());
        assertEquals("(0.5, 0.5)", vp.position("c").toString());
    }
}
