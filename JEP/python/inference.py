import time

def run_inference(payload):
    print(f"printed payload from Python: {payload}")
    time.sleep(5)
    return {f"prediction": 42}