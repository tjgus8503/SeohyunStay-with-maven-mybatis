FROM openjdk:17
WORKDIR /app
COPY app.jar /app
COPY application.properties /app
ENV JAVA_OPS=""
RUN test -f "application.properties" && \ export SPRING_CONFIG_LOCATION = "file:/app/application.properties" || true
CMD ["java", "-jar", "app.jar"]
