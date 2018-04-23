package vv.ui;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import vv.domain.Graph;
import vv.io.Io;
import vv.domain.RandPositioner;

public class Ui extends Application {
    private Group graphDraw;
    private FileChooser fileChooser;
    
    @Override
    public void init() {
        
    }
    
    private void openGraph(Stage stage) {
        graphDraw.getChildren().clear();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Graph graph = Io.readGraph(file);
                GraphPresentation gr = new GraphPresentation(graph, new RandPositioner(), 800, 600);
                gr.drawToGroup(graphDraw);
            } catch (FileNotFoundException ex) { 
                System.out.println("File not found: " + ex.getMessage() + "\nThis should not happen");
                System.exit(2);
            }
        }
    }
    
    @Override
    public void start(Stage stage) {
        System.out.println("sovellus käynnistyy");
        stage.setTitle("Verkkovisualisoija");
        fileChooser = new FileChooser();
        Button openButton = new Button("Open graph");
        Pane toolBar = new HBox(openButton);
        toolBar.setPadding(new Insets(5, 10, 20, 10));
        graphDraw = new Group();
        Pane main = new VBox(toolBar, graphDraw);
        
        stage.setScene(new Scene(main, 800, 650));
        stage.show();
        
        openButton.setOnAction(e -> {
            openGraph(stage);
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
