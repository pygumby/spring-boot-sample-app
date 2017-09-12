package com.lambdarookie.popularity;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.lambdarookie.popularity.models.Score;
import com.lambdarookie.popularity.models.User;
import com.lambdarookie.popularity.exceptions.*;

// Defines the app's REST API
@RestController
public class Server {

  @Autowired
  private final Persistor persistor = new Persistor();
  private final Requester requester = new Requester();
  private final Calculator calculator = new Calculator();

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

  // `throwExceptionIfNull` will throw an exception if the provided String `login` is null or empty.
  private void throwExceptionIfNull(String login) throws BadRequestException {
    if (login == null || login.isEmpty())
      throw new BadRequestException("String parameter 'login' cannot be null or empty.");
  }
  // `throwExceptionIfForbidden` will throw an exception if the provided String `login` equals "lambdarookie".
  private void throwExceptionIfForbidden(String login) throws ForbiddenException {
    if (login.equals("lambdarookie"))
      throw new ForbiddenException("No no no, I am embarrassed of my score, you can't look it up.");
  }
  // `throwExceptionIfNotFound` will throw an exception if there is no GitHub user with the provided name `login`.
  private void throwExceptionIfNotFound(String login) throws NotFoundException, InternalServerErrorException {
    this.requester.requestUser(login); // May throw NotFoundException, InternalServerErrorException
  }

  // Definition of the `/score?login=someusername` route (GET)
  // A GitHub user name (`login`) has to be provided, an instance of `Score`, representing the user's popularity score
  // will be returned.
  @RequestMapping(value = "score", method=RequestMethod.GET)
  public Score getScore(@RequestParam(value = "login") String login)
      throws BadRequestException, ForbiddenException, NotFoundException, InternalServerErrorException {
    this.throwExceptionIfNull(login);
    this.throwExceptionIfForbidden(login);
    final User user = this.requester.requestUser(login); // May throw NotFoundException, InternalServerErrorException
    final double score = this.calculator.calculateScore(user);
    return new Score(user.getLogin(), score);
  }

  // Definition of the `/user?login=someusername` route (POST)
  // A GitHub user name (`login`) has to be provided and it will be stored in the database.
  @RequestMapping(value = "user", method = RequestMethod.POST)
  public void postUser(@RequestParam(value = "login") String login)
      throws BadRequestException, ForbiddenException, NotFoundException, InternalServerErrorException {
    this.throwExceptionIfNull(login);
    this.throwExceptionIfForbidden(login);
    this.throwExceptionIfNotFound(login);
    this.persistor.addLogin(login);
  }

  // Definition of the `/user?login=someusername` route (DELETE)
  // A GitHub user name (`login`) has to be provided and it will be deleted from the database.
  @RequestMapping(value = "user", method = RequestMethod.DELETE)
  public void deleteUser(@RequestParam(value = "login") String login)
      throws BadRequestException {
    this.throwExceptionIfNull(login);
    this.persistor.removeLogin(login);
  }

  // Definition of the `/list/` route (GET)
  // A `Set` of `Score`s (one for each GitHub user stored in the database) will be returned.
  @RequestMapping(value = "list", method = RequestMethod.GET)
  public Set<Score> getList() throws NotFoundException, InternalServerErrorException {
    Set<String> logins = this.persistor.getLogins();
    Set<Score> scores = new HashSet<>();
    for (String login : logins) {
      final User user = this.requester.requestUser(login);
      final Score score = new Score(user.getLogin(), calculator.calculateScore(user));
      scores.add(score);
    }
    return scores;
  }

}
