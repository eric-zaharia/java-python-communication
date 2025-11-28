package agent;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.List;

public class RedisResultListener implements Runnable {
    private final RedisCommands<String, String> redis;
    private final String stream;

    public RedisResultListener(String redisUri, String agentId) {
        RedisClient client = RedisClient.create(redisUri);

        this.redis = client.connect().sync();
        this.stream = "results:" + agentId;
    }

    @Override
    public void run() {
        System.out.println("listening on stream: " + stream);

        String lastId = "$";

        try {
            while (!Thread.currentThread().isInterrupted()) {
                List<StreamMessage<String, String>> messages =
                        redis.xread(
                                XReadArgs.Builder.block(30_000),
                                XReadArgs.StreamOffset.from(stream, lastId)
                        );

                if (messages == null || messages.isEmpty()) {
                    continue;
                }

                for (StreamMessage<String, String> msg : messages) {
                    lastId = msg.getId();

                    String jobId = msg.getBody().get("job_id");
                    String result = msg.getBody().get("result");

                    System.out.println("received result for job " + jobId + ": " + result);
                }
            }
        }  catch (Exception e) {
            System.err.println("listener stopped: " + e.getMessage());
        }
    }
}
