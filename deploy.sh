#!/bin/bash

mvn clean install
rm -v /opt/tomcat/latest/webapps/timetabler-1.0.0.war
cp -v target/timetabler-1.0.0.war /opt/tomcat/latest/webapps/
tail -fv /opt/tomcat/latest/logs/catalina.out
