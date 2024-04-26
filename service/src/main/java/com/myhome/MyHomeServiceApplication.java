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
   * starts the MyHomeServiceApplication by running it using the `SpringApplication.run()`
   * method, passing in the class and argument array as parameters.
   * 
   * @param args 1 or more command line arguments passed to the Java application when
   * it is launched, and are passed to the `SpringApplication.run()` method as an array
   * of string values.
   * 
   * 	- The `String[]` parameter `args` represents an array of strings passed to the
   * application as command-line arguments.
   * 	- It can be destructured into individual string elements using the `String` class
   * methods such as `split()` or `stream().map()`.
   * 	- Each element in the array can be accessed through its index, starting from 0.
   * For example, `args[0]` represents the first argument passed to the application.
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
