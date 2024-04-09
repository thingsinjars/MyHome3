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
 * TODO
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
   * generates a unique token, sets its expiration date based on a provided duration,
   * and saves it to a repository for storage.
   * 
   * @param tokenType type of security token being created, which determines the
   * characteristics of the token.
   * 
   * 	- `tokenType`: This parameter represents the type of security token being created,
   * which can be one of several predefined values (e.g., `ClientCertificate`,
   * `SymmetricKey`, etc.).
   * 	- `liveTimeSeconds`: The duration in seconds that the security token is valid
   * for, starting from the creation date.
   * 	- `tokenOwner`: The user who owns the security token.
   * 
   * @param liveTimeSeconds duration of time that the security token is valid, which
   * is used to calculate the expiration date of the token.
   * 
   * 	- `liveTimeSeconds`: A `Duration` object representing the lifetime of the security
   * token in seconds.
   * 	- `Duration`: A class that represents a period of time, represented as an interval
   * between two points in time, usually measured in seconds or milliseconds.
   * 	- `LocalDate`: A class that represents a date and time in the form of a combination
   * of year, month, day, hour, minute, and second values.
   * 	- `getDateAfterDays`: A method that retrieves a new date that is a specified
   * number of days after the current date.
   * 
   * @param tokenOwner user who owns the security token being created.
   * 
   * 	- `tokenOwner`: The user who owns the security token.
   * 	- `LocalDate creationDate`: The date and time when the security token was created.
   * 	- `LocalDate expiryDate`: The date and time when the security token will expire.
   * 	- `boolean isActive`: A boolean value indicating whether the security token is
   * active or inactive.
   * 
   * @returns a newly created security token with a unique identifier, creation and
   * expiry dates, and a token owner.
   * 
   * 	- `token`: A unique token string generated using UUID.randomUUID() method.
   * 	- `creationDate`: The current date and time when the security token was created,
   * represented as a LocalDate object.
   * 	- `expiryDate`: The date and time after which the security token will expire,
   * calculated by subtracting the specified number of days from the current date and
   * time using the getDateAfterDays() method. Represented as a LocalDate object.
   * 	- `tokenOwner`: The user who owns the security token, represented as an instance
   * of the User class.
   * 	- `newSecurityToken`: An instance of the SecurityToken class, containing all the
   * properties and attributes of the created security token.
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
   * creates an email confirmation security token for a specified user based on the
   * current time and the user's identity.
   * 
   * @param tokenOwner user for whom the email confirmation token is being generated.
   * 
   * 	- `tokenOwner`: A `User` object representing the user whose email confirmation
   * token is being generated. The `User` class has attributes such as `id`, `username`,
   * `email`, and `password`.
   * 
   * @returns a SecurityToken object representing an email confirmation token.
   * 
   * 	- `SecurityTokenType`: This field denotes the type of security token created,
   * which is specifically `EMAIL_CONFIRM`.
   * 	- `emailConfirmTokenTime`: The time at which the email confirm token was generated.
   * 	- `tokenOwner`: The user whose email confirmation token has been created and returned.
   */
  @Override
  public SecurityToken createEmailConfirmToken(User tokenOwner) {
    return createSecurityToken(SecurityTokenType.EMAIL_CONFIRM, emailConfirmTokenTime, tokenOwner);
  }

  /**
   * creates a security token for password reset with a specified expiration time and
   * associated with the provided user.
   * 
   * @param tokenOwner user for whom a password reset token is being created.
   * 
   * 	- `tokenOwner`: A `User` object representing the user whose password reset token
   * is being generated. This object contains information about the user's account,
   * such as their username and email address.
   * 
   * @returns a security token with a type of `RESET` and a creation time stamped at `passResetTokenTime`.
   * 
   * 	- `SecurityTokenType`: This is an instance of the `SecurityTokenType` class, which
   * represents the type of security token being generated. In this case, it is set to
   * `RESET`, indicating that the token is for password reset purposes.
   * 	- `passResetTokenTime`: This is a long value representing the time at which the
   * password reset token was created. It is used in conjunction with other data to
   * ensure the token's validity and relevance.
   * 	- `tokenOwner`: This is an instance of the `User` class, which represents the
   * user for whom the password reset token is being generated. The token is personalized
   * to this user.
   */
  @Override
  public SecurityToken createPasswordResetToken(User tokenOwner) {
    return createSecurityToken(SecurityTokenType.RESET, passResetTokenTime, tokenOwner);
  }

  /**
   * updates a provided `SecurityToken` instance and persists it to the repository,
   * making it available for further use.
   * 
   * @param token SecurityToken object that is being used by the method, and its `used`
   * field is set to `true` before saving it in the security token repository using the
   * `save()` method.
   * 
   * 	- `setUsed(true)` marks the token as used to indicate that it has been consumed
   * in a security context.
   * 	- `securityTokenRepository.save(token)` persists the modified token in the
   * repository for future access.
   * 
   * @returns a modified SecurityToken object with the `used` field set to `true` and
   * saved in the repository.
   * 
   * 	- The `token` object is assigned a new value, which is saved in the `securityTokenRepository`.
   * 	- The `used` attribute of the token is set to `true`.
   * 	- The token's identity is persisted in the repository.
   */
  @Override
  public SecurityToken useToken(SecurityToken token) {
    token.setUsed(true);
    token = securityTokenRepository.save(token);
    return token;
  }

  /**
   * takes a `LocalDate` and a `Duration` as input, and returns a new `LocalDate`
   * representing the date after the specified number of days have passed since the
   * original date.
   * 
   * @param date LocalDate that is being modified by adding a specified number of days.
   * 
   * LocalDate is an immutable date-time value object that represents a point in time.
   * It has several properties, including year, month, day of the month, and hour of
   * the day. The `plusDays` method calculates the date after adding a specified number
   * of days to the original date.
   * 
   * @param liveTime number of days that must elapse after the original `date` before
   * the method returns a new `LocalDate`.
   * 
   * 	- `toDays()` is a method that converts a `Duration` object into a number of days.
   * 
   * @returns a new LocalDate that represents the date after adding the specified number
   * of days to the given date.
   * 
   * 	- The output is a `LocalDate` object representing the date after adding the
   * specified number of days to the given `date`.
   * 	- The `date` parameter is non-null and represents a valid `LocalDate` value.
   * 	- The `liveTime` parameter is non-null and represents a valid `Duration` value,
   * which is converted to days using the `toDays()` method.
   */
  private LocalDate getDateAfterDays(LocalDate date, Duration liveTime) {
    return date.plusDays(liveTime.toDays());
  }
}
