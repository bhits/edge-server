# Edge Server

The Edge Server acts as a gatekeeper to the outside world. It keeps unauthorized external requests from passing through. It uses *[Spring Cloud Zuul](https://spring.io/guides/gs/routing-and-filtering/)* as a routing framework, which serves as an entry point to the Consent2Share (C2S) microservices landscape. Zuul uses *[Spring Cloud Ribbon](https://spring.io/guides/gs/client-side-load-balancing/)* to lookup available services, and routes the external request to an appropriate service instance, facilitating Dynamic Routing and Load Balancing.

## Build

### Prerequisites

+ [Oracle Java JDK 8 with Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
+ [Docker Engine](https://docs.docker.com/engine/installation/) (for building a Docker image from the project)

### Commands

This is a Maven project and requires [Apache Maven](https://maven.apache.org/) 3.3.3 or greater to build it. It is recommended to use the *Maven Wrapper* scripts provided with this project. *Maven Wrapper* requires an internet connection to download Maven and project dependencies for the very first build.

To build the project, navigate to the folder (./edge-server/edge-server) which contains `pom.xml` file using the terminal/command line.

+ To build a JAR:
    + For Windows, run `mvnw.cmd clean install`
    + For *nix systems, run `mvnw clean install`
+ To build a Docker Image (this will create an image with `bhitsdev/edge-server:latest` tag):
    + For Windows, run `mvnw.cmd clean package docker:build`
    + For *nix systems, run `mvnw clean package docker:build`

## Run

### Commands

This is a [Spring Boot](https://projects.spring.io/spring-boot/) project and it serves via an embedded Tomcat instance. Therefore, there is no need for a separate application server to run this service.
+ Run as a JAR file: `java -jar edge-server-x.x.x-SNAPSHOT.jar <additional program arguments>`
+ Run as a Docker Container: `docker run -d bhitsdev/edge-server:latest <additional program arguments>`

*NOTE: In order for this API to fully function as a microservice in the Consent2Share application, it is required to setup the dependency microservices and the support level infrastructure. Please refer to the Consent2Share Deployment Guide in the corresponding Consent2Share release (see [Consent2Share Releases Page](https://github.com/bhits-dev/consent2share/releases)) for instructions to setup the Consent2Share infrastructure.*

## Configure

This API utilizes [`Configuration Server`](https://github.com/bhits-dev/config-server) which is based on [Spring Cloud Config](https://github.com/spring-cloud/spring-cloud-config) to manage externalized configuration, which is stored in a `Configuration Data Git Repository`. We provide a [`Default Configuration Data Git Repository`]( https://github.com/bhits-dev/c2s-config-data).

This API can run with the default configuration, which is targeted for a local development environment. Default configuration data is from three places: `bootstrap.yml`, `application.yml`, and the data which `Configuration Server` reads from `Configuration Data Git Repository`. Both `bootstrap.yml` and `application.yml` files are located in the `resources` folder of this source code.
  		  
We **recommend** overriding the configuration as needed in the `Configuration Data Git Repository`, which is used by the `Configuration Server`.
  		  
Also, please refer to [Spring Cloud Config Documentation](https://cloud.spring.io/spring-cloud-config/spring-cloud-config.html) to see how the config server works, [Spring Boot Externalized Configuration](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) documentation to see how Spring Boot applies the order to load the properties, and [Spring Boot Common Properties](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html) documentation to see the common properties used by Spring Boot.

### Examples for Overriding a Configuration in Spring Boot

#### Override a Configuration Using Program Arguments While Running as a JAR:

+ `java -jar edge-server-x.x.x-SNAPSHOT.jar --server.port=80 --logging.file=/logs/edge-server.log`

#### Override a Configuration Using Program Arguments While Running as a Docker Container:

+ `docker run -d bhitsdev/edge-server:latest --server.port=80 --logging.file=/logs/edge-server.log`

+ In a `docker-compose.yml`, this can be provided as:
```yml
version: '2'
services:
...
  edge-server.c2s.com:
    image: "bhitsdev/edge-server:latest"
    command: ["--server.port=80","--logging.file=/logs/edge-server.log"]
...
```
*NOTE: Please note that these additional arguments will be appended to the default `ENTRYPOINT` specified in the `Dockerfile` unless the `ENTRYPOINT` is overridden.*

### Enable SSL

For simplicity in development and testing environments, SSL is **NOT** enabled by default configuration. SSL can easily be enabled following the examples below:

#### Enable SSL While Running as a JAR

+ `java -jar edge-server-x.x.x-SNAPSHOT.jar --spring.profiles.active=ssl --server.ssl.key-store=/path/to/ssl_keystore.keystore --server.ssl.key-store-password=strongkeystorepassword`

#### Enable SSL While Running as a Docker Container

+ `docker run -d -v "/path/on/dockerhost/ssl_keystore.keystore:/path/to/ssl_keystore.keystore" bhitsdev/edge-server:latest --spring.profiles.active=ssl --server.ssl.key-store=/path/to/ssl_keystore.keystore --server.ssl.key-store-password=strongkeystorepassword`
+ In a `docker-compose.yml`, this can be provided as:
```yml
version: '2'
services:
...
  edge-server.c2s.com:
    image: "bhitsdev/edge-server:latest"
    command: ["--spring.profiles.active=ssl","--server.ssl.key-store=/path/to/ssl_keystore.keystore", "--server.ssl.key-store-password=strongkeystorepassword"]
    volumes:
      - /path/on/dockerhost/ssl_keystore.keystore:/path/to/ssl_keystore.keystore
...
```

*NOTE: As seen in the examples above, `/path/to/ssl_keystore.keystore` is made available to the container via a volume mounted from the Docker host running this container.*

### Override Java CA Certificates Store In Docker Environment

Java has a default CA Certificates Store that allows it to trust well-known certificate authorities. For development and testing purposes, one might want to trust additional self-signed certificates. In order to override the default Java CA Certificates Store in a Docker container, one can mount a custom `cacerts` file over the default one in the Docker image as follows: `docker run -d -v "/path/on/dockerhost/to/custom/cacerts:/etc/ssl/certs/java/cacerts" bhitsdev/edge-server:latest`

*NOTE: The `cacerts` references given in the both sides of volume mapping above are files, not directories.*

### Routing

By default, the Edge Server (Zuul) exposes all the service endpoints that are registered at the Discovery Server (Eureka). As can be seen in the [default configuration](edge-server/src/main/resources/application.yml), `zuul.ignoredServices='*'` property prevents this behavior by ignoring all services for routing. The following `zuul.routes` configuration explicitly defines the routes for each service that are supposed to be exposed by the Edge Server. The security is delegated to the resource servers that are exposed by the Edge Server. Therefore, **one should exercise great caution when configuring the routes to the microservices. The endpoints that contain sensitive information and do not implement a form of security MUST NOT BE exposed through the Edge Server.**

[//]: # (## Documentation)

[//]: # (## Notes)

[//]: # (## Contribute)

## Contact

If you have any questions, comments, or concerns please see [Consent2Share](http://bhits.github.io/consent2share/) project site.

## Report Issues

Please use [GitHub Issues](https://github.com/bhits-dev/edge-server/issues) page to report issues.

[//]: # (License)
