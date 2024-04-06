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

package com.myhome.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * TODO
 */
@Configuration
public class CorsConfig {

  @Value("${server.cors.allowedOrigins}")
  private String[] allowedOrigins;

  /**
   * defines CORS mappings for a web application, allowing requests from specified
   * origins, methods, headers, and credentials.
   * 
   * @returns a set of CORS mappings that allow requests from any origin and specify
   * which methods, headers, and credentials are allowed.
   * 
   * 	- `allowedOrigins`: An array of allowed origins for CORS requests. In this case,
   * it is empty, indicating that any origin can make a request to the server.
   * 	- `allowedMethods`: An array of allowed HTTP methods for CORS requests. This is
   * set to "*" to allow all methods.
   * 	- `allowedHeaders`: An array of allowed headers for CORS requests. This is also
   * set to "*" to allow all headers.
   * 	- `exposedHeaders`: An array of headers that are exposed in the response. In this
   * case, two headers are exposed: "token" and "userId".
   * 	- `allowCredentials`: A boolean value indicating whether credentials (e.g.,
   * cookies, authentication tokens) should be allowed for CORS requests. This is set
   * to true to allow credentials.
   */
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      /**
       * adds CORS mappings to a registry, allowing requests from any origin and specifying
       * which methods, headers, and credentials are allowed.
       * 
       * @param registry Cors registry that the method adds mappings to.
       * 
       * 	- `registry`: This is an instance of the `CorsRegistry` class, which represents
       * a collection of CORS configuration mappings for a server.
       * 	- `addMapping`: This method is used to add a new CORS mapping to the registry.
       * The method takes a string parameter representing the URL path that the mapping
       * applies to.
       * 	- `allowedOrigins`: An array of strings representing the origins (domains or IP
       * addresses) that are allowed to make requests to the server.
       * 	- `allowedMethods`: An array of strings representing the HTTP methods (such as
       * GET, POST, PUT, DELETE, etc.) that are allowed for the mapping.
       * 	- `allowedHeaders`: An array of strings representing the HTTP headers that are
       * allowed for the mapping.
       * 	- `exposedHeaders`: An array of strings representing the HTTP headers that are
       * exposed to clients in responses.
       * 	- `allowCredentials`: A boolean value indicating whether the CORS configuration
       * allows credentials (such as cookies or authorized access tokens) in requests.
       */
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("*")
            .allowedHeaders("*")
            .exposedHeaders("token", "userId")
            .allowCredentials(true);
      }
    };
  }
}
