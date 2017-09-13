package com.lambdarookie.popularity.models;

// Represents a GitHub user's popularity score
public class Score {

  // Contains the user's name (`login`) and score
  private final String login;
  private final Double score;

  public Score(String login, Double score) {
    this.login = login;
    this.score = score;
  }

  public String getLogin() {
    return this.login;
  }

  public Double getScore() {
    return this.score;
  }

}
