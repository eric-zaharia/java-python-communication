import os
import redis

r = redis.Redis(
    host=os.getenv("REDIS_HOST", "localhost"),
    port=int(os.getenv("REDIS_PORT", 6379)),
    decode_responses=True,
)

def publish_result(agent_id: str, job_id: str, result: dict):
    stream = f"results:{agent_id}"
    r.xadd(stream, {
        "job_id": job_id,
        "result": str(result),
    })
