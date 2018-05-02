package vv.domain;

import vv.utils.Vec2;

/**
 * Interface to implement a class that is responsible of positions of vertices.
 */
public interface VertexPositioner {
    /**
     * Position of a given vertex
     * @param vertex Vertex of a graph
     * @return Position of vertex
     */
    Vec2 position(String vertex);
    /**
     * Set position of a vertex / add a new vertex and its position.
     * @param vertex Vertex of a graph
     * @param position Position of vertex. Both components are assumed to be between 0 and 1
     */
    void addVertex(String vertex, Vec2 position);
}
