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
 * TODO
 */
@Profile("test")
@Component
public class NoSecretJwtEncoderDecoder implements AppJwtEncoderDecoder {
  private static final String SEPARATOR = "\\+";

  /**
   * decodes a JSON Web Token (JWT) and returns an instance of the `AppJwt` class with
   * the extracted values: user ID, expiration time.
   * 
   * @param encodedJwt JSON Web Token (JWT) that needs to be decoded and converted into
   * an instance of `AppJwt`.
   * 
   * 	- `encodedJwt`: A string containing the encrypted JWT token, which is split into
   * two parts using the provided separator `SEPARATOR`.
   * 	- `secret`: The secret key used for decoding the JWT token.
   * 
   * @param secret secret key used to verify the digital signature of the JWT, which
   * is necessary for authenticating the user and determining whether the JWT has been
   * tampered with.
   * 
   * The `encodedJwt` parameter is split into an array of strings using the `SEPARATOR`
   * constant.
   * 
   * The first element in the array represents the user ID, which is used to construct
   * the `AppJwt` object's `userId` field.
   * 
   * The second element in the array represents the expiration time of the JWT, which
   * is converted to a `LocalDateTime` object using the `parse()` method. This object
   * is then used to set the `expiration` field of the `AppJwt` object.
   * 
   * @returns an instance of `AppJwt` containing the user ID and expiration date extracted
   * from the encoded JWT.
   * 
   * 	- `AppJwt`: This is the class being modified to decode an JWT token.
   * 	- `builder()`: This is a method used to create a new instance of the `AppJwt`
   * class with default values.
   * 	- `userId(strings[0])`: The `userId` property is set to the first element of the
   * array `strings`, which is obtained by splitting the encoded JWT token using the `SEPARATOR`.
   * 	- `expiration(LocalDateTime.parse(strings[1]))`: The `expiration` property is set
   * to the second element of the array `strings`, which is a string in the format
   * `YYYY-MM-DDTHH:mm:ssZ`. This value represents the expiration time of the JWT token.
   * 	- `build()`: This method creates a new instance of the `AppJwt` class with the
   * properties set using the `builder()` method.
   */
  @Override public AppJwt decode(String encodedJwt, String secret) {
    String[] strings = encodedJwt.split(SEPARATOR);
    return AppJwt.builder().userId(strings[0]).expiration(LocalDateTime.parse(strings[1])).build();
  }

  /**
   * takes a `AppJwt` object and a secret as input, and returns a modified `AppJwt`
   * object with an additional field containing the user ID and expiration date.
   * 
   * @param jwt JSON Web Token being encoded, which contains the user ID and expiration
   * time.
   * 
   * 	- `jwt`: A `AppJwt` object containing information about the JWT token, including
   * the user ID and expiration time.
   * 
   * @param secret secret key used to sign the JWT token.
   * 
   * 	- `secret`: The secret used to sign the JWT.
   * 	- `jwt`: The JWT object containing the user ID and expiration time.
   * 
   * @returns a concatenation of the `userId` and `expiration` properties of the `AppJwt`
   * object, separated by a separator.
   * 
   * 	- `jwt.getUserId()`: This is a string representing the user ID.
   * 	- `SEPARATOR`: This is a constant string used to separate the user ID and expiration
   * time.
   * 	- `jwt.getExpiration()`: This is an integer representing the expiration time of
   * the JWT in milliseconds since the Unix epoch (January 1, 1970, 00:00:00 UTC).
   */
  @Override public String encode(AppJwt jwt, String secret) {
    return jwt.getUserId() + SEPARATOR + jwt.getExpiration();
  }
}
