#!/bin/sh
PROJECT_PATH=$1
JAR_BASE=$2

cd $JAR_BASE
java -jar publishtool-1.0-RELEASE.jar $1
