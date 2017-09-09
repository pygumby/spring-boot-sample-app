package com.lambdarookie.popularity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
  public void httpStatusCodeShouldBeOk() throws Exception {
    this.mockMvc
      // For this test to succeed, of course, it is required that the GitHub account @odersky has not been deleted.
      .perform(get("/score").param("login", "odersky"))
      .andExpect(status().isOk());
      // At the time of this writing, @odersky's score was 1 (highest). To test that as well, uncomment the following:
      // .andExpect(jsonPath("$.score").value(1));
  }

  @Test
  public void httpStatusCodeShouldBeBadRequest() throws Exception {
    this.mockMvc
      .perform(get("/score").param("login", ""))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void httpStatusCodeShouldBeForbidden() throws Exception {
    this.mockMvc
      .perform(get("/score").param("login", "lambdarookie"))
      .andExpect(status().isForbidden());
  }

  @Test
  public void httpStatusCodeShouldBeNotFound() throws Exception {
    this.mockMvc
      .perform(get("/score").param("login", "someusernameveryveryunlikelytoexist"))
      .andExpect(status().isNotFound());
  }

}
