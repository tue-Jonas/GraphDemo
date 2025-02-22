package at.htlhl.graphdemo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GraphControl graphControl = new GraphControl();
        GraphView graphView = new GraphView(graphControl);

        Scene scene = new Scene(graphView.getPane(), 1024, 768);
        stage.setTitle("GraphDemo");
        stage.setScene(scene);
        stage.show();

        graphView.initAfterVisible();
    }

    public static void main(String[] args) {
        launch();
    }
}