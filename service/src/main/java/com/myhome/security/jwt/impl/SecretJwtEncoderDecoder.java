/*
 * Copyright 2020 Prathab Murugan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myhome.security.jwt.impl;

import com.myhome.security.jwt.AppJwt;
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of {@link AppJwtEncoderDecoder}.
 */
/**
 * is an implementation of the AppJwtEncoderDecoder interface that handles encoding
 * and decoding of JSON Web Tokens (JWTs). The class provides methods for encoding a
 * JWT with a validated expiration time and a unique subject ID, signed with HMAC-SHA512
 * using a secret key. It also provides a method for decoding a JWT and extracting
 * the user ID, expiration date, and other relevant information.
 */
@Component
@Profile("default")
public class SecretJwtEncoderDecoder implements AppJwtEncoderDecoder {

  /**
   * decodes a JSON Web Token (JWT) and returns an updated `AppJwt` object with the
   * user ID and expiration time extracted from the decoded JWT.
   * 
   * @param encodedJwt JSON Web Token (JWT) that is to be decoded and transformed into
   * an `AppJwt` object.
   * 
   * @param secret HMAC-SHA key for verifying the signature of the JWT.
   * 
   * @returns an instance of `AppJwt` representing the decoded JWT.
   * 
   * * `userId`: The subject of the JWT claim, representing the user ID.
   * * `expiration`: The expiration time of the JWT, represented as an Instant object
   * in UTC time zone.
   * 
   * The output of the `decode` function is a new instance of the `AppJwt` class, which
   * contains the decoded JWT claims and additional attributes such as the user ID and
   * expiration time.
   */
  @Override public AppJwt decode(String encodedJwt, String secret) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
        .build()
        .parseClaimsJws(encodedJwt)
        .getBody();
    String userId = claims.getSubject();
    Date expiration = claims.getExpiration();
    return AppJwt.builder()
        .userId(userId)
        .expiration(expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
        .build();
  }

  /**
   * takes a `Jwt` object and a secret as input, generates a new JWT with an updated
   * expiration date based on the current time zone, and signs it with a HMAC-SHA-512
   * algorithm using the provided secret.
   * 
   * @param jwt JWT token to be encoded, which contains the user ID and expiration time.
   * 
   * 1/ `jwt`: The input JWT, which contains information about the user and the expiration
   * time.
   * 2/ `secret`: A secret key used for HMAC-SHA-512 signature generation.
   * 
   * @param secret 128-bit HMAC key used for signing the JWT.
   * 
   * @returns a compact JWT representing the user ID, expiration time, and HMAC-SHA-512
   * signature using the provided secret key.
   */
  @Override public String encode(AppJwt jwt, String secret) {
    Date expiration = Date.from(jwt.getExpiration().atZone(ZoneId.systemDefault()).toInstant());
    return Jwts.builder()
        .setSubject(jwt.getUserId())
        .setExpiration(expiration)
        .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS512).compact();
  }
}
