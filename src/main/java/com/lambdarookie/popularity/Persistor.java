package com.lambdarookie.popularity;

import java.util.HashSet;
import java.util.Set;
import com.lambdarookie.popularity.models.UserName;
import com.lambdarookie.popularity.persistence.UserNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
// Handles interaction with the database
public class Persistor {

  // An implementation of the `UserNameRepository` interfacewill be provided at run-time, `@Autowired` indicates that an
  // instance of it will be injected.
  @Autowired
  private UserNameRepository repository;

  // Given a GitHub user name (`login`), a `UserName` entity will be created and stored.
  public void addLogin(String login) {
    this.repository.save(new UserName(login));
  }

  // Given a GitHub user name (`login`), if there is a corresponding `UserName` entity, it will be returned.
  public void removeLogin(String login) {
    this.repository.delete(new UserName(login));
  }

  // All of the stored `UserName` entities will be retrieved and a `Set<String> of their user names will be returned.
  public Set<String> getLogins() {
    Iterable<UserName> result = this.repository.findAll();
    Set<String> logins = new HashSet<>();
    for (UserName userName : result) {
      logins.add(userName.getLogin());
    }
    return logins;
  }

}
