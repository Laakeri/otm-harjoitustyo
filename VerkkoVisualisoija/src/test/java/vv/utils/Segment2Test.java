package vv.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class Segment2Test {
    static final double PRECISION = 0.001;
    
    @Test
    public void pointSegmentDistanceTest() {
        Vec2 p1 = new Vec2(0, 0);
        Vec2 p2 = new Vec2(15, 0);
        Segment2 s1 = new Segment2(p1, p2);
        assertEquals(0, s1.dist(p1), PRECISION);
        assertEquals(0, s1.dist(p2), PRECISION);
        assertEquals(1, s1.dist(new Vec2(5, -1)), PRECISION);
        assertEquals(1.41421, s1.dist(new Vec2(16, 1)), PRECISION);
        
        Segment2 s2 = new Segment2(new Vec2(1, 3), new Vec2(-1, 1));
        assertEquals(1.41421, s2.dist(new Vec2(-1, 3)), PRECISION);
        assertEquals(1, s2.dist(new Vec2(-1, 0)), PRECISION);
    }
    
    @Test
    public void intersectTest() {
        Segment2 s1 = new Segment2(new Vec2(1, 2), new Vec2(6, 4));
        Segment2 s2 = new Segment2(new Vec2(3, 6), new Vec2(5, 1));
        Segment2 s3 = new Segment2(new Vec2(1, 5), new Vec2(5, 5));
        assertEquals(true, s1.intersects(s2));
        assertEquals(true, s2.intersects(s1));
        assertEquals(false, s1.intersects(s3));
        assertEquals(false, s3.intersects(s1));
        assertEquals(true, s2.intersects(s3));
        assertEquals(true, s3.intersects(s2));
    }
}