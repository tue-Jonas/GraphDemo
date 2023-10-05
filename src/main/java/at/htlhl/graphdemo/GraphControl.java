package at.htlhl.graphdemo;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;

/**
 * @author Jonas Tuechler
 */
public class GraphControl {

    private Graph<VertexData, EdgeData> graph;

    public GraphControl() {
        this.graph = new GraphEdgeList<>();

        buildGraph();
    }

    private void buildGraph() {
        VertexData vertexA = new VertexData("A");
        VertexData vertexB = new VertexData("B");
        VertexData vertexC = new VertexData("C");
        VertexData vertexD = new VertexData("D");
        VertexData vertexE = new VertexData("E");
        VertexData vertexF = new VertexData("F");
        VertexData vertexG = new VertexData("G");

        graph.insertVertex(vertexA);
        graph.insertVertex(vertexB);
        graph.insertVertex(vertexC);
        graph.insertVertex(vertexD);
        graph.insertVertex(vertexE);
        graph.insertVertex(vertexF);
        graph.insertVertex(vertexG);

        graph.insertEdge(vertexA, vertexB, new EdgeData(1));
        graph.insertEdge(vertexB, vertexA, new EdgeData(1));
        graph.insertEdge(vertexA, vertexC, new EdgeData(2));
        graph.insertEdge(vertexA, vertexD, new EdgeData(3));
        graph.insertEdge(vertexA, vertexE, new EdgeData(4));
        graph.insertEdge(vertexA, vertexF, new EdgeData(5));
        graph.insertEdge(vertexA, vertexG, new EdgeData(6));
    }

    // API ------------------------------------------------------------------------------------------------------------

    public Vertex<VertexData> findFirstVertexByName(String name) {
        for (Vertex<VertexData> vertex : graph.vertices()) {
            if (vertex.element().getName().equals(name)) {
                return vertex;
            }
        }
        return null;
    }

    public Graph<VertexData, EdgeData> getGraph() {
        return graph;
    }

}
