package vv.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;

import vv.domain.Graph;
import vv.io.Io;
import vv.domain.RandPositioner;

public class Ui extends Application {
    @Override
    public void init() {
        
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("sovellus käynnistyy");
        stage.setTitle("Verkkovisualisoija");
        
        Io io = new Io();
        Graph graph = io.readGraph("example.graph");
        
        GraphPresentation gr = new GraphPresentation(graph, new RandPositioner(), 800, 600);
        
        Group root = new Group();
        gr.drawToPane(root);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
  
    @Override
    public void stop() {
        System.out.println("sovellus sulkeutuu");
    }
  
    public static void main(String[] args) {
        launch(args);
    }
}
