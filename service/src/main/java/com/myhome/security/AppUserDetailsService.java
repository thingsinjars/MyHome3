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

package com.myhome.security;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.mapper.UserMapper;
import com.myhome.repositories.UserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom {@link UserDetailsService} catering to the need of service logic.
 */
/**
 * is a custom implementation of Spring Security's UserDetailsService interface. It
 * provides methods to load a user by their username and to retrieve the user's details
 * by their email address. The class maps the retrieved user entity from the repository
 * to a UserDto object for use in the application.
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  /**
   * loads a user by their username and returns a `UserDetails` object containing the
   * user's email, encrypted password, and other authentication-related information.
   * 
   * @param username username for which the user details are being loaded.
   * 
   * @returns a `UserDetails` object containing user information and authentication details.
   * 
   * * `email`: The email address of the user.
   * * `encryptedPassword`: The encrypted password for the user.
   * * `isActived`: A boolean indicating whether the user is active (true) or not (false).
   * * `isLocked`: A boolean indicating whether the user is locked (true) or not (false).
   * * `isAdmin`: A boolean indicating whether the user is an administrator (true) or
   * not (false).
   * * `groups`: An empty list, as there are no groups associated with the user.
   */
  @Override public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {

    com.myhome.domain.User user = userRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }

    return new User(user.getEmail(),
        user.getEncryptedPassword(),
        true,
        true,
        true,
        true,
        Collections.emptyList());
  }

  /**
   * retrieves a user's details from the repository and maps them to a `UserDto`. If
   * the user is not found, it throws a `UsernameNotFoundException`.
   * 
   * @param username username for which the user details are to be retrieved.
   * 
   * @returns a `UserDto` object containing the details of the user found in the database.
   * 
   * * The input `username` is used to retrieve a `com.myhome.domain.User` object from
   * the `userRepository`.
   * * If the `User` object is null, a `UsernameNotFoundException` is thrown.
   * * The `User` object is then mapped to a `UserDto` object using the `userMapper`.
   */
  public UserDto getUserDetailsByUsername(String username) {
    com.myhome.domain.User user = userRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return userMapper.userToUserDto(user);
  }
}
