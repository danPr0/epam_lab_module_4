#!/bin/sh
#used to tell to execute the file using the Bourne shell, or a compatible shell, assumed to be in the /bin directory
exec java -jar ${JAVA_OPTS} /app.jar ${@}
