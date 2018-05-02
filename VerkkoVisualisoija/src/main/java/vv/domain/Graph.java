package vv.domain;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Class for representing graphs (https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)).
 * Represents only the edges between the vertices and not any other information.
 * Does not support isolated vertices, as we think that they are useless.
 * Vertices are strings.
 */
public class Graph {
    /**
     * Class for edges in a graph. Edge is a pair of vertices, which are strings.
     */
    public class Edge {
        /**
         * Endpoints of the edge. v1 < v2 always
         */
        public final String v1, v2;
        private Edge(String v1, String v2) {
            assert !v1.equals(v2);
            if (v1.compareTo(v2) < 0) {
                this.v1 = v1;
                this.v2 = v2;
            } else {
                this.v1 = v2;
                this.v2 = v1;
            }
        }
        /**
         * Equality. Checks that both endpoints are equal.
         * @param obj Other object
         * @return Is this equal to the other object
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!Edge.class.isAssignableFrom(obj.getClass())) return false;
            final Edge o = (Edge) obj;
            return o.v1.equals(v1) && o.v2.equals(v2);
        }
        /**
         * Hashcode
         * @return Some hash of this
         */
        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + Objects.hashCode(this.v1);
            hash = 97 * hash + Objects.hashCode(this.v2);
            return hash;
        }
        /**
         * Is a given vertex an endpoint of this.
         * @param v A vertex
         * @return Is v an endpoint of this
         */
        public boolean isEndPoint(String v) {
            return v1.equals(v) || v2.equals(v);
        }
        /**
         * Does this edge have a common endpoint with given edge.
         * @param o Other edge
         * @return Does this edge have common endpoint with o
         */
        public boolean hasCommonEndPoint(Edge o) {
            return isEndPoint(o.v1) || isEndPoint(o.v2);
        }
    }
    private final ArrayList<String> vertices;
    private final ArrayList<Edge> edges;
    /**
     * Create an empty graph.
     */
    public Graph() {
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
    }
    /**
     * Add edge to the graph.
     * @param v1 Endpoint of the edge
     * @param v2 Endpoint of the edge
     * @return Returns Optional.empty() if v1 == v2 or the edge already existed. Otherwise returns the added edge
     */
    public Optional<Edge> addEdge(String v1, String v2) {
        if (v1.equals(v2)) return Optional.empty();
        if (!vertices.contains(v1)) vertices.add(v1);
        if (!vertices.contains(v2)) vertices.add(v2);
        Edge ne = new Edge(v1, v2);
        if (!edges.contains(ne)) {
            edges.add(ne);
            return Optional.of(ne);
        } else {
            return Optional.empty();
        }
    }
    /**
     * Remove edge from the graph.
     * @param v1 Endpoint of the edge
     * @param v2 Endpoint of the edge
     * @return True if the edge was in the graph and false if it wasn't
     */
    public boolean removeEdge(String v1, String v2) {
        if (v1.equals(v2)) return false;
        Edge ne = new Edge(v1, v2);
        if (edges.contains(ne)) {
            edges.remove(ne);
            removeIfNoEdges(v1);
            removeIfNoEdges(v2);
            return true;
        } else {
            return false;
        }
    }
    /**
     * Edges of the graph
     * @return Edges of the graph
     */
    public ArrayList<Edge> edges() {
        return edges;
    }
    /**
     * Vertices of the graph
     * @return Vertices of the graph
     */
    public ArrayList<String> vertices() {
        return vertices;
    }
    /**
     * Remove a vertex from the graph
     * @param vertex The vertex to be removed
     * @return True if the vertex was in the graph and false otherwise
     */
    public boolean removeVertex(String vertex) {
        if (vertices.contains(vertex)) {
            vertices.remove(vertex);
            edges.removeIf(e -> e.isEndPoint(vertex));
            return true;
        } else {
            return false;
        }
    }
    private void removeIfNoEdges(String vertex) {
        for (Edge e : edges) {
            if (e.isEndPoint(vertex)) return;
        }
        vertices.remove(vertex);
    }
}
