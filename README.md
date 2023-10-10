# Modak Challenge


## Table of Contents
1. [Introduction](#Introduction)
2. [Challenge Requirements](#challenge-requirements)
3. [Instructions](#instructions)
4. [Endpoint](#endpoint)




## Introduction


The following API was created as part of a Challenge. It was built using the following technologies:
- Java 21
- Spring Boot
- Spring Web
- Spring Data Redis
- Redis
- Docker



## Requerimientos Challenge
We have a Notification system that sends out email notifications of various types (supdatesupdate, daily news, project invitations, etc). We need to protect recipients from getting too many emails, either due to system errors or due to abuse, so let's limit the number of emails sent to them by implementing a rate-limited version of NotificationService.
The system must reject requests that are over the limit.
Some sample notification types and rate limit rules, e.g.:

- Status: not more than 2 per minute for each recipient

- News: not more than 1 per day for each recipient

- Marketing: not more than 3 per hour for each recipient

Etc. these are just samples, the system might have several rate limit rules!

NOTES:

- Your solution will be evaluated on code quality, clarity and development best practices.
- Feel free to use the programming language, frameworks, technologies, etc that you feel more comfortable with.
- Below you'll find a code snippet that can serve as a guidance of one of the implementation alternatives in Java. Feel free to use it if you find it useful or ignore it otherwise; it is not required to use it at all nor translate this code to your programming language.





## Instructions
To run the application, you need to have the following installed:
- Postman or a similar tool.
- Docker and Docker Compose.
- Git (if you want to clone the repository).




### Running with Docker Compose


You can use Docker Compose to run the application and its dependencies (Redis). You can either clone the Git repository or simply copy or download the docker-compose.yml file.


1. Clone the repository (optional):


```
git clone git@github.com:YMT03/modak.git
```
* Alternatively, you can download the [docker-compose.yml](https://github.com/YMT03/modak/blob/master/docker-compose.yml).
2. Run Docker Compose:
```
docker compose up
```
* Run this command at the root of the project directory or where the docker compose file is located.


### Performing Tests
To perform tests, if you're using Postman, you can use the following [collection](https://github.com/YMT03/modak/blob/master/src/main/resources/postman/modak.postman_collection).


## Endpoint


### POST /api/notifications


This is the endpoint for sending notifications. 
The service performs validations on the request body. If the request does not pass validation, an appropriate error will be returned.
###### Example of a valid request payload:

```
{
    "type": "status", // string - required - valid types: (news, status, marketing)
    "user_id": "1", // string - required
    "message": "Some new feature!" // string - required
}
```


###### Ejemplo de payload response
```
{
    "type": "status",
    "user_id": "1",
    "message": "Some new feature!"
}
```


###### Example of an error response due to an invalid request body:
```
{
    "id": "empty_notification_type",
    "title": "Empty notification type",
    "description": "Notification type cannot be empty",
    "http_status": 400,
    "date": "2023-10-10T20:57:16.461302652Z"
}
```


###### Example of an error response due to exceeding the rate limit:

```
{
    "id": "status_notifications_exceeded",
    "title": "Too Many Requests",
    "description": "Notifications of type STATUS have been exceeded for user 1",
    "http_status": 429,
    "date": "2023-10-10T20:47:55.333490063Z"
}
```






