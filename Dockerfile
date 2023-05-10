FROM openjdk:17
COPY "./target/dzkprueba-0.0.1-SNAPSHOT.war" "zkpres.war"
EXPOSE 8080
ENTRYPOINT ["java","-jar","zkpres.war"]