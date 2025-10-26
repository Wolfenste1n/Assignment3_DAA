package madiyar.smartflow.spring.algorithm;

import madiyar.smartflow.spring.model.Graph;
import madiyar.smartflow.spring.model.Edge;
import madiyar.smartflow.spring.model.AlgorithmResult;
import java.util.*;

public class PrimsAlgorithm implements MSTAlgorithm {

    @Override
    public AlgorithmResult findMST(Graph graph) {
        long startTime = System.nanoTime();
        int operationsCount = 0;

        List<Edge> mstEdges = new ArrayList<>();
        Set<String> inMST = new HashSet<>();
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        Map<String, List<Edge>> adjacencyList = buildAdjacencyList(graph);

        operationsCount += graph.getEdgeCount();

        if (!graph.getNodes().isEmpty()) {
            String startNode = graph.getNodes().get(0);
            inMST.add(startNode);
            operationsCount++;

            if (adjacencyList.containsKey(startNode)) {
                minHeap.addAll(adjacencyList.get(startNode));
                operationsCount += adjacencyList.get(startNode).size();
            }

            while (!minHeap.isEmpty() && inMST.size() < graph.getVertexCount()) {
                operationsCount++;

                Edge currentEdge = minHeap.poll();
                operationsCount++;

                String newNode = null;
                if (inMST.contains(currentEdge.getFrom()) && !inMST.contains(currentEdge.getTo())) {
                    newNode = currentEdge.getTo();
                } else if (inMST.contains(currentEdge.getTo()) && !inMST.contains(currentEdge.getFrom())) {
                    newNode = currentEdge.getFrom();
                }

                operationsCount += 2;

                if (newNode != null) {
                    mstEdges.add(currentEdge);
                    inMST.add(newNode);
                    operationsCount += 2;
                    if (adjacencyList.containsKey(newNode)) {
                        for (Edge edge : adjacencyList.get(newNode)) {
                            if (!inMST.contains(edge.getTo()) || !inMST.contains(edge.getFrom())) {
                                minHeap.add(edge);
                                operationsCount += 2;
                            }
                        }
                    }
                }
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        int totalCost = mstEdges.stream().mapToInt(Edge::getWeight).sum();
        operationsCount += mstEdges.size();

        return new AlgorithmResult(mstEdges, totalCost, operationsCount, executionTimeMs);
    }

    private Map<String, List<Edge>> buildAdjacencyList(Graph graph) {
        Map<String, List<Edge>> adjacencyList = new HashMap<>();

        for (Edge edge : graph.getEdges()) {
            adjacencyList.computeIfAbsent(edge.getFrom(), k -> new ArrayList<>()).add(edge);
            adjacencyList.computeIfAbsent(edge.getTo(), k -> new ArrayList<>()).add(
                    new Edge(edge.getTo(), edge.getFrom(), edge.getWeight())
            );
        }

        return adjacencyList;
    }

    @Override
    public String getAlgorithmName() {
        return "Prim's Algorithm";
    }
}