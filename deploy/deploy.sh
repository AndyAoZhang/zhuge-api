#!/bin/bash

# 项目启动脚本

function __help(){
    echo "usage: $0 <app_name> <profile> <port> <deploy_tag> <dist_dir>"
    echo "e.g. $0 nvwa-api test 8080 v20220707 ./dist"
    exit
}

if [[ $# != 5 ]]; then
    __help
fi

# args
DEPLOY_SCRIPT=$0

APP_NAME=$1

APP_PROFILE=$2

APP_PORT=$3

DEPLOY_TAG=$4

TARGET_DIR=$5

# tools
SUPERVISORCTL=supervisorctl

# options

APP_PROFILES="test production"

# dirs
HOME_DIR=~
APPS_HOME_DIR=${HOME_DIR}/apps
LOGS_HOME_DIR=${HOME_DIR}/logs
DEPLOY_DIR=$(dirname "${DEPLOY_SCRIPT}")
DEPLOY_FILE=$(basename "${DEPLOY_SCRIPT}")
APP=${APP_NAME}-${APP_PROFILE}-${APP_PORT}
LOG_DIR=${LOGS_HOME_DIR}/${APP}
APP_DIR=${APPS_HOME_DIR}/${APP}
DIST_DIR=${APPS_HOME_DIR}/${APP}/dist

NEW_CONF=${APP_DIR}/supervisor.conf

function __checkProfile(){
   for PROFILE_OPTION in ${APP_PROFILES}
   do
      if [ "${PROFILE_OPTION}" == "${APP_PROFILE}" ]; then
         return
      fi
   done
   echo "No profile found with: ${APP_PROFILE} in ${APP_PROFILES}"
   __help
}

__checkProfile

function __start(){
    echo 'begin __start()!'
    ${SUPERVISORCTL} start ${APP}

    k=1
    for k in $(seq 1 20)
    do
        sleep 1
        PID=`ps -ef | grep "${APP}" | grep -v grep | awk '{print $2}'`
        if [ "${PID}" != "" ]; then
            break
        fi
        echo ${k}
        if [ ${k} -eq 20 ]
        then
            echo 'process start time more than 20s, so abort'
            exit 11
        fi
    done

    echo 'app is started. wait 10 seconds'
    sleep 10
    PID=`ps -ef | grep "${APP}" | grep -v grep | awk '{print $2}'`
    if [ "${PID}" == "" ]; then
        echo 'cannot found process '
        exit 12
    fi
    num=`ps -ef | grep "${APP}" | grep -v grep | wc -l`
    if [ ${num} -gt 1 ]; then
        echo "have more than one ${APP} instances!!!"
        exit 13
    fi
    echo 'start successfully !!!'
}

function __stop(){
    echo 'begin __stop()!'

    num=`${SUPERVISORCTL} status | grep ${APP} | grep RUNNING | wc -l`
    if [ ${num} -gt 0 ]; then
        echo "stop supervisor process: ${APP}"
        ${SUPERVISORCTL} stop ${APP}
    else
        echo "supervisor process is not running"
    fi

    PID=`ps -ef | grep "${APP_NAME}" | grep -v "${DEPLOY_FILE}" | grep -v grep | awk '{print $2}'`
    ps -ef | grep "${APP_NAME}" | grep -v grep
    if [ "${PID}" != "" ]; then
        echo "kill ${APP_NAME}"
        kill ${PID}
        sleep 1
    else
        echo "no process. return"
        return
    fi

    k=1
    for k in $(seq 1 10)
    do
        PID=`ps -ef | grep "${APP_NAME}" | grep -v "${DEPLOY_FILE}" | grep -v grep | awk '{print $2}'`
        if [ "${PID}" = "" ]; then
            break
        fi
        if [ ${k} -eq 10 ]
        then
            echo 'cannot shutdown normally, so use kill -9'
            kill -9 ${PID}
            break
        fi
        echo "wait ${k} seconds"
        sleep ${k}
    done
}

function __deploy(){
    echo "0. stop service"
    __stop

    echo "1. make LOG_DIR at ${LOG_DIR}"
    mkdir -p ${LOG_DIR}

    echo "2. rm APP_DIR at ${APP_DIR}"
    rm -rf ${APP_DIR}

    echo "3. make APP_DIR at ${APP_DIR}"
    mkdir -p ${APP_DIR}

    echo '4. copy start.sh '
    cp  ${DEPLOY_DIR}/start.sh ${APP_DIR}/
    chmod +x ${APP_DIR}/start.sh

    echo "5.set up SUPERVISORCTL config ${APP} at ${NEW_CONF}"
    echo "[program:${APP}]" > "${NEW_CONF}"
    echo "command=${APP_DIR}/start.sh ${APP_PROFILE} ${APP_PORT} ${DEPLOY_TAG}  ${DIST_DIR}  ${LOG_DIR}" >> ${NEW_CONF}

    echo 'config content: '
    cat ${NEW_CONF}

    echo "7. make DIST_DIR at ${DIST_DIR}"
    mkdir -p ${DIST_DIR}

    echo '8. copy new app'
    cp -r ${TARGET_DIR}/* ${DIST_DIR}/

    echo "9. update supervisorctl"
    ${SUPERVISORCTL} update

    echo '10. start...'
    __start
}

__deploy
