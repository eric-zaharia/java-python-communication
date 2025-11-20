import grpc
from concurrent import futures
import threading
import uuid
from utils import run_task

import test_pb2
import test_pb2_grpc


class PythonTaskService(test_pb2_grpc.PythonTaskServiceServicer):
    def StartLongTask(self, request, context):
        job_id = str(uuid.uuid4())
        print(f"[Python] Received StartLongTask: job_id={job_id}, input={request.input}")

        thread = threading.Thread(target=run_task, args=(job_id, request.input))
        thread.daemon = True
        thread.start()

        return test_pb2.StartLongTaskResponse(job_id=job_id)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    test_pb2_grpc.add_PythonTaskServiceServicer_to_server(PythonTaskService(), server)

    server.add_insecure_port("[::]:50051")
    server.start()
    print("[Python] PythonTaskService running on port 50051")
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
