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
 * TODO
 */
@Component
@Profile("default")
public class SecretJwtEncoderDecoder implements AppJwtEncoderDecoder {

  /**
   * decodes a JSON Web Token (JWT) and returns an instance of the `AppJwt` class with
   * the decoded user ID, expiration date, and other relevant information.
   * 
   * @param encodedJwt JSON Web Token (JWT) that is to be decoded and converted into
   * an instance of the `AppJwt` class.
   * 
   * 	- `encodedJwt`: A string representation of a JSON Web Token (JWT) that contains
   * claims about the user and its expiration date.
   * 	- `secret`: The secret key used to sign the JWT, which is required for decoding.
   * 
   * The function first uses the `Jwts.parserBuilder()` method to create a `JwsParsingContext`
   * instance with the provided secret key. Then, it parses the `encodedJwt` using the
   * `build().parseClaimsJws()` method, which returns a `Claims` object containing the
   * decoded claims from the JWT. Finally, the function creates a new `AppJwt` instance
   * with the user ID and expiration date extracted from the `Claims` object, and returns
   * it.
   * 
   * @param secret HSM key used for signing and verifying the JWT token.
   * 
   * 	- `secret`: This is the secret key used for signing the JWT. It is an instance
   * of the `Keys` class, which provides methods for generating and managing cryptographic
   * keys.
   * 	- `hmacShaKeyFor(secret.getBytes())`: This method generates a HMAC-SHA-256 key
   * for signing the JWT using the provided secret. The resulting key is an instance
   * of the `HmacSha256Key` class.
   * 	- `parseClaimsJws(encodedJwt)`: This method parses the JSON Web Token (JWT) and
   * extracts the claims from it. The claims are stored in a `Claims` object, which
   * represents the payload of the JWT.
   * 	- `getBody()`: This method returns the body of the `Claims` object, which contains
   * the subject and expiration information of the JWT.
   * 
   * @returns an instance of `AppJwt` with updated user ID and expiration date.
   * 
   * 	- The `AppJwt` object is constructed with the decoded `Claims` object from the
   * input JWT.
   * 	- The `userId` field contains the subject claim of the decoded JWT.
   * 	- The `expiration` field contains the expiration date and time of the decoded
   * JWT, represented as a `LocalDateTime`.
   * 
   * The output of the `decode` function can be further analyzed or processed based on
   * the specific use case.
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
   * takes a `AppJwt` object and a secret as input, and generates a new JWT with an
   * updated expiration time based on the current date and time, and signs it using
   * HmacShaKeyFor and HS512 algorithm.
   * 
   * @param jwt JSON Web Token (JWT) to be encoded, which contains information about
   * the user and their expiration date.
   * 
   * 	- `jwt`: This is an instance of the AppJwt class, which represents a JSON Web
   * Token (JWT) that contains information about a user's identity and other attributes.
   * 	- `secret`: This is a secret key used for signing the JWT.
   * 	- `expiration`: This represents the date and time when the JWT will expire,
   * represented as an Instant object in the standard Java Date format.
   * 	- `subject`: This represents the user ID associated with the JWT.
   * 
   * @param secret secret key used for HMAC-SHA-256 signature creation.
   * 
   * 	- `secret`: A string that represents a secret key used for HMAC-SHA-512 signature
   * creation.
   * 	- `getBytes()`: Returns the bytes representation of the `secret` string.
   * 
   * @returns a JWT with a validated expiration time and a unique subject ID, signed
   * with HMAC-SHA512 using a secret key.
   * 
   * 1/ `String encode`: The return type is a string representing the encoded JWT.
   * 2/ `Jwt jwt`: The input parameter is an instance of the `AppJwt` class, which
   * contains information about the JWT, such as the user ID and expiration date.
   * 3/ `Date expiration`: The `expiration` object is created by converting the
   * `jwt.getExpiration()` field to a `Date` object using the `Date.from()` method.
   * This object represents the expiration time of the JWT in milliseconds since the
   * epoch (January 1, 1970, 00:00:00 GMT).
   * 4/ `Builder builder`: The `builder` is an instance of the `Jwts.builder()` method,
   * which is used to create a new JWT builder object. This object allows us to specify
   * the claims and signing algorithm for the JWT.
   * 5/ `setSubject(String userId)`: The `userId` parameter sets the subject of the
   * JWT, which is the identifier of the user who the JWT belongs to.
   * 6/ `setExpiration(Date expiration)`: The `expiration` parameter sets the expiration
   * time of the JWT, as mentioned above.
   * 7/ `signWith(String signatureAlgorithm, byte[] secret)`: The `signatureAlgorithm`
   * parameter specifies the signing algorithm to be used for the JWT signature. In
   * this case, it is set to `SignatureAlgorithm.HS512`. The `secret` parameter is the
   * secret key used for signing the JWT.
   * 8/ `compact()`: The `compact()` method returns the encoded JWT as a string.
   * 
   * The output of the `encode` function is a JWT that contains information about the
   * user, the expiration time, and the signing algorithm used.
   */
  @Override public String encode(AppJwt jwt, String secret) {
    Date expiration = Date.from(jwt.getExpiration().atZone(ZoneId.systemDefault()).toInstant());
    return Jwts.builder()
        .setSubject(jwt.getUserId())
        .setExpiration(expiration)
        .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS512).compact();
  }
}
