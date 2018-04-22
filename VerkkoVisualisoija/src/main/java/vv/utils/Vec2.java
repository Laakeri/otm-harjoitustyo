package vv.utils;

public class Vec2 {
    public double x, y;
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double cross(Vec2 o) {
        return o.y * x - y * o.x;
    }
    
    public static boolean ccw(Vec2 a, Vec2 b, Vec2 c) {
        return b.sub(a).cross(c.sub(a)) > 0;
    }
    
    public double dist(Vec2 o) {
        return Math.sqrt((x - o.x) * (x - o.x) + (y - o.y) * (y - o.y));
    }
    
    public Vec2 add(Vec2 o) {
        return new Vec2(x + o.x, y + o.y);
    }
    
    public Vec2 sub(Vec2 o) {
        return new Vec2(x - o.x, y - o.y);
    }
    
    public Vec2 mul(double s) {
        return new Vec2(x * s, y * s);
    }
}
