package com.myhome.services.springdatajpa;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.exceptions.CredentialsIncorrectException;
import com.myhome.controllers.exceptions.UserNotFoundException;
import com.myhome.domain.AuthenticationData;
import com.myhome.model.LoginRequest;
import com.myhome.security.jwt.AppJwt;
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import com.myhome.services.AuthenticationService;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * TODO
 */
@Slf4j
@Service
public class AuthenticationSDJpaService implements AuthenticationService {

  private final Duration tokenExpirationTime;
  private final String tokenSecret;

  private final UserSDJpaService userSDJpaService;
  private final AppJwtEncoderDecoder appJwtEncoderDecoder;
  private final PasswordEncoder passwordEncoder;

  public AuthenticationSDJpaService(@Value("${token.expiration_time}") Duration tokenExpirationTime,
      @Value("${token.secret}") String tokenSecret,
      UserSDJpaService userSDJpaService,
      AppJwtEncoderDecoder appJwtEncoderDecoder,
      PasswordEncoder passwordEncoder) {
    this.tokenExpirationTime = tokenExpirationTime;
    this.tokenSecret = tokenSecret;
    this.userSDJpaService = userSDJpaService;
    this.appJwtEncoderDecoder = appJwtEncoderDecoder;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * authenticates a user by checking their email and password, creating an encoded JWT
   * token, and returning an `AuthenticationData` object containing the token and user
   * ID.
   * 
   * @param loginRequest login request received from the client and contains the email
   * address of the user to be authenticated, along with their password.
   * 
   * 	- `log.trace("Received login request")`: This line logs a message indicating that
   * the login request has been received.
   * 	- `final UserDto userDto = userSDJpaService.findUserByEmail(loginRequest.getEmail())`:
   * This line retrieves the user details from the database using the provided email
   * address. The method `findUserByEmail` returns a `Optional<UserDto>` object, which
   * contains the user details if found, or an empty `Optional` otherwise.
   * 	- `orElseThrow(() -> new UserNotFoundException(loginRequest.getEmail()))`: This
   * line handles the case where the user is not found in the database. It throws a
   * `UserNotFoundException` with the provided email address as its message.
   * 	- `if (!isPasswordMatching(loginRequest.getPassword(), userDto.getEncryptedPassword()))
   * {`: This line checks whether the provided password matches the encrypted password
   * of the retrieved user details. If they don't match, an exception is thrown.
   * 	- `throw new CredentialsIncorrectException(userDto.getUserId())`: This line throws
   * a `CredentialsIncorrectException` with the user ID as its message, indicating that
   * the provided credentials are incorrect.
   * 	- `final AppJwt jwtToken = createJwt(userDto);`: This line creates a new JWT token
   * using the retrieved user details.
   * 	- `final String encodedToken = appJwtEncoderDecoder.encode(jwtToken, tokenSecret)`:
   * This line encodes the JWT token using the provided secret key.
   * 	- `return new AuthenticationData(encodedToken, userDto.getUserId());`: This line
   * returns an `AuthenticationData` object containing the encoded token and the user
   * ID.
   * 
   * @returns an `AuthenticationData` object containing an encoded JWT token and the
   * user ID.
   * 
   * 	- `encodedToken`: This is a string that represents an encoded JWT token, generated
   * using the `createJwt` method and the `tokenSecret`.
   * 	- `userId`: This is the unique identifier of the user who has successfully logged
   * in.
   */
  @Override
  public AuthenticationData login(LoginRequest loginRequest) {
    log.trace("Received login request");
    final UserDto userDto = userSDJpaService.findUserByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new UserNotFoundException(loginRequest.getEmail()));
    if (!isPasswordMatching(loginRequest.getPassword(), userDto.getEncryptedPassword())) {
      throw new CredentialsIncorrectException(userDto.getUserId());
    }
    final AppJwt jwtToken = createJwt(userDto);
    final String encodedToken = appJwtEncoderDecoder.encode(jwtToken, tokenSecret);
    return new AuthenticationData(encodedToken, userDto.getUserId());
  }

  /**
   * compares a provided password with an encrypted version stored in a database and
   * returns a boolean indicating whether they match.
   * 
   * @param requestPassword password provided by the user for authentication purposes.
   * 
   * 	- `requestPassword`: A string parameter representing the user-provided password.
   * 	- `databasePassword`: A string parameter representing the stored password in the
   * database.
   * 
   * @param databasePassword encrypted password stored in the database.
   * 
   * 	- `passwordEncoder`: This is an object responsible for encoding and decoding
   * passwords in the system.
   * 	- `requestPassword`: This is the password entered by the user.
   * 	- `databasePassword`: This is the password stored in the database that needs to
   * be compared with the user-entered password.
   * 
   * @returns a boolean value indicating whether the provided request password matches
   * the corresponding database password.
   */
  private boolean isPasswordMatching(String requestPassword, String databasePassword) {
    return passwordEncoder.matches(requestPassword, databasePassword);
  }

  /**
   * creates an AppJwt object representing a JSON Web Token (JWT) for a given user ID,
   * with an expiration time calculated based on a provided tokenExpirationTime value.
   * 
   * @param userDto user's details, including their ID, which are used to create a
   * unique JWT token.
   * 
   * 	- `userId`: The unique identifier of the user.
   * 
   * @returns a `AppJwt` instance containing user details and expiration time.
   * 
   * 	- `userId`: The user ID of the user to whom the JWT is being created.
   * 	- `expiration`: The expiration time of the JWT, calculated as the current date
   * and time plus the tokenExpirationTime parameter.
   * 	- `build()`: This method creates a new instance of the `AppJwt` class with the
   * specified properties.
   */
  private AppJwt createJwt(UserDto userDto) {
    final LocalDateTime expirationTime = LocalDateTime.now().plus(tokenExpirationTime);
    return AppJwt.builder()
        .userId(userDto.getUserId())
        .expiration(expirationTime)
        .build();
  }
}
