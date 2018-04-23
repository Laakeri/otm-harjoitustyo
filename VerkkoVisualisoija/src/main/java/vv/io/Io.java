package vv.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import vv.domain.Graph;
import java.util.ArrayList;

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
    
    private static Graph readFormat1(ArrayList<String> lines) {
        Graph graph = new Graph();
        for (String line : lines) {
            if (tokens(line).length == 3 && tokens(line)[0].equals("e:")) {
                graph.addEdge(tokens(line)[1], tokens(line)[2]);
            }
        }
        return graph;
    }
    
    private static Graph readFormat2(ArrayList<String> lines) throws Exception {
        if (lines.size() > 0) {
            String[] pline = tokens(lines.get(0));
            if (pline[0].equals("p") && Integer.parseInt(pline[1]) > 0 && Integer.parseInt(pline[2]) > 0) {
                Graph graph = new Graph();
                for (int i = 1; i < lines.size(); i++) {
                    String[] tline = tokens(lines.get(i));
                    if (tline.length != 3 || !tline[0].equals("e")) throw new Exception("Incorrect format");
                    graph.addEdge(tline[1], tline[2]);
                }
                return graph;
            }
        }
        throw new Exception("Incorrect format");
    }
    
    public static Graph readGraph(File file) throws FileNotFoundException {
        ArrayList<String> lines = readLines(new Scanner(file));
        try {
            return readFormat2(lines);
        } catch (Exception e) { }
        return readFormat1(lines);
    }
}
