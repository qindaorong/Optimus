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

SERVER_FOLDER=$PROJECT_BASE
BACK_BASE=$BACK_FOLDER_BASE
CODE_BASE=$OPTIMUS_MAIN
CONFIG_FOLDER=$ENV_CONFIG_FOLDER_BASE

#----------------------------------------backup file -----------------------------------------
if [ ! -d "$BACK_BASE" ]; then
    cd /opt/app
    mkdir $BACK_BASE
fi

if [ ! -d "$BACK_BASE/$SERVER_FOLDER" ]; then
    cd $BACK_BASE	
    mkdir $SERVER_FOLDER
fi

cd $HOME_BASE/$SERVER_FOLDER/webapps/ROOT

DATE='date +%Y-%m-%d'
tar zcvf $BACK_BASE/$SERVER_FOLDER/`$DATE.tar.gz` *

cd $BACK_BASE/$SERVER_FOLDER
rm -rf `find . -name '*.tar.gz' -mtime 10`

echo "$PROJECT_NAME project backup ok!"


#----------------------------------------publish program -----------------------------------------
cd $CODE_BASE/$CODE_FOLDER

rm -rf $HOME_BASE/$SERVER_FOLDER/webapps/ROOT/*

cp -r $WAR_DIR_ADMIN/* $HOME_BASE/$SERVER_FOLDER/webapps/ROOT

cp -r $CONFIG_FOLDER/$SERVER_FOLDER/* $HOME_BASE/$SERVER_FOLDER/webapps/ROOT/WEB-INF/classes

echo "publish $PROJECT_NAME project ok!"
