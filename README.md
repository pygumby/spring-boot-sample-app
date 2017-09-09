# SpringBootSampleApp

## An exemplary app using basic features of Spring Boot

### Overview

As part of the job application process at a German fintech start-up, I was asked to write a simple Java application featuring some basic functionality. I ended up implementing this exemplary app called `popularity` using Spring Boot. It revolves around the notion of a fairly bogus "GitHub popularity score", which is based on the ratio between the number of people that follow a user and the number of people that user follows.

This app is supposed to fulfill a set of requirements. They are listed below, along with the information whether or not they are featured yet.

#### Feature requirements
- [x] The app should provide a REST API that takes parameters and gives reasonable return codes.
- [x] At some point, GitHub's REST API should somehow be used.
- [ ] Data should be persisted in a database such as MySQL or H2 using ORM.

#### Implementation requirements
- [x] Git and GitHub should be used.
- [x] A framework such as Java EE 8 or Spring (Boot) should be used.
- [x] The code should be structured meaningfully.
- [x] Basic error handing should be done.
- [x] There should be unit tests.

### Usage

Make sure you have Java and Gradle installed, then navigate into the project root directory and do `gradle test` to make sure everything is fine. In case some tests fail, information is provided in the file `build/reports/tests/test/index.html`. In order to start the app, do `gradle bootRun`.

#### Implemented features

Once the app is running, you can issue HTTP requests to the server at `http://localhost:8080/`.

Requesting the popularity score of a GitHub user, e.g., @ypsy, can be done with a GET request to the route `score`:

````http
GET http://localhost:8080/score?login=ypsy
````

At the time of this writing, @ypsy had one follower and followed two users himself. Based on this, his popularity score was `0.04040404040404041`. A popularity score is a number between 0 and 1. Rounded and multiplied by 100, `ypsy`'s score can be interpreted as 4%. This is what the JSON response looks like:

````json
{
  "login": "ypsy",
  "score": 0.04040404040404041
}
````

Since this app features error handling and sensible HTTP status codes, malformed HTTP requests trigger exceptions that result in responses with appropriate status codes. Below you can see malicious requests and how the app responded to them:

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

#### Not-yet implemented features

I plan to introduce a route `user` to allow for user names to be saved and deleted. Saving user names will cause them to be persisted in a database, deleting them removes them from it.

````http
POST   http://localhost:8080/user?login=someusername
DELETE http://localhost:8080/user?login=someusername
````

Lastly, I plan to introduce a route `list` to allow for high score lists to be obtained. For each user name stored in the database, an up-to-date popularity score is calculated. Then, the users are sorted by their score and returned.

````http
GET http://localhost:8080/list
````

This is what a response to such a request might look like:

````json
[
  {"login": "odersky", "score": 1},
  {"login": "ypsy", "score": 0.04040404040404041}
]
````

### Credit

Copyright (C) 2017 Lucas Bärenfänger ([@lambdarookie](https://github.com/lambdarookie/))
