package com.lambdarookie.popularity;

import org.junit.Test;
import org.junit.Assert;
import com.lambdarookie.popularity.models.User;

public class CalculatorTests {

  private Calculator calculator = new Calculator();
  private User user = new User();

  @Test
  public void testCalculateScoreCornerCase1() {
    user.setFollowers(0L);
    user.setFollowing(0L);
    Assert.assertEquals(0d, this.calculator.calculateScore(user), 0.00001d);
  }

  @Test
  public void testCalculateScoreCornerCase2() {
    user.setFollowers(42L);
    user.setFollowing(0L);
    Assert.assertEquals(1d, this.calculator.calculateScore(user), 0.00001d);
  }

  @Test
  public void testCalculateScoreMinimum() {
    user.setFollowers(1L);
    user.setFollowing(10L);
    Assert.assertEquals(0d, this.calculator.calculateScore(user), 0.00001d);
  }

  @Test
  public void testCalculateScoreMaximum() {
    user.setFollowers(10L);
    user.setFollowing(1L);
    Assert.assertEquals(1d, this.calculator.calculateScore(user), 0.00001d);
  }

  @Test
  public void testCalculateScore10Followers2Following() {
    user.setFollowers(10L);
    user.setFollowing(2L);
    Assert.assertEquals(0.494949d, this.calculator.calculateScore(user), 0.00001d);
  }

}
