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

import com.myhome.security.jwt.AppJwt;
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * TODO
 */
public class MyHomeAuthorizationFilter extends BasicAuthenticationFilter {

  private final Environment environment;
  private final AppJwtEncoderDecoder appJwtEncoderDecoder;

  public MyHomeAuthorizationFilter(
      AuthenticationManager authenticationManager,
      Environment environment,
      AppJwtEncoderDecoder appJwtEncoderDecoder) {
    super(authenticationManager);
    this.environment = environment;
    this.appJwtEncoderDecoder = appJwtEncoderDecoder;
  }

  /**
   * filters incoming HTTP requests based on an authorization token found in the request
   * header. If the token is present and does not start with a prefix specified in
   * environment variables, it proceeds to the next stage of filtering. Otherwise, it
   * sets an authentication token using getAuthentication() and then proceeds to the
   * next stage of filtering.
   * 
   * @param request HTTP request that is being processed by the filter.
   * 
   * 	- `authHeaderName`: String property that represents the name of the HTTP header
   * field containing the authentication token.
   * 	- `authHeaderPrefix`: String property that represents the prefix of the authentication
   * token in the HTTP header field.
   * 	- `authHeader`: String property that contains the value of the authentication
   * token in the HTTP header field, which may or may not start with the specified prefix.
   * 	- `request`: The original HTTP request object, which is deserialized and passed
   * through the filter chain for further processing.
   * 
   * @param response response object that is being filtered by the chain of filters in
   * the `doFilterInternal()` method.
   * 
   * 	- `HttpServletResponse response`: This is an instance of the `HttpServletResponse`
   * class, which provides information about the HTTP request and response, including
   * headers, status codes, and other metadata.
   * 	- `IOException IOException`: This is a subclass of `Throwable` that represents
   * an error occurring during input/output operations, such as network connections or
   * file I/O.
   * 	- `ServletException ServletException`: This is a subclass of `Throwable` that
   * represents an error occurring during the processing of an HTTP request by a servlet
   * container, typically related to handling HTTP requests and responses.
   * 
   * @param chain 3rd party filter chain that the current filter will execute, allowing
   * the current filter to perform additional processing before the next filter in the
   * chain.
   * 
   * 	- `request`: The original HTTP request object that triggered the filter chain execution.
   * 	- `response`: The HTTP response object that the filter chain is processing.
   * 	- `FilterChain`: An instance of the `FilterChain` interface, representing the
   * sequence of filters that need to be executed for this request.
   * 	- `getAuthentication()`: A method that retrieves an authentication token from the
   * `request` object using the specified `authHeaderName` and `authHeaderPrefix`.
   * 	- `SecurityContextHolder`: A class that provides a mechanism for storing and
   * retrieving security-related information, including the current authentication token.
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    String authHeaderName = environment.getProperty("authorization.token.header.name");
    String authHeaderPrefix = environment.getProperty("authorization.token.header.prefix");

    String authHeader = request.getHeader(authHeaderName);
    if (authHeader == null || !authHeader.startsWith(authHeaderPrefix)) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

  /**
   * retrieves an authentication token from a request header and decodes it to obtain
   * a user ID. It then creates a `UsernamePasswordAuthenticationToken` instance with
   * the user ID and no credentials.
   * 
   * @param request HTTP request that is being processed and provides the authorization
   * token header value.
   * 
   * 	- `getHeader()` method is called on the `HttpServletRequest` object to retrieve
   * an HTTP header value. The property name returned by this method is specified as
   * an environment property (`"authorization.token.header.name"`).
   * 	- The retrieved header value is stored in a variable named `authHeader`.
   * 	- A substring of the `authHeader` value is extracted using string manipulation,
   * specifically replacing a prefix specified by another environment property
   * (`"authorization.token.header.prefix"`). This results in a shorter token value.
   * 	- The decoded JWT token contained within the `authHeader` value is retrieved using
   * an instance of the `appJwtEncoderDecoder` class and the `decode()` method.
   * 	- The `Jwt` object returned by this method contains a `userId` property, which
   * is then used to create a new `UsernamePasswordAuthenticationToken` instance. This
   * token has no credentials (i.e., an empty list ofPrincipal and Credentials).
   * 
   * @returns a `UsernamePasswordAuthenticationToken` instance containing the user ID
   * and an empty list of roles.
   * 
   * 	- `usernamePasswordAuthenticationToken`: This is an instance of the
   * `UsernamePasswordAuthenticationToken` class, which represents a user authentication
   * token containing the user ID and a password.
   * 	- `userID`: This property is of type `Long`, representing the unique identifier
   * of the user who made the request.
   * 	- `password`: This property is of type `String`, representing the password
   * associated with the user ID.
   * 	- `authorizationHeader`: This property is of type `String`, representing the
   * authorization header received in the HTTP request.
   * 	- `tokenSecret`: This property is of type `String`, representing the secret key
   * used to decode the JWT token.
   * 
   * Overall, the `getAuthentication` function returns a user authentication token
   * containing the user ID and password, which can be used for authentication purposes.
   */
  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String authHeader =
        request.getHeader(environment.getProperty("authorization.token.header.name"));
    if (authHeader == null) {
      return null;
    }

    String token =
        authHeader.replace(environment.getProperty("authorization.token.header.prefix"), "");
    AppJwt jwt = appJwtEncoderDecoder.decode(token, environment.getProperty("token.secret"));

    if (jwt.getUserId() == null) {
      return null;
    }
    return new UsernamePasswordAuthenticationToken(jwt.getUserId(), null, Collections.emptyList());
  }
}
