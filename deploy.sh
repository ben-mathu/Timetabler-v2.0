#!/bin/bash

mvn package
rm -v /opt/tomcat/latest/webapps/Timetabler-v2.0-0.0.1-SNAPSHOT.war
cp -v target/Timetabler-v2.0-0.0.1-SNAPSHOT.war /opt/tomcat/latest/webapps/
