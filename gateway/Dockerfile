FROM eclipse-temurin:21.0.4_7-jre
LABEL authors="yc"
COPY target/gateway-1.0-SNAPSHOT.jar gateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gateway.jar"]
