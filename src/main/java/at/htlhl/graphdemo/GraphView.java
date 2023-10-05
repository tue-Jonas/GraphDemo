package at.htlhl.graphdemo;

import com.brunomnsilva.smartgraph.containers.ContentZoomPane;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Jonas Tuechler
 */
public class GraphView {

    // FIELDS ---------------------------------------------------------------------------------------------------------

    private SmartGraphPanel<VertexData, EdgeData> smartGraphPanel;
    private ContentZoomPane contentZoomPane;
    private GraphControl graphControl;


    // INSTANCE CREATION ----------------------------------------------------------------------------------------------

    public GraphView(GraphControl graphControl) {
        this.graphControl = graphControl;

        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        smartGraphPanel = new SmartGraphPanel<>(graphControl.getGraph(), strategy);
        smartGraphPanel.setAutomaticLayout(true);

        contentZoomPane = new ContentZoomPane(smartGraphPanel);
        contentZoomPane.setStyle("-fx-background-color: #F4FFFB");

        // Create "Shortest Path" Button
        Button spButton = new Button("Shortest Path");
        spButton.setOnAction(new SPEventHandler());

        // Create Toolbar
        ToolBar toolBar = new ToolBar(spButton);

        contentZoomPane.setTop(toolBar);
    }

    /**
     * IMPORTANT! - Called after scene is displayed, so we can initialize the graph visualization
     */
    public void initAfterVisible() {
        smartGraphPanel.init();
    }

    public Pane getPane() {
        return contentZoomPane;
    }

    private List<String> getVertexLabels() {
        List<String> vertexLabels = new ArrayList<>();
        for (Vertex<VertexData> vertex : graphControl.getGraph().vertices()) {
            vertexLabels.add(vertex.element().getName());
        }
        return vertexLabels;
    }

    private void spActionMethod() {
        // Create the custom dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Find the shortest path");
        dialog.setHeaderText("Select the source and target vertices:");

        // Set the button types
        ButtonType findButtonType = new ButtonType("Route", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(findButtonType, ButtonType.CANCEL);

        // Create the source and target labels and fields
        ComboBox<String> sourceComboBox = new ComboBox<>();
        ComboBox<String> targetComboBox = new ComboBox<>();

        // Populate ComboBoxes with vertex labels (assuming you have a method getVertexLabels() to obtain vertex labels)
        sourceComboBox.getItems().addAll(getVertexLabels());
        targetComboBox.getItems().addAll(getVertexLabels());

        // source and target labels
        Label sourceLabel = new Label("Source:");
        Label targetLabel = new Label("Target:");

        GridPane grid = new GridPane();
        grid.add(sourceLabel, 0, 0);
        grid.add(sourceComboBox, 1, 0);
        grid.add(targetLabel, 0, 1);
        grid.add(targetComboBox, 1, 1);

        GridPane.setMargin(targetLabel, new Insets(10, 10, 0, 0));
        GridPane.setMargin(targetComboBox, new Insets(10, 0, 0, 0));

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a Pair<String, String> when the find button is clicked
        dialog.setResultConverter(new Callback<ButtonType, Pair<String, String>>() {
            @Override
            public Pair<String, String> call(ButtonType buttonType) {
                if (buttonType == findButtonType) {
                    return new Pair<>(sourceComboBox.getValue(), targetComboBox.getValue());
                }
                return null;
            }
        });

        // Process the result
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            List<Vertex<VertexData>> path = graphControl.findShortestPath(pair.getKey(), pair.getValue());
            if (path != null) {
                StringBuilder pathStr = new StringBuilder("Shortest path: ");
                for (Vertex<VertexData> vertex : path) {
                    pathStr.append(vertex.element().getName()).append(" -> ");
                }
                pathStr.delete(pathStr.length() - 4, pathStr.length());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Shortest Path Result");
                alert.setHeaderText(null);
                alert.setContentText(pathStr.toString());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Path Found");
                alert.setHeaderText(null);
                alert.setContentText("No path found between " + pair.getKey() + " and " + pair.getValue());
                alert.showAndWait();
            }
        });
    }


    private class SPEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            spActionMethod();
        }
    }

}
