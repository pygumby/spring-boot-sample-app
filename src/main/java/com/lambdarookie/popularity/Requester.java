package com.lambdarookie.popularity;

import org.springframework.web.client.RestTemplate;
import com.lambdarookie.popularity.dataclasses.User;
import com.lambdarookie.popularity.exceptions.InternalServerErrorException;
import com.lambdarookie.popularity.exceptions.NotFoundException;

// Handles outgoing requests to GitHub's REST API
public class Requester {

  // Issues a GET request to GitHub's API and then populates and returns a `User` instance
  public User requestUser(String name) throws NotFoundException, InternalServerErrorException {
    RestTemplate restTemplate = new RestTemplate();
    try {
      User user = restTemplate.getForObject("https://api.github.com/users/" + name, User.class);
      return user;
    } catch (Exception e) {
      if (e.getMessage().equals("404 Not Found")) {
        throw new NotFoundException("There seems to be no user @" + name + ".");
      } else {
        throw new InternalServerErrorException("Something went wrong when requesting user @" + name + ".");
      }
    }
  }

}
