package vv.domain;

import vv.utils.Vec2;

public interface VertexPositioner {
    Vec2 position(String vertex);
    void addVertex(String vertex, Vec2 position);
}
