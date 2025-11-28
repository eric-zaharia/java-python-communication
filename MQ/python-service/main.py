from typing import Dict, Any

from fastapi import FastAPI
from pydantic import BaseModel
from worker import submit_job

app = FastAPI()

class InferenceRequest(BaseModel):
    agent_id: str
    job_id: str
    payload: Dict[str, Any]


@app.post("/infer")
def infer(req: InferenceRequest):
    submit_job(
        agent_id=req.agent_id,
        job_id=req.job_id,
        payload=req.payload,
    )
    return {"status": "accepted", "job_id": req.job_id}
