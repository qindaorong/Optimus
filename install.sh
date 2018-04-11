#!/bin/sh
OPTIMUS_MAIN=$1

cd  $OPTIMUS_MAIN
mvn clean install -Pprod -Dmaven.test.skip=true

cd $OPTIMUS_MAIN/publishTool
mvn clean package

path=`pwd`
echo "$path"

if [ ! -f "$path/target/publishtool-1.0-RELEASE.jar" ]; then
  echo "publishTool package failed"
  exit 0
fi

cp  ./target/publishtool-1.0-RELEASE.jar /opt/app

cd ../

chmod u+x  eureka/package.sh
chmod u+x  websocketCenter/websocketCore/package.sh
chmod u+x  messageCenterService/core/package.sh
