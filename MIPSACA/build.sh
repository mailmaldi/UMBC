#! /bin/sh
chmod +x gradlew
./gradlew clean build jar
cp build/libs/MIPSACA-1.0.jar .