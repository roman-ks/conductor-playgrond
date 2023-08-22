# Nickname generation workflow

**Conductor server has to be running on localhost:8080**

## Add workflow definition
POST /api/metadata/workflow  
Request Body: [nickname_workflow](src/main/resources/nickname_workflow.json)

## Build and Run Nickname worker
Build:
```bash
./gradlew simple-workflow-worker:jar 
```
Run:
```bash
java -jar simple-workflow-worker.jar
```

## Submit input
POST /api/workflow/  
name: test_nickname  
Request Body:  
```json
{
"name": "patron"
}
```

Curl:
```bash
curl -X 'POST' \
'http://localhost:8080/api/workflow/test_nickname?priority=0' \
-H 'accept: text/plain' \
-H 'Content-Type: application/json' \
-d '{  "name": "patron"  }'
```
Get execution id(workflow id) from response body

## Get result
GET /api/workflow/{workflowId}

Curl:
```bash
curl -X 'GET' \
'http://localhost:8080/api/workflow/0b007528-86ce-4fa6-b1d9-565c7f0706c3?includeTasks=true' \
-H 'accept: */*'
```

# Nickname generation(with sleeping workers)
Workers get `sleepTime` field from the request and sleep that amount in millis before returning a nickname.  
Demonstrates workers executing concurrently.

## Add workflow
POST /api/metadata/workflow  
Request Body: [nickname_workflow_with_sleep](src/main/resources/nickname_workflow_with_sleep.json)

## Build and Run Nickname worker
Build:
```bash
./gradlew simple-workflow-worker:jar 
```
To run the sleeping Main class it needs to be specified explicitly, since the main class of the executable jar is used for non-sleeping Main.
Run:
```bash
java -cp simple-workflow-worker.jar -Dworkers.count=2 com.mashkario.conductor.sleeping.Main
```

## Submit input
Submit two ore more inputs to trigger multiple workers. Same input is duplicated is fine.
`sleepTime` has to have value in millis.

```bash
curl -X 'POST' \
'http://localhost:8080/api/workflow/test_nickname_sleep?priority=0' \
-H 'accept: text/plain' \
-H 'Content-Type: application/json' \
-d '{  "name": "patron", "sleepTime":"5000"  }'
```



