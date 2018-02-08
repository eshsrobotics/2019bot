#!/usr/bin/env bash

# Uses $JAVA_HOME if it can, so if the script's not working for you, set that
# first.

JAVA_COMMAND="java"
if [[ "$JAVA_HOME" != "" ]]; then
    JAVA_COMMAND="$JAVA_HOME/bin/java"
fi

$JAVA_COMMAND -Djava.library.path="$PWD" -jar ./networktables-input.jar
