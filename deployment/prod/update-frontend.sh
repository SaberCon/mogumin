#!/bin/sh

APP_NAME="megumin"

wget https://nightly.link/SaberCon/$APP_NAME-web/workflows/node.js/main/$APP_NAME.zip

unzip -d html $APP_NAME.zip

rm -f $APP_NAME.zip

rm -rf nginx/html/

mv html nginx/

docker restart nginx

echo "Frontend Update Success!"
