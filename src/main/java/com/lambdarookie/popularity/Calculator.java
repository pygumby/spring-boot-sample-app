package com.lambdarookie.popularity;

import com.lambdarookie.popularity.dataclasses.User;

// Performs calculations (normalization, popularity score)
public class Calculator {

  // Given a minimum and a maximum value, `value` will be scaled into the range between 0 and 1.
  private double normalize(double value, double min, double max) {
    return (value - min) / (max - min);
  }

  // Given a `User` instance representing a GitHub user, this user's popularity score will be returned. This is a
  // `double` between 0 (lowest) and 1 (highest), based on the ratio between
  // - the number of people that follow the user on GitHub ("followers") and
  // - the number of people that the user follows ("following").
  public double calculateScore(User user) {
    final double minScore = 0.1d; // Ratio of 1 followers : 10 following
    final double maxScore = 10d;  // Ratio of 10 followers : 1 following
    final double followers = user.getFollowers();
    final double following = user.getFollowing();
    // Avoid division by zero
    if (following == 0 && followers == 0) {
      return 0;
    }
    if (following == 0 /* && followers != 0 */) {
      return 1;
    }
    // Divide followers by following and return normalized ratio as score
    final double score = followers / following;
    if (score < minScore) {
      return 0;
    } else if (score > maxScore) {
      return 1;
    } else {
      return this.normalize(score, minScore, maxScore);
    }
  }

}
