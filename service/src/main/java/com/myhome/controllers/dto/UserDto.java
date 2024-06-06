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

package com.myhome.controllers.dto;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * represents a data transfer object for storing and manipulating user information,
 * including id, userId, name, email, password, encryptedPassword, communityIds, and
 * emailConfirmed fields.
 * Fields:
 * 	- id (Long): represents a unique identifier for each user in the application.
 * 	- userId (String): represents a unique identifier for a user in the system.
 * 	- name (String): stores a string value representing the user's name.
 * 	- email (String): in the UserDto class stores a user's email address.
 * 	- password (String): stores a string value representing the user's password.
 * 	- encryptedPassword (String): in the UserDto class contains an encoded version
 * of the user's password for added security.
 * 	- communityIds (Set<String>): in the UserDto class stores a set of strings
 * representing the communities to which a user belongs.
 * 	- emailConfirmed (boolean): indicates whether an user's email address has been
 * confirmed through a verification process.
 */
@Builder
@Getter
@Setter
public class UserDto {
  private Long id;
  private String userId;
  private String name;
  private String email;
  private String password;
  private String encryptedPassword;
  private Set<String> communityIds;
  private boolean emailConfirmed;
}
