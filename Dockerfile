FROM clojure:openjdk-11-tools-deps AS builder

WORKDIR /app

COPY . /app

RUN clojure -T:build uber

FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=builder /app/target/church-api-standalone.jar /app/church-api.jar

EXPOSE 8080

ENV DATABASE_URL="jdbc:postgresql://db:5432/church_db?user=postgres&password=postgres"
ENV PORT=8080

CMD ["java", "-jar", "church-api.jar"]
