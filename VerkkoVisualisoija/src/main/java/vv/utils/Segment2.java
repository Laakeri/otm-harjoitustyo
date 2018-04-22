package vv.utils;

public class Segment2 {
    public final Vec2 p1, p2;
    public Segment2(Vec2 p1, Vec2 p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
    public boolean intersects(Segment2 o) {
        return Vec2.ccw(p1, o.p2, p2) != Vec2.ccw(p1, o.p1, p2) && Vec2.ccw(o.p1, p1, o.p2) != Vec2.ccw(o.p1, p2, o.p2);
    }
    public Vec2 interpolate(double p) {
        return p1.add(p2.sub(p1).mul(p));
    }
    public double length() {
        return p1.dist(p2);
    }
}
