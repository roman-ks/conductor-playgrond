## Create Task definitions
Create get_nickname definition:
```bash
curl -X 'POST' \
  'http://localhost:8080/api/metadata/taskdefs' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '[
  {
      "createTime": 1692536596479,
      "createdBy": "",
      "accessPolicy": {},
      "name": "get_nickname",
      "retryCount": 3,
      "timeoutSeconds": 30,
      "inputKeys": [
        "name"
      ],
      "outputKeys": [
        "nickname"
      ],
      "timeoutPolicy": "TIME_OUT_WF",
      "retryLogic": "FIXED",
      "retryDelaySeconds": 60,
      "responseTimeoutSeconds": 30,
      "concurrentExecLimit": 100,
      "inputTemplate": {},
      "rateLimitPerFrequency": 50,
      "rateLimitFrequencyInSeconds": 60,
      "ownerEmail": "example@example.com",
      "pollTimeoutSeconds": 30,
      "backoffScaleFactor": 1
}
]
'
```

## Define a workflow
```bash
curl -X 'POST' \
  'http://localhost:8080/api/metadata/workflow' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "createTime": 1692525531525,
  "updateTime": 1692546933034,
  "accessPolicy": {},
  "name": "test_nickname_batch",
  "description": "Batch create the nicknames from the input names",
  "version": 12,
  "tasks": [
    {
      "name": "split_names",
      "taskReferenceName": "split_names",
      "inputParameters": {
        "names": "${workflow.input.names}"
      },
      "type": "SIMPLE",
      "startDelay": 0,
      "optional": false,
      "asyncComplete": false
    },
    {
      "name": "dynamic_fanout",
      "taskReferenceName": "fanout1",
      "inputParameters": {
        "dynamicTasks": "${split_names.output.dynamicForkTasksParam}",
        "input": "${split_names.output.dynamicForkTasksInputParamName}"
      },
      "type": "FORK_JOIN_DYNAMIC",
      "dynamicForkTasksParam": "dynamicTasks",
      "dynamicForkTasksInputParamName": "input",
      "startDelay": 0,
      "optional": false,
      "asyncComplete": false
    },
    {
      "name": "dynamic_join",
      "taskReferenceName": "join1",
      "inputParameters": {},
      "type": "JOIN",
      "startDelay": 0,
      "optional": false,
      "asyncComplete": false
    },
    {
      "name": "join_nicknames",
      "taskReferenceName": "join_nicknames",
      "inputParameters": {
        "names": "${join1.output}"
      },
      "type": "SIMPLE",
      "startDelay": 0,
      "optional": false,
      "asyncComplete": false
    }
  ],
  "inputParameters": [
    "names"
  ],
  "outputParameters": {},
  "schemaVersion": 2,
  "restartable": true,
  "workflowStatusListenerEnabled": false,
  "ownerEmail": "example@email.com",
  "timeoutPolicy": "ALERT_ONLY",
  "timeoutSeconds": 0,
  "variables": {},
  "inputTemplate": {}
}'
```

## Run the Nickname worker
See [simple-workflow-worker Build and Run Nickname worker](../simple-workflow-worker/Nickname.MD#build-and-run-nickname-worker)

## Build and Run the Fork Join workers
Build:
```bash
./gradlew simple-fork-join-workers:jar 
```
Run:
```bash
java -jar simple-fork-join-workers.jar
```

## Submit input
```bash
curl -X 'POST' \
  'http://localhost:8080/api/workflow/test_nickname_batch?priority=0' \
  -H 'accept: text/plain' \
  -H 'Content-Type: application/json' \
  -d '{
  "names": "pes,patron,dsns"
}'
```

## Get result
```bash
curl -X 'GET' \
  'http://localhost:8080/api/workflow/c0e8468c-e71c-4eaa-ad89-3bff17cc720c?includeTasks=true' \
  -H 'accept: */*'
```
