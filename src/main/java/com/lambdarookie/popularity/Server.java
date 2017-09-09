package com.lambdarookie.popularity;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.lambdarookie.popularity.dataclasses.Score;
import com.lambdarookie.popularity.dataclasses.User;
import com.lambdarookie.popularity.exceptions.*;

// Defines the app's REST API
@RestController
public class Server {

  // Wiring `BadRequestException` to HTTP status `400 Bad Request`
  @ExceptionHandler(BadRequestException.class)
  private void handleBadRequest(HttpServletResponse response) throws IOException {
    response.sendError(HttpStatus.BAD_REQUEST.value());
  }

  // Wiring `ForbiddenException` to HTTP status `403 Forbidden`
  @ExceptionHandler(ForbiddenException.class)
  private void handleForbidden(HttpServletResponse response) throws IOException {
    response.sendError(HttpStatus.FORBIDDEN.value());
  }

  // Wiring `NotFoundException`s to HTTP status `404 Not Found`
  @ExceptionHandler(NotFoundException.class)
  private void handleNotFound(HttpServletResponse response) throws IOException {
    response.sendError(HttpStatus.NOT_FOUND.value());
  }

  // Wiring `InternalServerErrorException` to HTTP status `500 Internal Server Error`
  @ExceptionHandler(InternalServerErrorException.class)
  private void handleInternalServerError(HttpServletResponse response) throws IOException {
    response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  // Definition of the `/score?login=someUserName` route (GET)
  // A GitHub user name (`login`) has to be provided, an instance of `Score`, representing the user's popularity
  // score will be returned.
  @RequestMapping(value = "score", method=RequestMethod.GET)
  public Score score(@RequestParam(value = "login") String login)
      throws BadRequestException, ForbiddenException, NotFoundException, InternalServerErrorException {
    if (login == null || login.isEmpty()) {
      throw new BadRequestException("String parameter 'login' cannot be null or empty.");
    }
    if (login.equals("lambdarookie")) {
      throw new ForbiddenException("No no no, I am embarrassed of my score, you can't look it up.");
    }
    final Requester requester = new Requester();
    final Calculator calculator = new Calculator();
    final User user = requester.requestUser(login);
    final double score = calculator.calculateScore(user);
    return new Score(user.getLogin(), score);
  }

}
