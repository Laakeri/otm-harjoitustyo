package vv.domain;

import vv.utils.Vec2;

public interface VertexPositioner {
    void loadGraph(Graph graph);
    Vec2 position(String vertex);
}
