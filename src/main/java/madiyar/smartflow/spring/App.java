package madiyar.smartflow.spring;

import madiyar.smartflow.spring.model.*;
import madiyar.smartflow.spring.algorithm.*;
import madiyar.smartflow.spring.util.GraphLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class App {
    public static void main(String[] args) {
        try {
            String inputFile = "input/ass_3_input.json";
            String outputFile = "output/ass_3_output.json";

            List<Graph> graphs = GraphLoader.loadGraphsFromFile(inputFile);
            System.out.println("Loaded " + graphs.size() + " graphs from " + inputFile);

            MSTAlgorithm prims = new PrimsAlgorithm();
            MSTAlgorithm kruskals = new KruskalsAlgorithm();

            List<Map<String, Object>> results = new ArrayList<>();

            for (Graph graph : graphs) {
                System.out.println("\nProcessing Graph " + graph.getId() +
                        " (Vertices: " + graph.getVertexCount() +
                        ", Edges: " + graph.getEdgeCount() + ")");

                AlgorithmResult primResult = prims.findMST(graph);
                System.out.println("Prim's Algorithm - Cost: " + primResult.getTotalCost() +
                        ", Time: " + String.format(Locale.US, "%.2f", primResult.getExecutionTimeMs()) + "ms");

                AlgorithmResult kruskalResult = kruskals.findMST(graph);
                System.out.println("Kruskal's Algorithm - Cost: " + kruskalResult.getTotalCost() +
                        ", Time: " + String.format(Locale.US, "%.2f", kruskalResult.getExecutionTimeMs()) + "ms");

                Map<String, Object> graphResult = new LinkedHashMap<>();
                graphResult.put("graph_id", graph.getId());

                Map<String, Object> inputStats = new LinkedHashMap<>();
                inputStats.put("vertices", graph.getVertexCount());
                inputStats.put("edges", graph.getEdgeCount());
                graphResult.put("input_stats", inputStats);
                graphResult.put("prim", buildAlgorithmResult(primResult));
                graphResult.put("kruskal", buildAlgorithmResult(kruskalResult));

                results.add(graphResult);
            }

            writeResultsToFile(results, outputFile);
            System.out.println("\nResults written to " + outputFile);

        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Map<String, Object> buildAlgorithmResult(AlgorithmResult result) {
        Map<String, Object> algoResult = new LinkedHashMap<>();
        algoResult.put("mst_edges", result.getMstEdges());
        algoResult.put("total_cost", result.getTotalCost());
        algoResult.put("operations_count", result.getOperationsCount());

        double executionTime = Double.parseDouble(String.format(Locale.US, "%.2f", result.getExecutionTimeMs()));
        algoResult.put("execution_time_ms", executionTime);

        return algoResult;
    }

    private static void writeResultsToFile(List<Map<String, Object>> results, String outputFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Map<String, Object> output = new LinkedHashMap<>();
        output.put("results", results);

        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        mapper.writeValue(new File(outputFile), output);
    }
}