import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import test.PythonTaskServiceGrpc;
import test.Test;

import java.io.IOException;
import java.util.Scanner;

public class JavaAsyncServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50052)
                .addService(new JavaCallbackServiceImpl())
                .build()
                .start();

        System.out.println("JavaCallbackService running on port 50052");

        new Thread(JavaAsyncServer::commandLoop).start();

        server.awaitTermination();
    }

    private static void commandLoop() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter job input (or 'exit'): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                System.exit(0);
            }

            try {
                triggerPythonJob(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void triggerPythonJob(String input) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        PythonTaskServiceGrpc.PythonTaskServiceBlockingStub stub =
                PythonTaskServiceGrpc.newBlockingStub(channel);

        Test.StartLongTaskResponse response = stub.startLongTask(
                Test.StartLongTaskRequest.newBuilder()
                        .setInput(input)
                        .build()
        );

        System.out.println("Python accepted job_id: " + response.getJobId());

        channel.shutdown();
    }
}
