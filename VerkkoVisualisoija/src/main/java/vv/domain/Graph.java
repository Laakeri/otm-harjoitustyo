package vv.domain;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class Graph {
    public class Edge {
        public final String v1, v2;
        Edge(String v1, String v2) {
            assert !v1.equals(v2);
            if (v1.compareTo(v2) < 0) {
                this.v1 = v1;
                this.v2 = v2;
            } else {
                this.v1 = v2;
                this.v2 = v1;
            }
        }
        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!Edge.class.isAssignableFrom(obj.getClass())) return false;
            final Edge o = (Edge)obj;
            return o.v1.equals(v1) && o.v2.equals(v2);
        }
        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + Objects.hashCode(this.v1);
            hash = 97 * hash + Objects.hashCode(this.v2);
            return hash;
        }
        public boolean isEndPoint(String v) {
            return v1.equals(v) || v2.equals(v);
        }
        public boolean hasCommonEndPoint(Edge o) {
            return isEndPoint(o.v1) || isEndPoint(o.v2);
        }
    }
    private final ArrayList<String> vertices;
    private final ArrayList<Edge> edges;
    public Graph() {
        edges = new ArrayList<>();
        vertices = new ArrayList<>();
    }
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
    public ArrayList<Edge> edges() {
        return edges;
    }
    public ArrayList<String> vertices() {
        return vertices;
    }
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
