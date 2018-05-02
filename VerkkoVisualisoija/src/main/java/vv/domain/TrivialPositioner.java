package vv.domain;

import java.util.HashMap;
import vv.utils.Vec2;

public class TrivialPositioner implements VertexPositioner {
    private final HashMap<String, Vec2> coordinates;
    public TrivialPositioner() {
        coordinates = new HashMap<>();
    }
    @Override
    public Vec2 position(String vertex) {
        if (coordinates.containsKey(vertex)) {
            return coordinates.get(vertex);
        } else {
            return new Vec2(0.5, 0.5);
        }
    }
    @Override
    public void addVertex(String vertex, Vec2 position) {
        coordinates.put(vertex, position);
    }
}
