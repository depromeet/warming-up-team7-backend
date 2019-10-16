FROM java:8
VOLUME /tmp
ADD /build/libs/familytalk-0.0.1.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]