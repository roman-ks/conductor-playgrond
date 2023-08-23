# Configure and run Conductor server

Currently, Conductor server exists in two repos:
1. https://github.com/Netflix/conductor/tree/main/server
2. https://github.com/Netflix/conductor-community/tree/main/community-server

Community-server includes more features at the moment besides modules from the ordinary server.
Furthermore Community-server doesn't include io.orkes dependencies which are included ordinary server and have custom license.

## Install server
### Precompiled
Easiest way to get Conductor server is to download compiled jar from Maven repo:
```bash
export CONDUCTOR_VER=3.13.8
export REPO_URL=https://repo1.maven.org/maven2/com/netflix/conductor/conductor-community-server

# output name without community mention to  simplify switching between community/regular servers
curl $REPO_URL/$CONDUCTOR_VER/conductor-community-server-$CONDUCTOR_VER-boot.jar \
--output conductor-server-$CONDUCTOR_VER-boot.jar
```

### Customly complied
Server can be build from source. 
Useful to decrease jar size, since precompiled jar includes many modules which might not be needed.
1. Install
```bash
./install-without-orkes.sh
```

## Configure server
Find or create a properties file with the configs. [Example](application.properties)
See below for more configs.

## Run server
```bash
java -jar conductor-server-*-boot.jar 
```

## Configuration
### Adding SQS Event handler
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