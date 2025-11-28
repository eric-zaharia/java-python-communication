package agent;

import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

public class InferenceClient {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final HttpClient client;
    private final URI inferenceUri;

    public InferenceClient(String inferenceUrl) {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(2))
                .build();

        this.inferenceUri = URI.create(inferenceUrl);
    }

    public void startInference(
            String agentId,
            String jobId,
            Map<String, Object> payload
    ) throws Exception {

        Map<String, Object> body = Map.of(
                "agent_id", agentId,
                "job_id", jobId,
                "payload", payload
        );

        String json = MAPPER.writeValueAsString(body);

        System.out.println(json);
        System.out.println(inferenceUri);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(inferenceUri)
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("failed to start inference");
        }
    }
}
