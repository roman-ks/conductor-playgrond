#!/bin/bash

export CONDUCTOR_VER=3.13.8
export REPO_URL=https://repo1.maven.org/maven2/com/netflix/conductor/conductor-community-server

# output name without community mention to  simplify switching between community/regular servers
curl $REPO_URL/$CONDUCTOR_VER/conductor-community-server-$CONDUCTOR_VER-boot.jar \
--output conductor-server-$CONDUCTOR_VER-boot.jar

