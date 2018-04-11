#!/bin/sh
OPTIMUS_MAIN=($(grep -oP '(?<=OPTIMUS_MAIN>)[^<]+' "env.xml"))
JAR_BASE=($(grep -oP '(?<=JAR_BASE>)[^<]+' "env.xml"))
JOB=$1

ENV_PATH=$OPTIMUS_MAIN/kafkaConsumerService/kafkaConsumerServer/env.xml
PROJECT_SERVER_PATH=$OPTIMUS_MAIN/kafkaConsumerService/kafkaConsumerServer/project_server.xml


chmod u+x  *.sh
#install optimus
install(){
  sh ../../install.sh "$OPTIMUS_MAIN"
}
#init env folder
env(){
  sh ../../deploy.sh "$ENV_PATH"
}
#publish project
publish(){
  sh ../../distribution.sh "$ENV_PATH"
}
#modify file
modifyFile(){
  if [ ! -f "$JAR_BASE/publishtool-1.0-RELEASE.jar" ]; then
      echo "jar not found!"
      exit 0
  fi
  echo "jar found!"

  sh ../../modifyFile.sh "$PROJECT_SERVER_PATH" "$JAR_BASE"
}

#doing case job
case "$JOB" in
"install")
  install
  exit 0
;;
"env")
  env
  exit 0
;;
"publish")
  publish
  exit 0
;;
"modifyFile")
  modifyFile
  exit 0
;;
*)
  echo "error command ,command is [install]、[env]、[publish]、[modifyFile] ,please use one of them! "
  exit 0
;;
esac