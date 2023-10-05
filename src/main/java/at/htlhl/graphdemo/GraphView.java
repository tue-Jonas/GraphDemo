package at.htlhl.graphdemo;

import com.brunomnsilva.smartgraph.containers.ContentZoomPane;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
        dialog.setTitle("Find the Shortest Path");
        dialog.setHeaderText("Select the source and target vertices:");

        // Set the button types
        ButtonType findButtonType = new ButtonType("Find", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(findButtonType, ButtonType.CANCEL);

        // Create the source and target labels and fields
        ComboBox<String> sourceComboBox = new ComboBox<>();
        ComboBox<String> targetComboBox = new ComboBox<>();

        // Populate ComboBoxes with vertex labels (assuming you have a method getVertexLabels() to obtain vertex labels)
        sourceComboBox.getItems().addAll(getVertexLabels());
        targetComboBox.getItems().addAll(getVertexLabels());

        GridPane grid = new GridPane();
        grid.add(new Label("Source Vertex:"), 0, 0);
        grid.add(sourceComboBox, 1, 0);
        grid.add(new Label("Target Vertex:"), 0, 1);
        grid.add(targetComboBox, 1, 1);

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
            System.out.println(graphControl.findShortestPath(pair.getKey(), pair.getValue()));
        });
    }


    private class SPEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            spActionMethod();
        }
    }

}
