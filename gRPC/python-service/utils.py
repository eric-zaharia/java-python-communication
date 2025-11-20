import time
import grpc
import test_pb2
import test_pb2_grpc


def long_computation(task_input):
    print(f"[Python] Working on task input: {task_input}")
    time.sleep(5)
    return f"Processed({task_input})"


def notify_java(job_id, result):
    print(f"[Python] Notifying Java callback service...")

    channel = grpc.insecure_channel("localhost:50052")
    stub = test_pb2_grpc.JavaCallbackServiceStub(channel)

    req = test_pb2.TaskResultRequest(
        job_id=job_id,
        result=result
    )

    response = stub.TaskFinished(req)
    print(f"[Python] Java acknowledged result: ok={response.ok}")


def run_task(job_id, input_data):
    print(f"[Python] Background worker started for job_id {job_id}")

    result = long_computation(input_data)

    print(f"[Python] Background worker finished job_id {job_id}, result={result}")
    notify_java(job_id, result)