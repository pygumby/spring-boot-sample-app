# spring-boot-sample-app

**An example app using basic features of Spring Boot**

---

* **spring-boot-sample-app** has been created by Lucas Baerenfaenger ([@lambdarookie](https://github.com/lambdarookie), [lambdarookie.com](https://lambdarookie.com)).
* Copyright © 2017 Lucas Baerenfaenger.

---

1. [Overview](#1-overview)
   1. [Feature requirements](#11-feature-requirements)
   2. [Implementation requirements](#12-implementation-requirements)
2. [Usage](2-usage)
   1. [Requesting a user's score](#21-requesting-a-users-score)
   2. [Storing and deleting user names](#22-storing-and-deleting-user-names)
   3. [Requesting a list of the stored user names and the respective popularity scores](#23-requesting-a-list-of-the-stored-user-names-and-the-respective-popularity-scores)

---

## 1. Overview

As part of the job application process at a German fintech start-up, I was asked to write a simple Java application featuring some basic functionality. I ended up implementing this exemplary app called `popularity` using Spring Boot. It revolves around the notion of a fairly bogus "GitHub popularity score", which is based on the ratio between the number of people that follow a user and the number of people that user follows.

This app is supposed to fulfill a set of requirements, which are listed below.

### 1.1. Feature requirements
- [x] The app should provide a REST API that takes parameters and returns reasonable HTTP status codes.
- [x] GitHub's REST API should somehow be used.
- [x] Data should be persisted in a database such as MySQL or H2 using ORM.

### 1.2. Implementation requirements
- [x] Git and GitHub should be used.
- [x] A framework such as Java EE 8 or Spring (Boot) should be used.
- [x] The code should be structured meaningfully.
- [x] Basic error handing should be done.
- [x] There should be unit tests.

## 2. Usage

Make sure you have Java (8 or newer) and Gradle (2.3 or newer) installed, then navigate into the project root directory and do `gradle test` to make sure everything is fine. In case some tests fail, information is provided in the file `build/reports/tests/test/index.html`. In order to start the app, do `gradle bootRun`. Once the app is running, you can issue HTTP requests to the server at `http://localhost:8080/`.

### 2.1. Requesting a user's score

Requesting the popularity score of a GitHub user, e.g., @ypsy, can be done with a `GET` request to the route `score`.

````http
GET http://localhost:8080/score?login=ypsy
````

At the time of this writing, @ypsy had one follower and followed two users himself. Based on this, his popularity score was `0.04040404040404041`. A popularity score is a number between 0 and 1. Rounded and multiplied by 100, @ypsy's score can be interpreted as 4%. This is what the JSON response to the above request looks like:

````json
{
  "login": "ypsy",
  "score": 0.04040404040404041
}
````

Since this app features error handling and sensible HTTP status codes, malformed HTTP requests trigger exceptions that then result in responses with the respective status codes. Below you can see malicious requests and how the app responds to them:

````http
GET http://localhost:8080/score?login=
````

````json
{
  "timestamp": 1504980713768,
  "status":    400,
  "error":     "Bad Request",
  "exception": "com.lambdarookie.popularity.exceptions.BadRequestException",
  "message":   "String parameter 'login' cannot be null or empty.",
  "path":      "/score"
}
````

````http
GET http://localhost:8080/score?login=lambdarookie
````

````json
{
  "timestamp": 1504980869873,
  "status":    403,
  "error":     "Forbidden",
  "exception": "com.lambdarookie.popularity.exceptions.ForbiddenException",
  "message":   "No no no, I am embarrassed of my score, you can't look it up.",
  "path":      "/score"
}
````

````http
GET http://localhost:8080/score?login=someusernameveryveryunlikelytoexist
````

````json
{
  "timestamp": 1504980977065,
  "status":    404,
  "error":     "Not Found",
  "exception": "com.lambdarookie.popularity.exceptions.NotFoundException",
  "message":   "There seems to be no user @someusernameveryveryunlikelytoexist.",
  "path":      "/score"
}
````

### 2.2. Storing and deleting user names

It is possible to store and delete GitHub user names to and from a database, respectively. Note that since an in-memory H2 database is used, data is only persisted as long as the server is running. Persistence is only featured in this app to demonstrate dependency injection and ORM.

User names can be stored or deleted by issuing a `POST` or `DELETE` request to the `user` route, respectively.

````http
POST   http://localhost:8080/user?login=ypsy
DELETE http://localhost:8080/user?login=ypsy
````

Malformed `POST` requests to the route `user` result in the following HTTP status codes:

| HTTP status code      | Reason                                                                                                                                               |
| --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| `BadRequest`          | `String login` is `null` or empty.                                                                                                                   |
| `Forbidden`           | GitHub user @lambdarookie is not allowed to be looked up.                                                                                            |
| `NotFound`            | GitHub user with the provided name cannot be found.                                                                                                  |
| `InternalServerError` | Either GitHub's API returned an error that is not a 404 error or something unexpected happened within this app and caused an exception to be thrown. |

Malformed `DELETE` requests to the route `user` result in the following HTTP status codes:

| HTTP status code | Reason                             |
| ---------------- | ---------------------------------- |
| `BadRequest`     | `String login` is `null` or empty. |

Some malicious behaviour is silently ignored by the database, e.g., storing the same user name more than once or deleting a user name that is not stored.

### 2.3. Requesting a list of the stored user names and the respective popularity scores

Lastly, by issuing a `GET` request to the `list` route, a list containing all GitHub user names stored in the database as well as their popularity scores can be obtained. For each user name stored in the database, an up-to-date score is calculated.

````http
GET http://localhost:8080/list/
````

This is what a response to such a request might look like:

````json
[
  {"login": "ypsy",    "score": 0.04040404040404041}
  {"login": "odersky", "score": 1},
]
````

Malformed `GET` requests to the route `list` result in the following HTTP status codes:

| HTTP status code      | Reason                                                                                                                                                                         |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `NotFound`            | GitHub user with the stored name cannot be found. (This should only happen if the respective GitHub account has been deleted after it's name has been stored to the database.) |
| `InternalServerError` | Either GitHub's API returned an error that is not a 404 error or something unexpected happened within this app and caused an exception to be thrown.                           |
