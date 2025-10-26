package madiyar.smartflow.spring.util;

import madiyar.smartflow.spring.model.Graph;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GraphLoader {

    public static List<Graph> loadGraphsFromFile(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(new File(filePath),
                new TypeReference<Map<String, Object>>() {});

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> graphsList = (List<Map<String, Object>>) jsonMap.get("graphs");

        return mapper.convertValue(graphsList, new TypeReference<List<Graph>>() {});
    }
}