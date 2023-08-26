# SWITCH task example
Execute different tasks based to `type` field value.


## Define a workflow
```bash
curl -X 'POST' \
  'http://localhost:8080/api/metadata/workflow' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "createTime": 1693042308353,
  "accessPolicy": {},
  "name": "test_nickname_decision",
  "description": "Create a nickname based on input name and type",
  "version": 1,
  "tasks": [
    {
      "name": "nickname_type_check",
      "taskReferenceName": "nickname_type_check",
      "inputParameters": {
        "switchCaseValue": "${workflow.input.type}"
      },
      "type": "SWITCH",
      "decisionCases": {
        "cool": [
          {
            "name": "get_cool_nickname",
            "taskReferenceName": "get_cool_nickname",
            "inputParameters": {
              "name": "${workflow.input.name}"
            },
            "type": "SIMPLE",
            "startDelay": 0,
            "optional": false,
            "asyncComplete": false
          }
        ]
      },
      "defaultCase": [
        {
          "name": "get_nickname",
          "taskReferenceName": "get_nickname",
          "inputParameters": {
            "name": "${workflow.input.name}"
          },
          "type": "SIMPLE",
          "startDelay": 0,
          "optional": false,
          "asyncComplete": false
        }
      ],
      "startDelay": 0,
      "optional": false,
      "asyncComplete": false,
      "evaluatorType": "value-param",
      "expression": "switchCaseValue"
    }
  ],
  "inputParameters": [
    "name"
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

## Build and Run the Fork Join workers
Build:
```bash
./gradlew simple-decision-workflow:jar 
```
Run:
```bash
cd simple-decision-workflow/build/libs
java -jar simple-decision-workflow.jar
```

## Trigger
Get normal nickname:
```bash
curl -X 'POST' \
'http://localhost:8080/api/workflow/test_nickname_decision?priority=0' \
-H 'accept: text/plain' \
-H 'Content-Type: application/json' \
-d '{  "name": "patron"  }'
```

Get cool nickname:
```bash
curl -X 'POST' \
'http://localhost:8080/api/workflow/test_nickname_decision?priority=0' \
-H 'accept: text/plain' \
-H 'Content-Type: application/json' \
-d '{  "name": "patron", "type": "cool"  }'
```