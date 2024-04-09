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
 * TODO
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  /**
   * loads a user by their username, retrieving the user from the repository and returning
   * a `User` object with the email address, encrypted password, and other properties
   * set to true.
   * 
   * @param username username for which the UserDetails object is to be loaded.
   * 
   * 	- `email`: The user's email address.
   * 	- `encryptedPassword`: The encrypted password for the user.
   * 	- `isAdmin`: A boolean value indicating whether the user is an administrator or
   * not.
   * 	- `isActive`: A boolean value indicating whether the user is active or not.
   * 	- `isAccountNonExpired`: A boolean value indicating whether the user's account
   * has not expired.
   * 	- `isPasswordNonExpired`: A boolean value indicating whether the user's password
   * is non-expired.
   * 	- `accountLocked`: A boolean value indicating whether the user's account is locked.
   * 
   * @returns a `UserDetails` object representing the user with the provided username.
   * 
   * 	- `Email`: The email address of the user.
   * 	- `EncryptedPassword`: The encrypted password for the user.
   * 	- `IsAdmin`: A boolean indicating whether the user is an administrator or not.
   * 	- `IsEnabled`: A boolean indicating whether the user is enabled or not.
   * 	- `IsAccountNonExpired`: A boolean indicating whether the user's account is
   * non-expired or not.
   * 	- `IsPasswordNonExpired`: A boolean indicating whether the user's password is
   * non-expired or not.
   * 	- `Collections emptyList`: An empty list of objects, which is used to represent
   * an empty collection.
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
   * retrieves a `User` entity from the repository based on the provided username, maps
   * it to a `UserDto`, and returns the mapped result.
   * 
   * @param username email address of the user for which details are being requested.
   * 
   * 	- `username`: This parameter represents a string value passed as an argument to
   * the function.
   * 	- `userRepository.findByEmail(username)`: This method call retrieves a
   * `com.myhome.domain.User` object from the user repository based on the provided `username`.
   * 	- `userMapper.userToUserDto(user)`: This method call converts the retrieved
   * `com.myhome.domain.User` object into a `UserDto` object, which is then returned
   * as the function's output.
   * 
   * @returns a `UserDto` object containing the details of the user with the specified
   * username.
   * 
   * 	- The function returns a `UserDto` object, which represents a user in the application.
   * 	- The `User` object is retrieved from the `userRepository` using the `findByEmail`
   * method, passing in the `username` parameter. If the user is not found, a
   * `UsernameNotFoundException` is thrown.
   * 	- The `UserMapper` class is used to map the `User` object to a `UserDto` object,
   * which includes only the relevant attributes for the application.
   */
  public UserDto getUserDetailsByUsername(String username) {
    com.myhome.domain.User user = userRepository.findByEmail(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    return userMapper.userToUserDto(user);
  }
}
