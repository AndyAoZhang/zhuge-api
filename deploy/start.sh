#!/bin/bash
echo "boot start"
# start.sh ${APP_PROFILE} ${APP_PORT} ${DEPLOY_TAG} ${LOG_DIR}

APP_PROFILE=$1
APP_PORT=$2
DEPLOY_TAG=$3
LOG_DIR=$4


function __help(){
    echo "usage: $0 <profile> <port> <deploy_tag> <LOG_DIR>"
    echo "e.g. $0 test 8080 v20220707 ../logs"
    exit
}

if [[ $# != 4 ]]; then
    __help
fi


java -Dserver.port=${APP_PORT} -Dlogging.path=${LOG_DIR} -jar zhuge-api-0.0.1-SNAPSHOT.jar

echo "done"