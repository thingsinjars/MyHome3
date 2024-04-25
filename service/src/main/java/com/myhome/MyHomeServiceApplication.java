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
 * is a Spring Boot application that provides a `BCryptPasswordEncoder` instance for
 * encrypting passwords using the BCrypt algorithm. The encoder is designed to handle
 * strong password validation and is suitable for use in web applications.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class MyHomeServiceApplication {

  /**
   * runs a SpringApplication with the `MyHomeServiceApplication` class as its argument,
   * launching the application with the given command-line arguments.
   * 
   * @param args command-line arguments passed to the main method of the `SpringApplication`.
   * 
   * The `String[] args` represents an array of command-line arguments passed to the
   * application during launching.
   */
  public static void main(String[] args) {
    SpringApplication.run(MyHomeServiceApplication.class, args);
  }

  /**
   * returns a `BCryptPasswordEncoder` instance, which is used to encrypt passwords
   * using the BCrypt algorithm.
   * 
   * @returns a BCryptPasswordEncoder instance.
   * 
   * The `BCryptPasswordEncoder` object represents a password encryption algorithm that
   * uses the bcrypt hashing algorithm to securely store and hash passwords.
   * It provides several methods for encrypting, hashing, and verifying passwords.
   * The encoder is designed to handle strong password validation and is suitable for
   * use in web applications.
   */
  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
