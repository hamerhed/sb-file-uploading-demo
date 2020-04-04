# SGMS rfid connector

This project contains the implementation of prototype of SGMS rfid connector serving RFID devices.

##Requirements

java 1.8

maven at least 3.x.x

To run component there are needed 2 running components: database and message-broker.

##Application building and run
In order to build and run application locally a maven command should be run:

`mvn clean spring-boot:run -P<profile>`

Application uses profiles in order to set up properties used by the application. The profiles are as follow:

- dev - used for development. May me modified by any developer in order to adjust properties to local system configuration.
- prod - designed to use in target environment

Configuration of these profiles are placed in *.properties inside /src/main/profiles directory.

##Application docker image creation and push to Docker registry
To build a new image maven tools has been configured

The command `mvn clean package -P<profile>` builds a local image of the application using current state of the project with the latest tag.
It means that any local changes of source code will be reflected in the image. This option should be used for testing new functionalities.
Sd the result latest tagged image is created.

The command `mvn clean install -P<profile>` builds and tags local image of the application using current version of artifact, e.g. 1.0.0 tag version.

The command `mvn clean deploy -P<profile>` builds and pushes to docker registry :latest and :<version> of the application images. This command
should be invoked from the master fully updated branch.

