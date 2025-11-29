import time
import numpy as np

def run_inference(payload):
    print(f"printed payload from Python: {payload}")
    time.sleep(5)
    return {f"prediction": 42}


def run_inference_pip_package(payload):
    return float(np.sum([1, 2, 3]))