package vv.io;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import vv.domain.Graph;

public class Io {
    public static Graph readGraph(File file) throws Exception {
        Scanner in = new Scanner(new FileReader(file));
        Graph graph = new Graph();
        while (in.hasNext()) {
            String token = in.next();
            if (token.equals("e:")) {
                String v1, v2;
                v1 = in.next();
                v2 = in.next();
                graph.addEdge(v1, v2);
            }
        }
        return graph;
    }
}
