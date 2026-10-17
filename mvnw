#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper script for UNIX
# ----------------------------------------------------------------------------

set -e

PRG="$0"
while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done
SAVED="`pwd`"
cd "`dirname \"$PRG\"`" >/dev/null
APP_HOME="`pwd -P`"
cd "$SAVED" >/dev/null

MAVEN_WRAPPER_JAR="$APP_HOME/.mvn/wrapper/maven-wrapper.jar"

if [ ! -e "$MAVEN_WRAPPER_JAR" ]; then
    echo "Downloading maven-wrapper.jar..."
    mkdir -p "$APP_HOME/.mvn/wrapper"
    curl -o "$MAVEN_WRAPPER_JAR" https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar || \
    wget -O "$MAVEN_WRAPPER_JAR" https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar
fi

exec java $MAVEN_OPTS -jar "$MAVEN_WRAPPER_JAR" "$@"
