package com.myhome.services.springdatajpa;

import com.myhome.domain.SecurityTokenType;
import com.myhome.domain.SecurityToken;
import com.myhome.domain.User;
import com.myhome.repositories.SecurityTokenRepository;
import com.myhome.services.SecurityTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

/**
 * provides security token-related functionality for an application. It generates
 * unique tokens, sets their expiration dates, and saves them to a repository for
 * future use. It also updates used tokens and persists them in the repository.
 * Additionally, it provides methods for creating email confirmation and password
 * reset tokens with specific expiration times.
 */
@Service
@RequiredArgsConstructor
public class SecurityTokenSDJpaService implements SecurityTokenService {

  private final SecurityTokenRepository securityTokenRepository;

  @Value("${tokens.reset.expiration}")
  private Duration passResetTokenTime;
  @Value("${tokens.email.expiration}")
  private Duration emailConfirmTokenTime;

  /**
   * creates a new security token with a unique identifier, creation and expiry dates,
   * and sets the token owner. It saves the token to the repository for later use.
   * 
   * @param tokenType type of security token being created, which determines the format
   * and content of the generated token.
   * 
   * * `tokenType`: Represents the type of security token, which can be one of the
   * predefined values (e.g., "Bearer", "ApprovalCode", etc.).
   * * `liveTimeSeconds`: The duration for which the security token is valid in seconds.
   * * `tokenOwner`: The user who owns the security token.
   * 
   * @param liveTimeSeconds duration of time that the generated security token will be
   * valid, and is used to calculate the expiration date of the token.
   * 
   * * `LocalDate.now()` represents the current date and time when the token is created.
   * * `getDateAfterDays(LocalDate.now(), liveTimeSeconds)` calculates the expiration
   * date of the token based on the provided `liveTimeSeconds`. The method takes two
   * parameters - the current date and time, and the total number of days for which the
   * token should be valid. It returns a new `LocalDate` object representing the
   * calculated expiration date.
   * 
   * @param tokenOwner user whose token is being created and stored in the SecurityToken
   * repository.
   * 
   * * `tokenOwner`: The User object that owns the security token.
   * 	+ Attributes:
   * 		- `id`: The unique identifier of the user.
   * 		- `username`: The username of the user.
   * 		- `email`: The email address of the user.
   * 		- `firstName`: The first name of the user.
   * 		- `lastName`: The last name of the user.
   * 
   * @returns a newly generated security token instance with a unique identifier,
   * creation date, expiry date, and owner.
   * 
   * * `token`: A unique token string generated using the `UUID.randomUUID()` method.
   * * `creationDate`: The current date and time when the security token was created.
   * * `expiryDate`: The date and time after which the security token will expire,
   * calculated by subtracting the `liveTimeSeconds` from the current date using the
   * `getDateAfterDays()` method.
   * * `tokenOwner`: The user who owns the security token.
   * * `securityTokenRepository`: A repository used to save the created security token
   * in the database.
   * 
   * The function returns a new security token instance with its properties set based
   * on the input parameters.
   */
  private SecurityToken createSecurityToken(SecurityTokenType tokenType, Duration liveTimeSeconds, User tokenOwner) {
    String token = UUID.randomUUID().toString();
    LocalDate creationDate = LocalDate.now();
    LocalDate expiryDate = getDateAfterDays(LocalDate.now(), liveTimeSeconds);
    SecurityToken newSecurityToken = new SecurityToken(tokenType, token, creationDate, expiryDate, false, null);
    newSecurityToken.setTokenOwner(tokenOwner);
    newSecurityToken = securityTokenRepository.save(newSecurityToken);
    return newSecurityToken;
  }

  /**
   * creates an email confirmation token for a user based on specified time and user information.
   * 
   * @param tokenOwner User object whose security token is being generated.
   * 
   * * `tokenOwner`: This is a `User` object that represents the user for whom an email
   * confirmation token is being created. The `User` class contains attributes such as
   * `id`, `email`, and `username`.
   * 
   * @returns a SecurityToken instance representing an email confirmation token.
   * 
   * * `SecurityToken`: This is the type of token returned, specifically `EMAIL_CONFIRM`.
   * * `tokenOwner`: The user for whom the token was created.
   * * `emailConfirmTokenTime`: The time at which the token was generated.
   */
  @Override
  public SecurityToken createEmailConfirmToken(User tokenOwner) {
    return createSecurityToken(SecurityTokenType.EMAIL_CONFIRM, emailConfirmTokenTime, tokenOwner);
  }

  /**
   * creates a security token for password reset with a generated token ID and expiration
   * time based on the provided user ID and current date-time.
   * 
   * @param tokenOwner User whose password reset token is being created.
   * 
   * * `tokenOwner`: represents a `User` object, which contains attributes such as
   * username, email, and password.
   * * `passResetTokenTime`: marks the time when the token was created or last updated.
   * 
   * @returns a security token with the specified type and expiration time, created
   * using the provided user's information.
   * 
   * * `SecurityTokenType`: This indicates that the token is a password reset token.
   * * `passResetTokenTime`: This represents the time when the token was generated or
   * updated.
   * * `tokenOwner`: This specifies the user for whom the password reset token was created.
   */
  @Override
  public SecurityToken createPasswordResetToken(User tokenOwner) {
    return createSecurityToken(SecurityTokenType.RESET, passResetTokenTime, tokenOwner);
  }

  /**
   * updates a given SecurityToken and saves it to the repository, marking it as used.
   * 
   * @param token SecurityToken that is being used, and it is modified to indicate that
   * it has been used and saved in the security token repository.
   * 
   * * `setUsed(true)` sets the `used` attribute to `true`.
   * * `securityTokenRepository.save(token)` saves the token in the repository.
   * 
   * The `token` object is destructured and its attributes are described as follows:
   * 
   * * `token`: The input SecurityToken object that represents a security token used
   * for authentication or authorization purposes.
   * 
   * @returns a modified SecurityToken object with the `used` field set to true and
   * saved in the repository.
   * 
   * * The `SecurityToken` object is modified by setting the `used` field to `true`.
   * * The `token` object is saved in the `securityTokenRepository`, which can be used
   * for later retrieval or manipulation.
   * * The returned `SecurityToken` object is the updated version of the original input
   * parameter, with the `used` field set to `true`.
   */
  @Override
  public SecurityToken useToken(SecurityToken token) {
    token.setUsed(true);
    token = securityTokenRepository.save(token);
    return token;
  }

  /**
   * takes a `LocalDate` and a `Duration` object as input, and returns a new `LocalDate`
   * after adding the specified number of days to the original date.
   * 
   * @param date date to be adjusted based on the specified `liveTime`.
   * 
   * * `LocalDate date`: This is the input parameter for the function, which represents
   * a date in the form of a LocalDate object.
   * * `liveTime Duration liveTime`: This is the second input parameter for the function,
   * which represents a duration of time in days.
   * 
   * @param liveTime number of days to add to the `date` parameter, resulting in the
   * new date after the specified duration has passed.
   * 
   * * The `Duration liveTime` is represented as an object with the toDays() method
   * that returns the number of days in the duration.
   * 
   * @returns a new `LocalDate` object representing the date after adding the specified
   * number of days to the input `LocalDate` and `Duration`.
   * 
   * * The returned value is a `LocalDate` object representing the date that is `liveTime`
   * days after the initial `date`.
   * * The `PlusDays` method used to calculate the updated date returns a new `LocalDate`
   * instance, which is the final result of the function.
   * * The `Duration` parameter `liveTime` represents the number of days to add to the
   * initial `date`, which determines the resulting date.
   */
  private LocalDate getDateAfterDays(LocalDate date, Duration liveTime) {
    return date.plusDays(liveTime.toDays());
  }
}
