package com.lambdarookie.popularity.models;

import javax.persistence.Entity;
import javax.persistence.Id;

// `@Table` is annotation left out, so it is assumed that this entity will be mapped to a table `UserName`.
@Entity // Indicates a JPA entity
// Represents a GitHub user's name (`login`)
public class UserName {

  @Id // Indicates that `login` is the JPA entity's id
  // Contains the user's name (`login`)
  private String login;

  // Default constructor only exists for the JPA.
  protected UserName() {
  }

  public UserName(String login) {
    this.login = login;
  }

  public String getLogin() {
    return this.login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

}
