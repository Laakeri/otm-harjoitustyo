package vv.domain;

import java.util.ArrayList;

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
        public boolean equals(Edge o) {
            return o.v1.equals(v1) && o.v2.equals(v2);
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
    public void addEdge(String v1, String v2) {
        if (v1.equals(v2)) return;
        if (!vertices.contains(v1)) vertices.add(v1);
        if (!vertices.contains(v2)) vertices.add(v2);
        Edge ne = new Edge(v1, v2);
        if (!edges.contains(ne)) edges.add(ne);
    }
    public ArrayList<Edge> edges() {
        return edges;
    }
    public ArrayList<String> vertices() {
        return vertices;
    }
}
