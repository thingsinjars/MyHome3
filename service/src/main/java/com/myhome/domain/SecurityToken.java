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
 * represents a secure token with unique identifier, creation and expiry dates, owner,
 * and flags indicating whether it has been used or not.
 * Fields:
 * 	- tokenType (SecurityTokenType): represents an enumerated type indicating the
 * category of security token, such as "access token" or "refresh token".
 * 	- token (String): in the SecurityToken class represents a unique identifier for
 * a security token with metadata including creation and expiry dates, and an owner.
 * 	- creationDate (LocalDate): represents the date when the security token was created.
 * 	- expiryDate (LocalDate): represents the date after which the security token
 * becomes invalid or no longer usable.
 * 	- isUsed (boolean): indicates whether a security token has been used or not.
 * 	- tokenOwner (User): represents a user who owns or has access to the security
 * token in question.
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
