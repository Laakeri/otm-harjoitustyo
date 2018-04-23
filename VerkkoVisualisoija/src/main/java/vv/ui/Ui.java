package vv.ui;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

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
        
        Button openButton = new Button("Open graph");
        FileChooser fileChooser = new FileChooser();
        Pane toolBar = new HBox(openButton);
        toolBar.setPadding(new Insets(5, 10, 20, 10));
        Group graphDraw = new Group();
        Pane main = new VBox(toolBar, graphDraw);
        
        stage.setScene(new Scene(main, 800, 650));
        stage.show();
        
        openButton.setOnAction(e->{
            graphDraw.getChildren().clear();
            try {
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    Graph graph = Io.readGraph(file);
                    GraphPresentation gr = new GraphPresentation(graph, new RandPositioner(), 800, 600);
                    gr.drawToGroup(graphDraw);
                }
            } catch (Exception ex) {}

        });
    }
  
    @Override
    public void stop() {
        System.out.println("sovellus sulkeutuu");
    }
  
    public static void main(String[] args) {
        launch(args);
    }
}
