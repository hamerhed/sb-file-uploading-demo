#!/bin/sh

if [ -z "$WAIT_FOR_SERVICE" ]
then
	WAIT_FOR_SERVICE=message-broker:5672
fi

if [ -z "$WAIT_FOR_SERVICE_TIMEOUT" ]
then
	WAIT_FOR_SERVICE_TIMEOUT=0
fi

echo "wait for " $WAIT_FOR_SERVICE " and start application access and management service"

./wait-for-it.sh $WAIT_FOR_SERVICE --timeout=$WAIT_FOR_SERVICE_TIMEOUT -- java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar "${HOME}/sgms-app.jar" "$@"
