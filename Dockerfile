FROM java:8
VOLUME /tmp
ADD "/build/libs/familytalk-0.0.1.jar" app.jar
ENV JAVA_OPTS=""
EXPOSE 5000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","app.jar"]