package agent;

import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Map<String, Object> DEFAULT_PAYLOAD = Map.of("input", "static_payload");

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("agent id must be passed as argument");
            System.exit(1);
        }

        String agentId = args[0];

        String redisUri = "redis://localhost:6379";
        String inferenceUrl = "http://localhost:8000/infer";

        InferenceClient client = new InferenceClient(inferenceUrl);

        Thread listener = new Thread(
                new RedisResultListener(redisUri, agentId)
        );
        listener.setDaemon(true);
        listener.start();

        System.out.println("agent " + agentId + " started.");
        System.out.println("type a job_id to send inference, or 'exit' to quit.");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                System.out.println("shutting down agent...");
                listener.interrupt();
                break;
            }

            if (line.isEmpty()) {
                continue;
            }

            try {
                client.startInference(agentId, line, DEFAULT_PAYLOAD);

                System.out.println("submitted job " + line);

            } catch (Exception e) {
                System.err.println("failed to submit job: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("agent stopped.");
    }
}
