#!/bin/bash

chmod +x "$1/gradlew"
#./gradlew build
"$1/gradlew" -b "$1/build.gradle" unpack