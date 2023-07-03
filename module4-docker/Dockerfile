FROM openjdk:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=module4-fat.jar
ARG RUN_FILE=appRun.sh
ENV JAVA_OPTS=""
COPY ${JAR_FILE} app.jar
COPY ${RUN_FILE} run.sh
#COPY module4-0.0.1-SNAPSHOT.jar app.jar
RUN chmod +x /run.sh
ENTRYPOINT ["/run.sh"]
#ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]
#ENTRYPOINT ["java", "${JAVA_OPTS}", "-jar", "/app.jar"]
#CMD ["java", "-jar", "/app.jar"]
#sh,-c,java ${JAVA_OPTS} -jar /app.jar
