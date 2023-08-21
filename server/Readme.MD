# Run customized Conductor server

1. Install
```shell
./install-without-orkes.sh
```
2. Find or create a properties file with the configs. [Example](application.properties)
3. Run the server with configs
```shell
java -jar conductor-server-*-boot.jar 
```

## Adding SQS Event handler
Below properties are required in config to enable SQS Event handler.
```properties
conductor.event-queues.sqs.enabled=true
conductor.event-queues.sqs.authorized-accounts=<account number>
```
Also make sure AWS credentials are available to load by [Default credentials provider chain](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials-chain.html)

Event handler below uses simple-workflow-worker [workflow](../simple-workflow-worker/src/main/resources/nickname_workflow.json)
```bash
curl -X 'POST' \
  'http://localhost:8080/api/event' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "poll_nicknames_sqs",
  "event": "sqs:conductor-nicknames-sqs",
  "active": true,
  "actions": [
    {
      "action": "start_workflow",
      "start_workflow": {
        "name": "test_nickname",
        "input": {
          "name": "${name}"
        }
      }
    }
  ]
}'
```

The message is inputted directly to the workflow. For message in SQS like below
```json
{
  "name": "patron"
}
```
The `name` field will be extracted and used and in `name` field in workflow input: 
```
"input": {
  "name": "${name}"
}
```