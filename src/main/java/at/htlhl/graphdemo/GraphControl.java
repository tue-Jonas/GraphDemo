package at.htlhl.graphdemo;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;

import java.util.*;

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
        VertexData seattle = createCityAndInsert("Seattle");
        VertexData sanFrancisco = createCityAndInsert("San Francisco");
        VertexData losAngeles = createCityAndInsert("Los Angeles");
        VertexData riverside = createCityAndInsert("Riverside");
        VertexData phoenix = createCityAndInsert("Phoenix");
        VertexData chicago = createCityAndInsert("Chicago");
        VertexData boston = createCityAndInsert("Boston");
        VertexData newYork = createCityAndInsert("New York");
        VertexData atlanta = createCityAndInsert("Atlanta");
        VertexData miami = createCityAndInsert("Miami");
        VertexData dallas = createCityAndInsert("Dallas");
        VertexData houston = createCityAndInsert("Houston");
        VertexData detroit = createCityAndInsert("Detroit");
        VertexData philadelphia = createCityAndInsert("Philadelphia");
        VertexData washington = createCityAndInsert("Washington");
        graph.insertEdge(seattle, chicago, new EdgeData(1737));
        graph.insertEdge(seattle, sanFrancisco, new EdgeData(678));
        graph.insertEdge(sanFrancisco, riverside, new EdgeData(386));
        graph.insertEdge(sanFrancisco, losAngeles, new EdgeData(348));
        graph.insertEdge(losAngeles, riverside, new EdgeData(50));
        graph.insertEdge(losAngeles, phoenix, new EdgeData(357));
        graph.insertEdge(riverside, phoenix, new EdgeData(307));
        graph.insertEdge(riverside, chicago, new EdgeData(1704));
        graph.insertEdge(phoenix, dallas, new EdgeData(887));
        graph.insertEdge(phoenix, houston, new EdgeData(1015));
        graph.insertEdge(dallas, chicago, new EdgeData(805));
        graph.insertEdge(dallas, atlanta, new EdgeData(721));
        graph.insertEdge(dallas, houston, new EdgeData(225));
        graph.insertEdge(houston, atlanta, new EdgeData(702));
        graph.insertEdge(houston, miami, new EdgeData(968));
        graph.insertEdge(atlanta, chicago, new EdgeData(588));
        graph.insertEdge(atlanta, washington, new EdgeData(543));
        graph.insertEdge(atlanta, miami, new EdgeData(604));
        graph.insertEdge(miami, washington, new EdgeData(923));
        graph.insertEdge(chicago, detroit, new EdgeData(238));
        graph.insertEdge(detroit, boston, new EdgeData(613));
        graph.insertEdge(detroit, washington, new EdgeData(396));
        graph.insertEdge(detroit, newYork, new EdgeData(482));
        graph.insertEdge(boston, newYork, new EdgeData(190));
        graph.insertEdge(newYork, philadelphia, new EdgeData(81));
        graph.insertEdge(philadelphia, washington, new EdgeData(123));
    }

    private VertexData createCityAndInsert(String name) {
        VertexData city = new VertexData(name);
        graph.insertVertex(city);
        return city;
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

    /**
     * Prim's algorithm for finding a minimum spanning tree (MST) of a weighted undirected graph.
     */
    public List<Edge<EdgeData, VertexData>> primAlgorithm() {
        // Create a set to store the vertices included in MST
        Set<Vertex<VertexData>> mstSet = new HashSet<>();

        // Create a priority queue to store the edges, with their weights as the key
        PriorityQueue<Edge<EdgeData, VertexData>> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.element().getDistance()));

        // Choose an arbitrary vertex to start from
        Vertex<VertexData> startVertex = graph.vertices().iterator().next();
        mstSet.add(startVertex);

        // Add all edges of the starting vertex to the priority queue
        for (Edge<EdgeData, VertexData> edge : graph.incidentEdges(startVertex)) {
            pq.add(edge);
        }

        // Create a list to store the edges in the MST
        List<Edge<EdgeData, VertexData>> mst = new ArrayList<>();

        // While the MST doesn't include all vertices
        while (mstSet.size() < graph.vertices().size()) {
            // Get the edge with the smallest weight that connects the MST to a vertex not in the MST
            Edge<EdgeData, VertexData> minEdge = pq.poll();

            // Get the vertex connected by minEdge that is not in the MST
            Vertex<VertexData> nextVertex = graph.opposite(mstSet.iterator().next(), minEdge);

            // Add the edge to the MST and the vertex to the MST set
            mst.add(minEdge);
            mstSet.add(nextVertex);

            // Add all edges of the vertex to the priority queue
            for (Edge<EdgeData, VertexData> edge : graph.incidentEdges(nextVertex)) {
                if (!mstSet.contains(graph.opposite(nextVertex, edge))) {
                    pq.add(edge);
                }
            }
        }

        return mst;
    }

    public List<Vertex<VertexData>> findShortestPath(String sourceName, String targetName) {
        Vertex<VertexData> source = findFirstVertexByName(sourceName);
        Vertex<VertexData> target = findFirstVertexByName(targetName);

        if (source == null || target == null) {
            throw new IllegalArgumentException("Source or target vertex not found.");
        }

        Map<Vertex<VertexData>, Double> distances = new HashMap<>();
        Map<Vertex<VertexData>, Vertex<VertexData>> predecessors = new HashMap<>();
        PriorityQueue<Vertex<VertexData>> pq = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        for (Vertex<VertexData> vertex : graph.vertices()) {
            if (vertex.equals(source)) {
                distances.put(vertex, 0.0);
            } else {
                distances.put(vertex, Double.POSITIVE_INFINITY);
            }
            predecessors.put(vertex, null);
        }

        pq.add(source);

        while (!pq.isEmpty()) {
            Vertex<VertexData> current = pq.poll();

            for (Edge<EdgeData, VertexData> edge : graph.incidentEdges(current)) {
                Vertex<VertexData> opposite = graph.opposite(current, edge);
                double newDist = distances.get(current) + edge.element().getDistance();
                if (newDist < distances.get(opposite)) {
                    distances.put(opposite, newDist);
                    predecessors.put(opposite, current);
                    pq.remove(opposite);  // Re-balance the priority queue
                    pq.add(opposite);
                }
            }
        }

        LinkedList<Vertex<VertexData>> path = new LinkedList<>();
        for (Vertex<VertexData> at = target; at != null; at = predecessors.get(at)) {
            path.addFirst(at);
        }

        return path.size() > 1 ? path : null;  // Return null if only the target vertex is in the path (no path found)
    }

    public Graph<VertexData, EdgeData> getGraph() {
        return graph;
    }

}
