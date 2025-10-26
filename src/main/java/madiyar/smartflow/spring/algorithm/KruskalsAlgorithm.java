package madiyar.smartflow.spring.algorithm;

import madiyar.smartflow.spring.model.Graph;
import madiyar.smartflow.spring.model.Edge;
import madiyar.smartflow.spring.model.AlgorithmResult;
import java.util.*;

public class KruskalsAlgorithm implements MSTAlgorithm {

    @Override
    public AlgorithmResult findMST(Graph graph) {
        long startTime = System.nanoTime();
        int operationsCount = 0;

        List<Edge> mstEdges = new ArrayList<>();
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());

        sortedEdges.sort(Comparator.comparingInt(Edge::getWeight));
        operationsCount += sortedEdges.size() * (int) Math.log(sortedEdges.size());

        UnionFind uf = new UnionFind(graph.getNodes());
        operationsCount += graph.getVertexCount();

        for (Edge edge : sortedEdges) {
            operationsCount++;

            String rootFrom = uf.find(edge.getFrom());
            String rootTo = uf.find(edge.getTo());
            operationsCount += 2;

            if (!rootFrom.equals(rootTo)) {
                mstEdges.add(edge);
                uf.union(edge.getFrom(), edge.getTo());
                operationsCount += 2;
            }

            if (mstEdges.size() == graph.getVertexCount() - 1) {
                break;
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        int totalCost = mstEdges.stream().mapToInt(Edge::getWeight).sum();
        operationsCount += mstEdges.size();

        return new AlgorithmResult(mstEdges, totalCost, operationsCount, executionTimeMs);
    }

    @Override
    public String getAlgorithmName() {
        return "Kruskal's Algorithm";
    }

    private static class UnionFind {
        private Map<String, String> parent;
        private Map<String, Integer> rank;

        public UnionFind(List<String> nodes) {
            parent = new HashMap<>();
            rank = new HashMap<>();

            for (String node : nodes) {
                parent.put(node, node);
                rank.put(node, 0);
            }
        }

        public String find(String node) {
            if (!parent.get(node).equals(node)) {
                parent.put(node, find(parent.get(node)));
            }
            return parent.get(node);
        }

        public void union(String node1, String node2) {
            String root1 = find(node1);
            String root2 = find(node2);

            if (!root1.equals(root2)) {

                if (rank.get(root1) < rank.get(root2)) {
                    parent.put(root1, root2);
                } else if (rank.get(root1) > rank.get(root2)) {
                    parent.put(root2, root1);
                } else {
                    parent.put(root2, root1);
                    rank.put(root1, rank.get(root1) + 1);
                }
            }
        }
    }
}