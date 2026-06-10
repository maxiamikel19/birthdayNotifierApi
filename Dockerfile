FROM maven:3.8.5-openjdk-17 as builder
WORKDIR /app
COPY . .
RUN mvn dependency:resolve
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder ./app/target/*.jar ./application.jar
EXPOSE 8080
ENV POSTGRES_HOST=localhost
ENTRYPOINT ["java", "-jar", "application.jar"]