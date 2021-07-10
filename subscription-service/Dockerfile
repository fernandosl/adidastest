FROM java:8-jdk-alpine
COPY ./subscription-webcontroller/target/subscription-uservice.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch subscription-uservice.jar'
ENTRYPOINT ["java","-jar","subscription-uservice.jar"]