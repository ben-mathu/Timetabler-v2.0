#!/bin/bash

mvn package
rm -v /opt/tomcat/latest/webapps/timetabler.war
cp -v target/timetabler.war /opt/tomcat/latest/webapps/
tail -fv /opt/tomcat/latest/logs/catalina.out
