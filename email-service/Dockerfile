FROM java:8-jdk-alpine
COPY ./target/email-webcontroller/target/email-uservice.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch email-uservice.jar'
ENTRYPOINT ["java","-jar","email-uservice.jar"]