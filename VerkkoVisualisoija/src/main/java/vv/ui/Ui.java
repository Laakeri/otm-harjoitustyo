package vv.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
    static final int WINDOWWIDTH = 1000;
    static final int UIHEIGHT = 80;
    static final int WINDOWHEIGHT = 700;
    
    @Override
    public void init() {
        
    }
    private void refreshDraw() {
        graphDraw.getChildren().clear();
        graphPresentation.drawToPane(graphDraw);
    }
    private void openGraph(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Avaa");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Scanner sc = new Scanner(file);
                try {
                    Io.ReadResult res = Io.readGraph(sc);
                    if (res.graph.isPresent()) {
                        if (res.vertexPositioner.isPresent()) {
                            graphPresentation = new GraphPresentation(res.graph.get(), res.vertexPositioner.get(), WINDOWWIDTH, WINDOWHEIGHT - UIHEIGHT);
                        } else {
                            graphPresentation = new GraphPresentation(res.graph.get(), new RandPositioner(res.graph.get()), WINDOWWIDTH, WINDOWHEIGHT - UIHEIGHT);
                        }
                        refreshDraw();
                    }
                    setHelpText(res.errorMessage);
                } catch (Exception e) {
                    setHelpText(e.getMessage());
                }
            } catch (FileNotFoundException ex) { 
                setHelpText("File not found: " + ex.getMessage() + ". This should not happen");
            }
        }
    }
    private void saveGraph(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Tallenna");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                PrintWriter pw = new PrintWriter(file);
                Io.writeGraph(pw, graphPresentation.graph(), graphPresentation.vertexPositioner());
                pw.close();
                setHelpText("Tallennettu");
            } catch (FileNotFoundException ex) {
                setHelpText("File not found: " + ex.getMessage() + ". This should not happen");
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
                setHelpText("Solmu " + vertex + " on jo olemassa!");
            }
        });
    }
    private void addEdge() {
        setHelpText("Klikkaa jotain solmua");
        graphPresentation.addOnClickEvents(vertex1 -> {
            setHelpText("Klikkaa toista solmua");
            graphPresentation.addOnClickEvents(vertex2 -> {
                if (graphPresentation.addEdge(vertex1, vertex2)) {
                    refreshDraw();  
                    setHelpText("Kaari " + vertex1 + " - " + vertex2 + " lisätty!");
                } else {
                    setHelpText("Virhe kaaren lisäyksessä!");
                }
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
                if (graphPresentation.removeEdge(vertex1, vertex2)) {
                    refreshDraw();
                    setHelpText("Kaari " + vertex1 + " - " + vertex2 + " poistettu!");
                } else {
                    setHelpText("Virhe kaaren poistamisessa!");
                }
            });
        });
    }
    private void setHelpText(String text) {
        helpText.setText(text);
    }
    private void resetGraph() {
        graphPresentation = new GraphPresentation(new Graph(), new TrivialPositioner(), WINDOWWIDTH, WINDOWHEIGHT - UIHEIGHT);
        refreshDraw();
    }
    private void uiResetGraph() {
        resetGraph();
        setHelpText("Näkymä tyhjennetty!");
    }
    private void repositionGraph() {
        graphPresentation.reposition(new RandPositioner(graphPresentation.graph()));
        refreshDraw();
        setHelpText("Aseteltu uudelleen!");
    }
    @Override
    public void start(Stage stage) {
        System.out.println("sovellus käynnistyy");
        stage.setTitle("Verkkovisualisoija");
        Button openButton = new Button("Avaa tiedosto");
        Button saveButton = new Button("Tallenna");
        Button newVertexButton = new Button("Uusi solmu");
        Button newEdgeButton = new Button("Uusi kaari");
        Button removeVertexButton = new Button("Poista solmu");
        Button removeEdgeButton = new Button("Poista kaari");
        Button repositionButton = new Button("Asettele uudelleen");
        Button resetButton = new Button("Tyhjennä");
        Pane toolBar = new HBox(20, openButton, saveButton, newVertexButton, newEdgeButton, removeVertexButton, removeEdgeButton, repositionButton, resetButton);
        toolBar.setPadding(new Insets(5, 10, 20, 10));
        helpText = new Text("Tervetuloa! Voit aloittaa avaamalla tiedoston tai suoraan luomalla uusia solmuja.");
        graphDraw = new Pane();
        graphDraw.setStyle("-fx-border-style: solid hidden hidden hidden;");
        Pane main = new VBox(toolBar, helpText, graphDraw);
        stage.setScene(new Scene(main, WINDOWWIDTH, WINDOWHEIGHT));
        stage.show();
        resetGraph();
        openButton.setOnAction(e -> {
            openGraph(stage);
        });
        saveButton.setOnAction(e -> {
            saveGraph(stage);
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
        repositionButton.setOnAction(e -> {
            repositionGraph();
        });
        resetButton.setOnAction(e -> {
            uiResetGraph();
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
