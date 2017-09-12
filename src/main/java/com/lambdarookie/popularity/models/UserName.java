package com.lambdarookie.popularity.models;

import javax.persistence.Entity;
import javax.persistence.Id;

// `@Table` is annotation left out, so it is assumed that this entity will be mapped to a table `Customer`.
@Entity // Indicating a JPA entity
public class UserName {

  @Id // Indicating that `login` is the JPA entity's id
  private String login;

  // Default constructor only exists for the JPA
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
