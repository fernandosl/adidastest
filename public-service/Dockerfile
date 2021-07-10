FROM java:8-jdk-alpine
COPY ./pubservice-webcontroller/target/pubservice-uservice.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch pubservice-uservice.jar'
ENTRYPOINT ["java","-jar","pubservice-uservice.jar"]