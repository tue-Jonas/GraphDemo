package at.htlhl.graphdemo;

import com.brunomnsilva.smartgraph.containers.ContentZoomPane;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;

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

        // Create "Test" Button
        Button testButton = new Button("Test");
        testButton.setOnAction(new TestEventHandler());

        // Create Toolbar
        ToolBar toolBar = new ToolBar(testButton);

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

    private void testActionMethod() {
        Vertex<VertexData> foundVertex = graphControl.findFirstVertexByName("C");
        if (foundVertex != null) {
            smartGraphPanel.getStylableVertex(foundVertex).setStyleClass("htlVertex");
        }
    }

    private class TestEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            testActionMethod();
        }
    }

}
