package vv.utils;

/**
 * Class for representing 2d line segments and doing standard geometric computations with them.
 */
public class Segment2 {
    /**
     * Endpoints of the segment
     */
    public final Vec2 p1, p2;
    /**
     * Line segment between points p1 and p2
     * @param p1 Point
     * @param p2 Point
     */
    public Segment2(Vec2 p1, Vec2 p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
    /**
     * Does this segment intersect with the given segment. Probably not reliable
     * in edge cases.
     * @param o Another segment
     * @return Do the segments intersect
     */
    public boolean intersects(Segment2 o) {
        return Vec2.ccw(p1, o.p2, p2) != Vec2.ccw(p1, o.p1, p2) && Vec2.ccw(o.p1, p1, o.p2) != Vec2.ccw(o.p1, p2, o.p2);
    }
    /**
     * Interpolates between the endpoints of the segment (p1 and p2) with p1 as 0 and p2 as 1.
     * @param p Interpolation parameter
     * @return The resulting point
     */
    public Vec2 interpolate(double p) {
        return p1.add(p2.sub(p1).mul(p));
    }
    /**
     * Length of the segment
     * @return Length of this segment (distance between p1 and p2)
     */
    public double length() {
        return p1.dist(p2);
    }
    /**
     * Distance between p and this segment
     * @param p Point
     * @return Distance from point p to this segment
     */
    public double dist(Vec2 p) {
        Vec2 z = p.sub(p1).div(p2.sub(p1));
        if (z.x < 0) return p1.dist(p);
        if (z.x > 1) return p2.dist(p);
        return Math.abs(z.y) * length();
    }
}
