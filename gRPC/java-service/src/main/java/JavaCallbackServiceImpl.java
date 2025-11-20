import io.grpc.stub.StreamObserver;
import test.JavaCallbackServiceGrpc;
import test.Test;

public class JavaCallbackServiceImpl extends JavaCallbackServiceGrpc.JavaCallbackServiceImplBase {
    @Override
    public void taskFinished(Test.TaskResultRequest request, StreamObserver<Test.TaskResultResponse> responseObserver) {

        System.out.println("=== Java received async result from Python ===");
        System.out.println("Job ID : " + request.getJobId());
        System.out.println("Result : " + request.getResult());
        System.out.println("==============================================");

        Test.TaskResultResponse reply = Test.TaskResultResponse.newBuilder()
                .setOk(true)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
