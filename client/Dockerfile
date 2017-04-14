FROM java

# compile client
# par besoin yolo
# RUN mvn -q -DskipTests clean package assembly:single

# copy executable dans docker image
COPY ./target/tcf-client-1.0-SNAPSHOT.jar tcf-client.jar

# expose le port sur lequel va tourner le client
EXPOSE 8080

# run le client
# normalement EXPOSE ne va pas etre utile
ENTRYPOINT ["java", "-cp", "tcf-client.jar", "Main"]

# CMD ["localhost", "8080"]
