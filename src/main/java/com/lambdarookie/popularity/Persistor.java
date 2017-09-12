package com.lambdarookie.popularity;

import java.util.HashSet;
import java.util.Set;
import com.lambdarookie.popularity.models.UserName;
import com.lambdarookie.popularity.persistence.UserNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Persistor {

  @Autowired
  private UserNameRepository repository;

  public Persistor() {
  }

  public void addLogin(String login) {
    this.repository.save(new UserName(login));
  }

  public void removeLogin(String login) {
    this.repository.delete(new UserName(login));
  }

  public Set<String> getLogins() {
    Iterable<UserName> result = this.repository.findAll();
    Set<String> logins = new HashSet<String>();
    for (UserName userName : result) {
      logins.add(userName.getLogin());
    }
    return logins;
  }

}
