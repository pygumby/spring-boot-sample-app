package com.lambdarookie.popularity.dataclasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Represents a GitHub user
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  // For the sake of simplicity, all properties returned by GitHub's API are discarded, except the user's name (`login`)
  // as well as the number of followers and following.
  private String login;
  private Long followers;
  private Long following;

  public User() {
  }

  public String getLogin() {
    return this.login;
  }

  public Long getFollowers() {
    return this.followers;
  }

  public Long getFollowing() {
    return this.following;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public void setFollowers(Long followers) {
    this.followers = followers;
  }

  public void setFollowing(Long following) {
    this.following = following;
  }

}
