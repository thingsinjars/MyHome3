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
 * is responsible for authenticating users by checking their email and password,
 * creating an encoded JWT token, and returning an `AuthenticationData` object
 * containing the token and user ID. It also compares a provided password with an
 * encrypted version stored in a database and returns a boolean indicating whether
 * they match.
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
   * authenticates a user by checking their password and creating an JWT token for
   * authentication. If the password is incorrect, it throws an exception with the user
   * ID. The encoded JWT token is returned as the AuthenticationData object along with
   * the user ID.
   * 
   * @param loginRequest логин request received by the function, providing the email
   * address and password of the user attempting to log in.
   * 
   * 1/ `getEmail()`: retrieves the email address of the user attempting to log in.
   * 2/ `getPassword()`: retrieves the password entered by the user for authentication
   * verification.
   * 3/ `orElseThrow()`: throws a `UserNotFoundException` if no user is found with the
   * provided email address.
   * 4/ `isPasswordMatching()`: compares the entered password to the encrypted password
   * stored in the user's profile and returns `true` if they match, otherwise returns
   * `false`.
   * 5/ `throw new CredentialsIncorrectException()`: throws an exception with the user
   * ID of the user who attempted to log in if the passwords do not match.
   * 
   * @returns an `AuthenticationData` object containing an encoded JWT token and the
   * user ID.
   * 
   * * `AuthenticationData`: This is the class that represents the login response. It
   * contains two properties:
   * 	+ `encodedToken`: This is a string representing the JWT token encoded with the
   * secret key.
   * 	+ `userId`: This is an integer representing the user ID associated with the encoded
   * token.
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
   * compares a provided `requestPassword` with the corresponding value stored in the
   * database using the password encoder to ensure they match.
   * 
   * @param requestPassword password provided by the user for comparison with the
   * corresponding password stored in the database.
   * 
   * @param databasePassword password stored in the database that is being compared to
   * the `requestPassword`.
   * 
   * @returns a boolean value indicating whether the provided request password matches
   * the corresponding database password.
   */
  private boolean isPasswordMatching(String requestPassword, String databasePassword) {
    return passwordEncoder.matches(requestPassword, databasePassword);
  }

  /**
   * creates a JWT token for a given user by setting its expiration time and building
   * the token using the user ID and expiration date.
   * 
   * @param userDto user information that will be used to generate the JWT token.
   * 
   * * `userId`: The user ID associated with the JWT.
   * * `expirationTime`: The time when the JWT will expire, calculated by adding the
   * token expiration time to the current date and time.
   * 
   * @returns an AppJwt object containing user ID and expiration time.
   * 
   * * `userId`: The user ID associated with the JWT token.
   * * `expiration`: The expiration time of the JWT token in LocalDateTime format.
   * * `builder()`: The builder method used to construct the JWT token.
   */
  private AppJwt createJwt(UserDto userDto) {
    final LocalDateTime expirationTime = LocalDateTime.now().plus(tokenExpirationTime);
    return AppJwt.builder()
        .userId(userDto.getUserId())
        .expiration(expirationTime)
        .build();
  }
}
