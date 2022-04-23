#!/bin/bash
echo "> build : short-form-server-0.0.1-SNAPSHOT.jar" >> /home/ubuntu/deploy.log

echo "> current running process pid 확인" >> /home/ubuntu/deploy.log
CURRENT_PID=$(pgrep -f short-form-server-0.0.1-SNAPSHOT.jar)

if [ -z $CURRENT_PID ]
then
  echo "> not exist short form server application process - no exit" >> /home/ubuntu/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi
