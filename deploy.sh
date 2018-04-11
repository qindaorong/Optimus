#!/bin/sh
XML_PATH=$1

HOME_BASE=($(grep -oP '(?<=HOME_BASE>)[^<]+' "$XML_PATH"))
BACK_FOLDER_BASE=($(grep -oP '(?<=BACK_FOLDER_BASE>)[^<]+' "$XML_PATH"))
ENV_CONFIG_FOLDER_BASE=($(grep -oP '(?<=ENV_CONFIG_FOLDER_BASE>)[^<]+' "$XML_PATH"))
OPTIMUS_MAIN=($(grep -oP '(?<=OPTIMUS_MAIN>)[^<]+' "$XML_PATH"))
MAIN_TOMCAT_BASE=($(grep -oP '(?<=MAIN_TOMCAT_BASE>)[^<]+' "$XML_PATH"))
PROJECT_NAME=($(grep -oP '(?<=PROJECT_NAME>)[^<]+' "$XML_PATH"))
CODE_FOLDER=($(grep -oP '(?<=CODE_FOLDER>)[^<]+' "$XML_PATH"))
PROJECT_BASE=($(grep -oP '(?<=PROJECT_BASE>)[^<]+' "$XML_PATH"))
WAR_DIR_ADMIN=($(grep -oP '(?<=WAR_DIR_ADMIN>)[^<]+' "$XML_PATH"))

cd $OPTIMUS_MAIN
chmod u+x *.sh

if [ ! -n $HOME_BASE ]; then
  echo "HOME_BASE IS NULL!"
  exit 0
fi
if [ ! -n $BACK_FOLDER_BASE ]; then
  echo "BACK_FOLDER_BASE IS NULL!"
  exit 0
fi
if [ ! -n $ENV_CONFIG_FOLDER_BASE ]; then
  echo "ENV_CONFIG_FOLDER_BASE IS NULL!"
  exit 0
fi
if [ ! -n $OPTIMUS_MAIN ]; then
  echo "OPTIMUS_MAIN IS NULL!"
  exit 0
fi
if [ ! -n $MAIN_TOMCAT_BASE ]; then
  echo "MAIN_TOMCAT_BASE IS NULL!"
  exit 0
fi
if [ ! -n $PROJECT_NAME ]; then
  echo "PROJECT_NAME IS NULL!"
  exit 0
fi
if [ ! -n $CODE_FOLDER ]; then
  echo "CODE_FOLDER IS NULL!"
  exit 0
fi
if [ ! -n $PROJECT_BASE ]; then
  echo "PROJECT_BASE IS NULL!"
  exit 0
fi
if [ ! -n $WAR_DIR_ADMIN ]; then
  echo "WAR_DIR_ADMIN IS NULL!"
  exit 0
fi

echo "$XML_PATH values all are not NULL!"

#-----------------------------------------------environment check---------------------------------------------------------
sh ./env.sh "$HOME_BASE" "$BACK_FOLDER_BASE" "$ENV_CONFIG_FOLDER_BASE" "$OPTIMUS_MAIN" "$MAIN_TOMCAT_BASE"  "$PROJECT_BASE"  "$PROJECT_NAME" "$CODE_FOLDER"
