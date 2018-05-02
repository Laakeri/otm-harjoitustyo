package vv.domain;

import java.util.HashMap;
import vv.utils.Vec2;

/** 
 * Trivial implementation of VertexPositioner interface. Default position is (0.5, 0.5)
 * @author tuukka
 */
public class TrivialPositioner implements VertexPositioner {
    private final HashMap<String, Vec2> coordinates;
    /**
     * Construct TriavialPositioner
     */
    public TrivialPositioner() {
        coordinates = new HashMap<>();
    }
    /**
     * Returns position of given vertex in this positioner.
     * @param vertex Vertex of a graph
     * @return Position of vertex. Vector whose components are between 0 and 1
     */
    @Override
    public Vec2 position(String vertex) {
        if (coordinates.containsKey(vertex)) {
            return coordinates.get(vertex);
        } else {
            return new Vec2(0.5, 0.5);
        }
    }
    /**
     * Set position of a vertex / add a new vertex and its position.
     * @param vertex Vertex of a graph
     * @param position Position of vertex. Both components are assumed to be between 0 and 1
     */
    @Override
    public void addVertex(String vertex, Vec2 position) {
        coordinates.put(vertex, position);
    }
}
