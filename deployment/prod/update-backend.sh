#!/bin/sh

APP_NAME="megumin"

docker container stop $APP_NAME

docker container rm $APP_NAME

docker image rm ghcr.io/sabercon/$APP_NAME

docker-compose up --detach

echo "Backend Update Success!"