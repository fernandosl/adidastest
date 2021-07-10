

# Adidas TEST

Adidas TEST is the demo application for the task of developing three microservices for serving a subscription system.

## Modules

The TEST demo project consist of three independent microservices:
* public-uservice: exposes the functionality of the application for integration by the clients.
* subscription-service: implements the logic for the management of the subscriptions to the notifications service, it is called by the public-uservice.
* email-uservice: exposes the service for the process of sending the email, integrating de smtp functions.

## Building the project

Go to the root of the project from command line and type

```bash
mvn clean install
```

It will build the three independent subprojects ordering the modules for resolving the internal dependencies.

## Running

The projects are Spring Boot applications wich can be indepently run from command line. After a successful building from root project directory independent applications can be run from command line:

### Running from command line

```
cd public-service
java -jar pubservice-webcontroller/target/pubservice-uservice.jar
```
```
cd subscription-service
java -jar subscription-webcontroller/target/subscription-uservice.jar
```
```
cd email-service
java -jar email-webcontroller/target/email-uservice.jar
```

### Running from maven

```
cd public-service/pubservice-webcontroller
mvn spring-boot:run
```
```
cd subscription-service/subscription-service
mvn spring-boot:run
```
```
cd email-service/email-webcontroller
mvn spring-boot:run
```

### Running from Dockers

Each project has a Docker configuration, please configure application-docker.properties in each service with the proper values.
Following is a list with the commands for building and running docker images, take into account that for final configuration the 'docker' profile must be selected:

From public-service directory, using internal configured 8080 port:
```
docker build -t publicservice-app .
docker run -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=docker" publicservice-app
```
From subscription-service directory, using internal configured 8081 port:
```
docker build -t subscriptionservice-app .
docker run -p 8081:8081 -e "SPRING_PROFILES_ACTIVE=docker" subscriptionservice-app
```
From email-service directory, using internal configured 8082 port:
```
docker build -t emailservice-app .
docker run -p 8082:8082 -e "SPRING_PROFILES_ACTIVE=docker" emailservice-app
```

## Accesing the services

Default configuration in application.properties for the services exposes these access url:

```
public-uservice
url.........: http://localhost:8080
swagger.....: http://localhost:8080/swagger-ui/
actuator....: http://localhost:8080/actuator/

subscription-uservice
url.........: http://localhost:8081
swagger.....: http://localhost:8081/swagger-ui/
h2-console..: http://localhost:8081/h2-console/

public-uservice
url.........: http://localhost:8082
swagger.....: http://localhost:8082/swagger-ui/
```

Services and methods available can be seen in the swagger documentation of each service.

## Authentication

Services need authentication, security is maintained for stateless service by jwttoken. First it is needed to get the jwt token, endpoint for obtaining the token is in public-uservice.

```
url........: http://localhost:8080/api/v1/authentication/
method.....: POST
json body..: 
{
  "username": "test",
  "password": "pass"
}
```
This call will return a json object with two elemenst:
* "token": is the jwt bearer token for using with authentication
* "user": details of the autenticated user

The token must be added to the header to autenthicate subsequent calls. The header name is jwttoken and its value must be the word Bearer followed by one space and then the token.
The container RestTemplate injects the token if it has not been already added to the request.

Example
```
Header name...: jwttoken
Header value..: Bearer eyJhbGciOiJIUzUxMiJ9...[...]
```

## Roles

Services are secured with jwt token authentication and restricted by the use of user roles. Authenticated used must have the right roles to obtain permission for calling a specific service.

```
public-uservice endpoints
/api/v1/authentication     no user and no role needed
/api/v1/subscription       user with ROLE_PUB_SUBSCRIBE

subscription-uservice endpoints
/api/v1/subscription       user with ROLE_PUB_SUBSCRIBE

email-uservice endpoints
/api/v1/email              user with ROLE_EMAIL_SEND_MESSAGE
```

## Logs and storage

Logs and data storage is configured by default onto directory:
```
/storage
```

### Logs

All directories can be changed in each service configuring the application.properties and the logback-spring.xml file.
Default directories:
```
public........: /storage/logs/public-service
subscription..: /storage/logs/subscription-service
email.........: /storage/logs/email-service
```
Each logger has three log files: one general, one for hibernate (if applicable) and one for request made to service.

### Database

Subscription microservice has a embedded H2 database for keeping the created subscriptions, access data is:
```
user......: sa
password..: password
jdbcurl...: jdbc:h2:file:D:/storage/h2-data/testservice
```

Note that jdbc url for windows systems requires unit letter.

## Entities and validations

Subscription service uses SubscriptionDTO for inserting a new Subscription:
- Id must be null, it will be autoassigned on insertion.
- Email must be valid.
- Name is mandatory.
- Gender is a String (MALE/FEMALE...)
- Consent flat is one char: 1 consent 0 no consent

The service returns the inserted register with the id informed.

For querying and individual subscription the service requires the id of the subscription. Returns the json with the recovered object, 204 no content if not found.

Cancel a subscription requires the id of the register, returns true or false depending if it has been cancelled. Cancel is stored as a timestamp in cancelDate field.

Subscription list service can be paginated, supply page and number of rows per page to be returned. Reply includes x-total-count header with the total number of registers of the query. Filter parameters can be added to query passed in the same SubscriptionDTO object.


## License
Not needed