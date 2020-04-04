#!/bin/bash

#set -ex
set -e

CMDS=($(echo $@))
length=${#CMDS[@]}

if [ $length -ne 3 ]; then
    echo "usage: ./docker-build.sh <build|push> -t <tag-name>"
    exit;
fi

CMD="${CMDS[0]}"
TAG="latest"

if [ ${CMDS[1]} == "-t" ]; then
    TAG=${CMDS[2]}
else
    echo "wrong tag flag. Should be -t"
    exit;
fi


PROJECT_NAME=agrobank
PARENT_DIR=$(basename "${PWD%/*}")
CURRENT_DIR="${PWD##*/}"
IMAGE_NAME="$PROJECT_NAME/sgms-rfid-connector"
REGISTRY="registry.apps.man.poznan.pl"

case "$CMD" in
"build")
    docker build -t ${REGISTRY}/${IMAGE_NAME}:${TAG} .
    ;;
"push")
    docker build -t ${REGISTRY}/${IMAGE_NAME}:${TAG} .
    docker push ${REGISTRY}/${IMAGE_NAME}:${TAG}
    ;;
esac
