1. First of all, we need to start the Redis container, using Docker Compose:
    > docker compose up -d

2. Create venv and start FastApi service (in python-service dir):
    > python3 -m venv .venv
    > source .venv/bin/activate
    > pip install -r requirements.txt
    > uvicorn main:app --host 0.0.0.0 --port 8000

    The server now accepts HTTP connections

3. To start Java service (in java-service dir):
    > mvn clean package
    > java -cp target/java-service-1.0-SNAPSHOT.jar agent.Main service-A

    (optionally create other services, another terminal)
    > java -cp target/java-service-1.0-SNAPSHOT.jar agent.Main service-B

    Then you can type job_ids which Python will execute
