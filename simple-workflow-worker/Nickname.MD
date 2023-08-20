# Nickname generation workflow

**Conductor server has to be running on localhost:8080**

## Add workflow definition
POST /api/metadata/workflow  
Request Body: [nickname_workflow](src/main/resources/nickname_workflow.json)

## Build worker
```shell
./gradlew simple-workflow-worker:jar 
```

## Run worker
```shell
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





