package vv.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import vv.domain.Graph;
import vv.io.Io;
import vv.domain.RandPositioner;
import vv.domain.TrivialPositioner;

public class Ui extends Application {
    private Pane graphDraw;
    private GraphPresentation graphPresentation;
    private Text helpText;
    
    @Override
    public void init() {
        
    }
    
    private void refreshDraw() {
        graphDraw.getChildren().clear();
        graphPresentation.drawToPane(graphDraw);
    }
    
    private void openGraph(Stage stage, FileChooser fileChooser) {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Graph graph = Io.readGraph(new Scanner(file));
                graphPresentation = new GraphPresentation(graph, new RandPositioner(graph), 800, 600);
                refreshDraw();
                setHelpText("Tiedosto avattu!");
            } catch (FileNotFoundException ex) { 
                System.out.println("File not found: " + ex.getMessage() + "\nThis should not happen");
                System.exit(2);
            }
        }
    }
    private void askWithDialog(String title, String headerText, String contentText, Consumer<String> fn) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String input = result.get();
            fn.accept(input);
        }
    }
    private void addVertex() {
        askWithDialog("Uusi solmu", "Nimeä uusi solmu", "Uuden solmun nimi", vertex -> {
            if (!graphPresentation.hasVertex(vertex)) {
                graphPresentation.addVertex(vertex);
                refreshDraw();
                setHelpText("Solmu " + vertex + " lisätty!");
            } else {
                setHelpText("Solmu " + vertex + " on jo olemassa");
            }
        });
    }
    private void addEdge() {
        setHelpText("Klikkaa jotain solmua");
        graphPresentation.addOnClickEvents(vertex1 -> {
            setHelpText("Klikkaa toista solmua");
            graphPresentation.addOnClickEvents(vertex2 -> {
                graphPresentation.addEdge(vertex1, vertex2);
                refreshDraw();
                setHelpText("Kaari " + vertex1 + " - " + vertex2 + " lisätty!");
            });
        });
    }
    private void removeVertex() {
        setHelpText("Klikkaa jotain solmua");
        graphPresentation.addOnClickEvents(vertex -> {
            graphPresentation.removeVertex(vertex);
            refreshDraw();
            setHelpText("Solmu " + vertex + " poistettu!");
        });
    }
    private void removeEdge() {
        setHelpText("Klikkaa jotain solmua");
        graphPresentation.addOnClickEvents(vertex1 -> {
            setHelpText("Klikkaa toista solmua");
            graphPresentation.addOnClickEvents(vertex2 -> {
                graphPresentation.removeEdge(vertex1, vertex2);
                refreshDraw();
                setHelpText("Kaari " + vertex1 + " - " + vertex2 + " poistettu!");
            });
        });
    }
    private void setHelpText(String text) {
        helpText.setText(text);
    }
    @Override
    public void start(Stage stage) {
        System.out.println("sovellus käynnistyy");
        stage.setTitle("Verkkovisualisoija");
        FileChooser fileChooser = new FileChooser();
        Button openButton = new Button("Avaa tiedosto");
        Button saveButton = new Button("Tallenna");
        Button newVertexButton = new Button("Uusi solmu");
        Button newEdgeButton = new Button("Uusi kaari");
        Button removeVertexButton = new Button("Poista solmu");
        Button removeEdgeButton = new Button("Poista kaari");
        Pane toolBar = new HBox(20, openButton, saveButton, newVertexButton, newEdgeButton, removeVertexButton, removeEdgeButton);
        toolBar.setPadding(new Insets(5, 10, 20, 10));
        helpText = new Text("Tervetuloa! Voit aloittaa avaamalla tiedoston tai suoraan luomalla uusia solmuja.");
        graphDraw = new Pane();
        graphDraw.setStyle("-fx-border-style: solid hidden hidden hidden;");
        Pane main = new VBox(toolBar, helpText, graphDraw);
        
        stage.setScene(new Scene(main, 800, 700));
        stage.show();
        
        graphPresentation = new GraphPresentation(new Graph(), new TrivialPositioner(), 800, 600);
        refreshDraw();
        
        openButton.setOnAction(e -> {
            openGraph(stage, fileChooser);
        });
        newVertexButton.setOnAction(e -> {
            addVertex();
        });
        newEdgeButton.setOnAction(e -> {
            addEdge();
        });
        removeVertexButton.setOnAction(e -> {
            removeVertex();
        });
        removeEdgeButton.setOnAction(e -> {
            removeEdge();
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
