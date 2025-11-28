import time
from concurrent.futures import ThreadPoolExecutor
from redis_client import publish_result

executor = ThreadPoolExecutor(max_workers=4)

def submit_job(agent_id: str, job_id: str, payload: dict):
    executor.submit(_run, agent_id, job_id, payload)

def _run(agent_id, job_id, payload):
    result = run_inference(payload)
    publish_result(agent_id, job_id, result)

def run_inference(payload):
    time.sleep(5)
    return {f"prediction": 42}
