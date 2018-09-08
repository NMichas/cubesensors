#!/usr/bin/env bash

# Advances the last number of the given version string by one.
function advance_version () {
    local v=$1
    # Get the last number. First remove any suffixes (such as '-SNAPSHOT').
    local cleaned=`echo $v | sed -e 's/[^0-9][^0-9]*$//'`
    local last_num=`echo $cleaned | sed -e 's/[0-9]*\.//g'`
    local next_num=$(($last_num+1))
    # Finally replace the last number in version string with the new one.
    echo $v | sed -e "s/[0-9][0-9]*\([^0-9]*\)$/$next_num/"
}

CURRENT_VERSION=`xmllint --xpath "//*[local-name()='project']/*[local-name()='version']/text()" pom.xml`
NEW_VERSION=`advance_version $CURRENT_VERSION`

mvn versions:set -DnewVersion=$NEW_VERSION -DgenerateBackupPoms=false
mvn clean package
docker login
docker build -t nassos/cubesensors:$NEW_VERSION .
docker push nassos/cubesensors:$NEW_VERSION