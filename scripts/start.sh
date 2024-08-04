#!/usr/bin/env bash

set -x

PROJECT_ROOT="/home/ubuntu/springapp"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 현재 실행 중인 애플리케이션 종료
CURRENT_PID=$(pgrep -f $JAR_FILE)
if [ -n "$CURRENT_PID" ]; then
    echo "$TIME_NOW > 실행중인 $CURRENT_PID 애플리케이션 종료" >> $DEPLOY_LOG
    kill -15 $CURRENT_PID
fi

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

# 새 프로세스 ID 확인
NEW_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $NEW_PID 입니다." >> $DEPLOY_LOG

# 프로세스 실행 확인
sleep 10
if ! pgrep -f $JAR_FILE > /dev/null; then
    echo "$TIME_NOW > 애플리케이션 실행 실패. 로그를 확인하세요." >> $DEPLOY_LOG
    exit 1
fi

echo "$TIME_NOW > 애플리케이션 정상 실행" >> $DEPLOY_LOG
