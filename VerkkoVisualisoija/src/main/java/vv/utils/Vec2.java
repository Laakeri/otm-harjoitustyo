package vv.utils;
import java.util.Random;

/**
 * Class for representing 2d vectors and doing standard geometric computations with them.
 */
public class Vec2 {
    /**
     * Components
     */
    public final double x, y;
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    /**
     * Create a vector
     * @param x x component
     * @param y y component
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Cross product with other vector. (The z component of 3d cross product of the vectors)
     * @param o Other vector
     * @return Cross product
     */
    public double cross(Vec2 o) {
        return o.y * x - y * o.x;
    }
    /**
     * Is the turn a -> b -> c counterclockwise
     * @param a Vector a
     * @param b Vector b
     * @param c Vector c
     * @return Is a -> b -> c counterclockwise
     */
    public static boolean ccw(Vec2 a, Vec2 b, Vec2 c) {
        return b.sub(a).cross(c.sub(a)) > 0;
    }
    /**
     * Distance from this to o
     * @param o Other vector
     * @return Distance to o
     */
    public double dist(Vec2 o) {
        return Math.sqrt((x - o.x) * (x - o.x) + (y - o.y) * (y - o.y));
    }
    /**
     * Addition
     * @param o Other vector
     * @return this + o
     */
    public Vec2 add(Vec2 o) {
        return new Vec2(x + o.x, y + o.y);
    }
    /**
     * Subtraction
     * @param o Other vector
     * @return this - o
     */
    public Vec2 sub(Vec2 o) {
        return new Vec2(x - o.x, y - o.y);
    }
    /**
     * Multiplication by constant
     * @param s Constant
     * @return This multiplied by constant s
     */
    public Vec2 mul(double s) {
        return new Vec2(x * s, y * s);
    }
    /**
     * Multiplication by other vector. This is complex multiplication.
     * @param o Other vector
     * @return Complex multiplication this * o
     */
    public Vec2 mul(Vec2 o) {
        return new Vec2(x * o.x - y * o.y, y * o.x + x * o.y);
    }
    /** 
     * Complex conjugate
     * @return Complex conjugate of this (x, -y)
     */
    public Vec2 conj() {
        return new Vec2(x, -y);
    }
    /**
     * Division by other vector. This is complex division.
     * @param o Other vector
     * @return Complex division this / o
     */
    public Vec2 div(Vec2 o) {
        return mul(o.conj()).mul(1 / (o.x * o.x + o.y * o.y));
    }
    /**
     * String of this. In format (x, y)
     * @return String of this
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    /**
     * Uniformly random vector in [0, 1]^2
     * @return Uniformly random vector in [0, 1]^2
     */
    public static Vec2 unitRandom() {
        return new Vec2(RANDOM.nextDouble(), RANDOM.nextDouble());
    }
}
