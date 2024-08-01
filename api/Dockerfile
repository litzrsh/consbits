FROM openjdk:17
ARG SPRING_PROFILE="dev"
ENV SPRING_PROFILE=$SPRING_PROFILE
COPY target/app.jar /app/app.jar
WORKDIR /app
EXPOSE 8080
VOLUME ["/app/.ssh", "/app/upload"]
CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILE}", "app.jar"]
