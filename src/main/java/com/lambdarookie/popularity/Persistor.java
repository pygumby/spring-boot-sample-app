package com.lambdarookie.popularity;

import com.lambdarookie.popularity.exceptions.BadRequestException;
import com.lambdarookie.popularity.exceptions.NotFoundException;

import java.util.HashSet;
import java.util.Set;

// `Persistor` is a dummy class that will eventually be replaced by a class that actually deals with a database. It
// mocks the ability to store and delete GitHub user names in a database as well as to retrieve them all at once.
public class Persistor {

  private Set<String> logins;

  public Persistor() {
    this.logins = new HashSet<>();
  }

  public void addLogin(String login) throws BadRequestException {
    if (this.logins.contains(login)) {
      throw new BadRequestException("Login @" + login + " cannot be stored since it already is.");
    }
    this.logins.add(login);
  }

  public void removeLogin(String login) throws NotFoundException {
    if (!this.logins.contains(login)) {
      throw new NotFoundException("Login @" + login + " cannot be removed since it is not stored.");
    }
    this.logins.remove(login);
  }

  public Set<String> getLogins() {
    return this.logins;
  }

}
