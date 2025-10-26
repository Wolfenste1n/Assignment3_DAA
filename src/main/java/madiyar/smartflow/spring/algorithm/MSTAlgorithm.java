package madiyar.smartflow.spring.algorithm;

import madiyar.smartflow.spring.model.Graph;
import madiyar.smartflow.spring.model.AlgorithmResult;

public interface MSTAlgorithm {
    AlgorithmResult findMST(Graph graph);
    String getAlgorithmName();
}