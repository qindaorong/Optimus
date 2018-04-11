#!/bin/sh

HOME_BASE=$1
BACK_FOLDER_BASE=$2
ENV_CONFIG_FOLDER_BASE=$3
OPTIMUS_MAIN=$4
MAIN_TOMCAT_BASE=$5
PROJECT_BASE=$6
PROJECT_NAME=${7}
CODE_FOLDER=${8}

#------------------------------------------------create backup folder ----------------------------------------------------------
cd /opt/app

if [ ! -d "$BACK_FOLDER_BASE" ]; then
  mkdir $BACK_FOLDER_BASE
fi

if [ ! -d "$BACK_FOLDER_BASE/$PROJECT_BASE" ]; then
  mkdir $BACK_FOLDER_BASE/$PROJECT_BASE
fi

if [ ! -d "$BACK_FOLDER_BASE/apache-tomcat-8.5.20" ]; then
  mkdir $BACK_FOLDER_BASE/apache-tomcat-8.5.20
  cp -r /opt/app/apache-tomcat-8.5.20/. $BACK_FOLDER_BASE/apache-tomcat-8.5.20
fi

echo "backup folder is ready!"

#------------------------------------------------env_config_folder ----------------------------------------------------------
cd /opt/app

if [ ! -d "$ENV_CONFIG_FOLDER_BASE" ]; then
  mkdir $ENV_CONFIG_FOLDER_BASE
fi

if [ ! -d "$ENV_CONFIG_FOLDER_BASE/$PROJECT_BASE" ]; then
  mkdir $ENV_CONFIG_FOLDER_BASE/$PROJECT_BASE
else
  rm -rf $ENV_CONFIG_FOLDER_BASE/$PROJECT_BASE/*
fi

cp -r $OPTIMUS_MAIN/$CODE_FOLDER/env_config/. $ENV_CONFIG_FOLDER_BASE/$PROJECT_BASE

echo "$ENV_CONFIG_FOLDER_BASE/$PROJECT_BASE is ready !"

#------------------------------------------------create tomcat instance folder ----------------------------------------------------------
cd /opt/app

if [ ! -d "$HOME_BASE" ]; then
  mkdir $HOME_BASE
fi


if [ ! -d "$MAIN_TOMCAT_BASE" ]; then
  cd $HOME_BASE
  mkdir $MAIN_TOMCAT_BASE

  cd $MAIN_TOMCAT_BASE
  mkdir bin
  mkdir lib
  mkdir logs

  cp -r $BACK_FOLDER_BASE/apache-tomcat-8.5.20/bin/. $MAIN_TOMCAT_BASE/bin
  cp -r $BACK_FOLDER_BASE/apache-tomcat-8.5.20/lib/. $MAIN_TOMCAT_BASE/lib
  cp -r $BACK_FOLDER_BASE/apache-tomcat-8.5.20/logs/. $MAIN_TOMCAT_BASE/logs

  cp  $BACK_FOLDER_BASE/apache-tomcat-8.5.20/LICENSE $MAIN_TOMCAT_BASE
  cp  $BACK_FOLDER_BASE/apache-tomcat-8.5.20/NOTICE $MAIN_TOMCAT_BASE
  cp  $BACK_FOLDER_BASE/apache-tomcat-8.5.20/RELEASE-NOTES $MAIN_TOMCAT_BASE
  cp  $BACK_FOLDER_BASE/apache-tomcat-8.5.20/RUNNING.txt $MAIN_TOMCAT_BASE
fi

cd $HOME_BASE

if [ ! -d "tomcat_shell" ]; then
  mkdir tomcat_shell
else
  rm -rf $HOME_BASE/tomcat_shell
fi


if [ ! "$(ls -A tomcat_shell)" ]; then
  cp -r $OPTIMUS_MAIN/tomcat_shell/. $HOME_BASE/tomcat_shell
fi

chmod u+x $HOME_BASE/tomcat_shell/*.sh

echo "create tomcat instance is ready!"
#------------------------------------------------Publish tomcat ----------------------------------------------------------
cd $HOME_BASE

if [ ! -d "$HOME_BASE/$PROJECT_BASE" ]; then
    cd $HOME_BASE
    mkdir $HOME_BASE/$PROJECT_BASE
else
    rm -rf $HOME_BASE/$PROJECT_BASE/*
fi

cd $HOME_BASE/$PROJECT_BASE

mkdir conf
mkdir logs
mkdir temp
mkdir webapps
mkdir work

cp -r $BACK_FOLDER_BASE/apache-tomcat-8.5.20/conf/. $HOME_BASE/$PROJECT_BASE/conf
cp -r $BACK_FOLDER_BASE/apache-tomcat-8.5.20/logs/. $HOME_BASE/$PROJECT_BASE/logs
cp -r $BACK_FOLDER_BASE/apache-tomcat-8.5.20/temp/. $HOME_BASE/$PROJECT_BASE/temp
cp -r $BACK_FOLDER_BASE/apache-tomcat-8.5.20/webapps/. $HOME_BASE/$PROJECT_BASE/webapps
cp -r $BACK_FOLDER_BASE/apache-tomcat-8.5.20/work/. $HOME_BASE/$PROJECT_BASE/work

rm -rf $HOME_BASE/$PROJECT_BASE/webapps/ROOT/*

echo "[env.sh] Done."