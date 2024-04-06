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
 * TODO
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class MyHomeServiceApplication {

  /**
   * runs a Spring application instance of `MyHomeServiceApplication`.
   * 
   * @param args command-line arguments passed to the `SpringApplication.run()` method
   * when executing the application.
   * 
   * 	- `args`: An array of strings representing command-line arguments passed to the
   * application. Each string in the array represents an individual argument.
   */
  public static void main(String[] args) {
    SpringApplication.run(MyHomeServiceApplication.class, args);
  }

  /**
   * returns a `BCryptPasswordEncoder` instance, which is a widely-used password hashing
   * algorithm that provides strong security against brute force attacks.
   * 
   * @returns a BCryptPasswordEncoder object, which is used to encrypt passwords securely.
   * 
   * The PasswordEncoder object returned is an instance of BCryptPasswordEncoder, which
   * is a secure password hashing algorithm. This class provides password encryption
   * using the bcrypt algorithm, which is more secure than the traditional MD5 or SHA-256
   * hash functions. It also includes features like salt generation and incremental
   * hashing to improve security.
   * 
   * The BCryptPasswordEncoder instance returned can be used to encrypt passwords by
   * calling its encode() method, passing in the password to be encrypted as a string
   * argument. The resulting encoded password is a secure representation of the original
   * password that can be stored or compared for authentication purposes.
   */
  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
