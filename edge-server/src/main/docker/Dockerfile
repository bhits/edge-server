FROM java:8
VOLUME /java
ARG finalName
ADD $finalName /edge-server.jar
ENTRYPOINT ["java","-Djavax.net.ssl.trustStore=/java/keystore/c2s.keystore","-Djavax.net.ssl.trustStorePassword=changeit","-jar","/edge-server.jar","--spring.profiles.active=ssl,eureka-ssl,docker"]