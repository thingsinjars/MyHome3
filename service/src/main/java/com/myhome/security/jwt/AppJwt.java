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

package com.myhome.security.jwt;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a JWT in the application.
 */
/**
 * represents a JWT token used for authentication in an application containing user
 * ID and expiration date information.
 * Fields:
 * 	- userId (String): in the AppJwt class represents a unique identifier for a
 * specific user within an application.
 * 	- expiration (LocalDateTime): represents the date and time after which the JWT
 * token ceases to be valid or usable.
 */
@Builder
@ToString
@Getter
public class AppJwt {
  private final String userId;
  private final LocalDateTime expiration;
}