package com.lambdarookie.popularity.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
// Represents a GitHub user
public class User {

  // For the sake of simplicity, all properties returned by GitHub's API are discarded, except the ones listed below,
  // i.e., the user's name (`login`) as well as the number of followers and following. This is what the
  // `@JsonIgnoreProperties` annotation is for.
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
