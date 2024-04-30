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

package com.myhome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * is a Spring Boot application that provides a password encoder using BCrypt algorithm.
 * The class also includes a main method for launching the application and a configuration
 * property scanning mechanism.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class MyHomeServiceApplication {

  /**
   * starts a Spring application instance of `MyHomeServiceApplication`.
   * 
   * @param args command-line arguments passed to the application when it is launched.
   * 
   * 	- The `String[]` argument `args` represents an array of command-line arguments
   * passed to the application.
   * 	- Each element in the array is a string that represents a command-line option or
   * argument.
   * 	- The exact meaning and usage of each option or argument depends on the application
   * and its configuration.
   */
  public static void main(String[] args) {
    SpringApplication.run(MyHomeServiceApplication.class, args);
  }

  /**
   * retrieves a `BCryptPasswordEncoder` instance to encrypt passwords securely using
   * a bcrypt algorithm.
   * 
   * @returns a BCryptPasswordEncoder instance, which is a cryptographic hash function
   * for password storage.
   * 
   * 	- The `BCryptPasswordEncoder` object is an implementation of the `PasswordEncoder`
   * interface in Java, which enables password hashing using the Bcrypt algorithm.
   * 	- The `BCryptPasswordEncoder` class provides methods for encrypting and decrypting
   * passwords securely.
   * 	- The returned encoder object can be used to hash and verify passwords in a secure
   * manner, protecting against various types of attacks such as brute-force attacks
   * and dictionary attacks.
   */
  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
