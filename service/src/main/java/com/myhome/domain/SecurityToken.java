package com.myhome.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * represents a secure token with a unique identifier, creation and expiry dates, and
 * an owner, with the ability to mark it as used or unused.
 * Fields:
 * 	- tokenType (SecurityTokenType): represents an enumerated type indicating the
 * category of security token, such as "access token" or "refresh token".
 * 	- token (String): is an instance of the SecurityTokenType enumeration type,
 * representing one of several types of security tokens, along with a unique token
 * string and metadata such as creation and expiry dates, and a boolean flag indicating
 * whether it has been used.
 * 	- creationDate (LocalDate): represents the date when the security token was created.
 * 	- expiryDate (LocalDate): represents the date after which the SecurityToken becomes
 * invalid or no longer usable.
 * 	- isUsed (boolean): in the SecurityToken class indicates whether a token has been
 * used or not.
 * 	- tokenOwner (User): in the SecurityToken class represents a user who owns or has
 * access to the security token in question.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"tokenOwner"})
public class SecurityToken extends BaseEntity {
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SecurityTokenType tokenType;
  @Column(nullable = false, unique = true)
  private String token;
  @Column(nullable = false)
  private LocalDate creationDate;
  @Column(nullable = false)
  private LocalDate expiryDate;
  private boolean isUsed;
  @ManyToOne
  private User tokenOwner;
}
