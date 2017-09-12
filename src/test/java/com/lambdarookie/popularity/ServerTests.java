package com.lambdarookie.popularity;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ServerTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void getScoreStatusShouldBeOk() throws Exception {
    this.mockMvc
      // For this and some of the following tests to succeed, of course, it is required that the GitHub account @odersky
      // has not been deleted.
      .perform(get("/score").param("login", "odersky"))
      .andExpect(status().isOk())
      // At the time of this writing, @odersky's score was 1 (highest). To test that as well, uncomment the following:
      .andExpect(jsonPath("$.score").value(1));
  }

  @Test
  public void getScoreStatusShouldBeBadRequest() throws Exception {
    this.mockMvc
      .perform(get("/score").param("login", ""))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void getScoreStatusShouldBeForbidden() throws Exception {
    this.mockMvc
      .perform(get("/score").param("login", "lambdarookie"))
      .andExpect(status().isForbidden());
  }

  @Test
  public void getScoreStatusShouldBeNotFound() throws Exception {
    this.mockMvc
      .perform(get("/score").param("login", "someusernameveryveryunlikelytoexist"))
      .andExpect(status().isNotFound());
  }

  @Test
  public void postUserStatusShouldBeOk() throws Exception {
    this.mockMvc
      // Again, for this test to succeed, it is required that the GitHub account @odersky has not been deleted.
      .perform(post("/user").param("login", "odersky"))
      .andExpect(status().isOk());
    this.mockMvc
      // TODO Avoid dependencies!
      // This test should not rely on `DELETE` requests to route `user` for its clean-up. Below, these requests are
      // tested separately.
      .perform(delete("/user").param("login", "odersky"));
  }

  @Test
  public void postUserStatusShouldBeBadRequest() throws Exception {
    this.mockMvc
      .perform(post("/user").param("login", ""))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void postUserStatusShouldBeForbidden() throws Exception {
    this.mockMvc
      .perform(post("/user").param("login", "lambdarookie"))
      .andExpect(status().isForbidden());
  }

  @Test
  public void postUserStatusShouldBeNotFound() throws Exception {
    this.mockMvc
      .perform(post("/user").param("login", "someusernameveryveryunlikelytoexist"))
      .andExpect(status().isNotFound());
  }

  @Test
  public void deleteUserStatusShouldBeOk() throws Exception {
    this.mockMvc
       // TODO Avoid dependencies!
       // Again, testing `DELETE` on `user` should not require `POST` to work correctly.
      .perform(post("/user").param("login", "odersky"))
      .andExpect(status().isOk());
    this.mockMvc
      .perform(delete("/user").param("login", "odersky"))
      // TODO Avoid misleading design!
      // Caution! The database silently ignores malicious queries. Therefore, we could have asked it to delete "odersky"
      // even if this user name had not been stored in the database, and we would have still gotten the HTTP status code
      // `OK`. Accordingly, this does not indicate a successful database operation! It only indicates that the request
      // was well-formed (i.e., the parameter was not empty or null) and no exception has occurred.
      .andExpect(status().isOk());
  }

  @Test
  public void deleteUserStatusShouldBeBadRequest() throws Exception {
    this.mockMvc
      .perform(post("/user").param("login", ""))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void getListShouldBeOk() throws Exception {
    this.mockMvc
       // TODO Avoid dependencies!
      .perform(post("/user").param("login", "odersky"))
      .andExpect(status().isOk());
    this.mockMvc
      .perform(get("/list"))
      .andExpect(status().isOk());
      // Again, at the time of this writing, @odersky's score was 1. If this is still true, uncomment the following:
      // .andExpect(content().json("[ { \"login\": \"odersky\", \"score\": 1 } ])"));
  }

}
