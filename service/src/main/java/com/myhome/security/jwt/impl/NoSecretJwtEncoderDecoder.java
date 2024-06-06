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
import java.time.LocalDateTime;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of {@link AppJwtEncoderDecoder}. Use this only in testing.
 */
/**
 * is an implementation of the AppJwtEncoderDecoder interface that decodes a JSON Web
 * Token (JWT) and returns an instance of the AppJwt class with extracted user ID and
 * expiration time. The class splits the encoded JWT into two parts using a separator
 * and then uses these values to construct the AppJwt object's user ID and expiration
 * fields. Additionally, the class provides an encode method that concatenates the
 * user ID and expiration time of the JWT and returns the encoded string.
 */
@Profile("test")
@Component
public class NoSecretJwtEncoderDecoder implements AppJwtEncoderDecoder {
  private static final String SEPARATOR = "\\+";

  /**
   * decodes a JSON Web Token (JWT) and returns an instance of the `AppJwt` class with
   * the extracted user ID and expiration time.
   * 
   * @param encodedJwt JSON Web Token (JWT) that is being decoded and transformed into
   * an `AppJwt` object.
   * 
   * @param secret secret key used for signing the JWT, which is required to validate
   * the signature and unserstand the content of the JWT.
   * 
   * @returns an instance of `AppJwt` with the user ID and expiration time extracted
   * from the encoded JWT.
   * 
   * * `AppJwt`: This is the class that represents an JSON Web Token (JWT). It has
   * several attributes such as `userId`, `expiration`, and `build()` method to construct
   * a new JWT instance.
   * * `builder()` method: This is a factory method used to create a new `AppJwt` builder
   * instance, which can be used to modify the properties of the JWT before building it.
   * * `split(SEPARATOR)`: This method splits the input `encodedJwt` string into an
   * array of strings using the specified `SEPARATOR`.
   * * `LocalDateTime.parse()`: This method parses the second element of the array,
   * which represents the expiration time of the JWT in ISO 8601 format. It creates a
   * `LocalDateTime` object representing the expiration date and time.
   * 
   * The output of the `decode` function is an instance of `AppJwt`, which contains the
   * user ID and expiration date and time extracted from the input `encodedJwt`.
   */
  @Override public AppJwt decode(String encodedJwt, String secret) {
    String[] strings = encodedJwt.split(SEPARATOR);
    return AppJwt.builder().userId(strings[0]).expiration(LocalDateTime.parse(strings[1])).build();
  }

  /**
   * takes a JWT object and a secret as input, and returns a encoded string containing
   * the user ID and expiration time.
   * 
   * @param jwt Java library for JSON Web Tokens, which is used to generate and validate
   * tokens in this function.
   * 
   * * `jwt`: A `AppJwt` object representing a JSON Web Token.
   * * `secret`: The secret used to sign the token.
   * 
   * @param secret secret key used to sign the JWT token.
   * 
   * @returns a base64-encoded string representing the user ID and expiration time of
   * the JWT.
   */
  @Override public String encode(AppJwt jwt, String secret) {
    return jwt.getUserId() + SEPARATOR + jwt.getExpiration();
  }
}
