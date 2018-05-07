package vv.io;

import java.io.PrintWriter;
import java.util.Scanner;
import vv.domain.Graph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import vv.domain.TrivialPositioner;
import vv.domain.VertexPositioner;
import vv.utils.Vec2;

/**
 * Class for reading and writing graphs from files.
 */
public class Io {
    private static ArrayList<String> readLines(Scanner scanner) {
        ArrayList<String> lines = new ArrayList<>();
        while (scanner.hasNext()) {
            lines.add(scanner.nextLine());
        }
        return lines;
    }
    private static String[] tokens(String line) {
        return line.trim().split("\\s+");
    }
    private static Graph readFormat1(ArrayList<String> lines) throws Exception {
        Graph graph = new Graph();
        boolean found = false;
        for (String line : lines) {
            if (tokens(line).length == 3 && tokens(line)[0].equals("e:")) {
                graph.addEdge(tokens(line)[1], tokens(line)[2]);
                found = true;
            }
        }
        if (!found) throw new Exception("Incorrect format");
        return graph;
    }
    private static boolean pFormat(ArrayList<String> lines) {
        if (lines.size() > 0) {
            String[] pline = tokens(lines.get(0));
            return pline[0].equals("p") && Integer.parseInt(pline[1]) > 0 && Integer.parseInt(pline[2]) > 0;
        } else {
            return false;
        }
    }
    private static Graph readFormat2(ArrayList<String> lines) throws Exception {
        if (pFormat(lines)) {
            Graph graph = new Graph();
            for (int i = 1; i < lines.size(); i++) {
                String[] tline = tokens(lines.get(i));
                if (tline.length > 0 && tline[0].equals("c")) continue;
                if (tline.length != 3 || !tline[0].equals("e")) throw new Exception("Incorrect format");
                graph.addEdge(tline[1], tline[2]);
            }
            return graph;
        } else {
            throw new Exception("Incorrect format");
        }
    }
    private static VertexPositioner readPositioner(ArrayList<String> vertices, ArrayList<String> lines) throws Exception {
        if (pFormat(lines)) {
            HashSet<String> found = new HashSet<>();
            TrivialPositioner positioner = new TrivialPositioner();
            for (int i = 1; i < lines.size(); i++) {
                String[] tline = tokens(lines.get(i));
                if (tline.length == 4 && tline[0].equals("c")) {
                    positioner.addVertex(tline[1], new Vec2(Double.parseDouble(tline[2]), Double.parseDouble(tline[3])));
                    found.add(tline[1]);
                }
            }
            for (String v : vertices) {
                if (!found.contains(v)) throw new Exception("Not all vertices positioned");
            }
            return positioner;
        } else {
            throw new Exception("Incorrect format");
        }
    }
    public static void writeGraph(PrintWriter writer, Graph graph, VertexPositioner vertexPositioner) {
        writer.println("p " + graph.vertices().size() + " " + graph.edges().size());
        graph.vertices().forEach(v -> {
            Vec2 pos = vertexPositioner.position(v);
            writer.println("c " + v + " " + pos.x + " " + pos.y);
        });
        graph.edges().forEach(e -> {
            writer.println("e " + e.v1 + " " + e.v2);
        });
    }
    public static class ReadResult {
        public final Optional<Graph> graph;
        public final Optional<VertexPositioner> vertexPositioner;
        public final String errorMessage;
        public ReadResult(Graph graph, VertexPositioner vertexPositioner) {
            this.graph = Optional.of(graph);
            this.vertexPositioner = Optional.of(vertexPositioner);
            this.errorMessage = "Ladattu onnistuneesti";
        }
        public ReadResult(Graph graph) {
            this.graph = Optional.of(graph);
            this.vertexPositioner = Optional.empty();
            this.errorMessage = "Ladattu onnistuneesti";
        }
        public ReadResult(String errorMessage) {
            this.graph = Optional.empty();
            this.vertexPositioner = Optional.empty();
            this.errorMessage = errorMessage;
        }
    }
    /**
     * Reads a graph from given scanner. Supports 2 different formats and automatically
     * deducts which is used. Supports also reading the vertex positioning.
     * @param scanner Scanner to read the graph from
     * @return ReadResult object. 
     * @throws java.lang.Exception
     */
    public static ReadResult readGraph(Scanner scanner) throws Exception {
        ArrayList<String> lines = readLines(scanner);
        try {
            Graph graph = readFormat2(lines);
            try {
                VertexPositioner vp = readPositioner(graph.vertices(), lines);
                return new ReadResult(graph, vp);
            } catch (Exception e) { }
            return new ReadResult(graph);
        } catch (Exception e) { }
        try {
            Graph graph = readFormat1(lines);
            return new ReadResult(graph);
        } catch (Exception e) { }
        return new ReadResult("Väärä tiedostomuoto");
    }
}
