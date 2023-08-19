# Nickname generation workflow

**Conductor server has to be running on localhost:8080**

## Setup
POST /api/metadata/workflow  
Request Body: [nickname_workflow](src/main/resources/nickname_workflow.json)

## Submit input
POST /api/workflow/  
name: test_nickname  
Request Body:  
```json
{
  "name": "patron"
}
```
Get execution id(workflow id) from response body

## Get result
GET /api/workflow/{workflowId}

## Build worker
```shell
./gradlew simple-workflow-worker:jar 
```

## Run worker
```shell
java -jar simple-workflow-worker.jar
```





