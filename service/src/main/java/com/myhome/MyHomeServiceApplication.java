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
 * It also includes a main method for launching the application and configuration
 * properties scan to enable the auto-discovery of spring boot configurations.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class MyHomeServiceApplication {

  /**
   * runs a Spring application, specifically the `MyHomeServiceApplication`, passing
   * the `args` array as a parameter to the `SpringApplication.run()` method.
   * 
   * @param args command-line arguments passed to the program when it is executed
   * directly from the command line or launched by some other application.
   * 
   * The input `args` is an array of strings that contains the command-line arguments
   * passed to the program by the user.
   * 
   * Each element in the array represents a separate argument, and can be accessed using
   * its index position within the array. For example, `args[0]` represents the first
   * argument passed to the program.
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
