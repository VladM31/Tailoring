FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /build
COPY pom.xml ./
RUN mvn -X dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /build/target/Tailoring*jar Tailoring.jar
EXPOSE 8080
CMD ["java", "-jar", "Tailoring.jar"]