#!/bin/bash
mv /tmp/module4-thin.war /usr/local/tomcat/webapps/ROOT.war
echo -e "export JAVA_OPTS=\"-Dspring.profiles.active=prod\"" > /usr/local/tomcat/bin/setenv.sh
/usr/local/tomcat/bin/catalina.sh start
