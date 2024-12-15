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

import com.myhome.security.filters.CommunityAuthorizationFilter;
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import com.myhome.services.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.Filter;

/**
 * configures security features for an API, including CORS and CSFR disablement,
 * session management, and authorization rules based on URL paths and HTTP methods.
 * It also adds filters to authorize requests and enables authenticated access to the
 * API using the JWT encoder/decoder.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {
  private final Environment environment;
  private final UserDetailsService userDetailsService;
  private final CommunityService communityService;
  private final PasswordEncoder passwordEncoder;
  private final AppJwtEncoderDecoder appJwtEncoderDecoder;

  /**
   * configures the security settings for an application, disabling CSRF and frame
   * options, and enabling stateless session management. It also adds filters to authorize
   * requests based on specific URL patterns and authentication methods.
   * 
   * @param http HTTP security configuration object, which is used to configure various
   * security features such as CORS, CSFR, session management, and authorization.
   * 
   * * `cors()` enables Cross-Origin Resource Sharing (CORS) functionality.
   * * `csrf()` disables Cross-Site Request Forgery (CSRF) protection.
   * * `headers()` allows for the manipulation of HTTP headers.
   * * `frameOptions()` disables the display of frame options in the browser.
   * * `sessionManagement()` configures session creation policies, with a policy of `STATELESS`.
   * * `addFilterAfter()` adds a filter after the specified filter.
   * * `getCommunityFilter()` returns a filter instance.
   * 
   * Note that `http` is not explicitly mentioned in the code snippet provided.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable();
    http.headers().frameOptions().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilterAfter(getCommunityFilter(), MyHomeAuthorizationFilter.class);

    http.authorizeRequests()
        .antMatchers(environment.getProperty("api.public.h2console.url.path"))
        .permitAll()
        .antMatchers(environment.getProperty("api.public.actuator.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.POST, environment.getProperty("api.public.registration.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.POST, environment.getProperty("api.public.login.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.OPTIONS, environment.getProperty("api.public.cors.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.GET, environment.getProperty("api.public.confirm-email.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.GET, environment.getProperty("api.public.resend-confirmation-email.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.POST, environment.getProperty("api.public.confirm-email.url.path"))
        .permitAll()
        .antMatchers("/swagger/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilter(new MyHomeAuthorizationFilter(authenticationManager(), environment,
            appJwtEncoderDecoder))
        .addFilterAfter(getCommunityFilter(), MyHomeAuthorizationFilter.class);
  }

  /**
   * creates a `CommunityAuthorizationFilter` instance by combining an `AuthenticationManager`
   * and a `CommunityService`. This filter allows for community-based authorization.
   * 
   * @returns an instance of the `Filter` class, which represents a community authorization
   * filter created through the combination of an authentication manager and a community
   * service.
   * 
   * * The function returns an instance of `Filter`, which is a Java class that represents
   * a filter for processing HTTP requests.
   * * The `Filter` object is created by passing two arguments to its constructor:
   * `authenticationManager()` and `communityService`.
   * * The `authenticationManager()` argument is likely a reference to an instance of
   * `AuthenticationManager`, which is responsible for handling authentication-related
   * tasks in the application.
   * * The `communityService` argument is likely a reference to an instance of
   * `CommunityService`, which provides access to various community-related functionality
   * in the application.
   */
  private Filter getCommunityFilter() throws Exception {
    return new CommunityAuthorizationFilter(authenticationManager(), communityService);
  }

  /**
   * sets up an Authentication Manager by defining user details service and password encoder.
   * 
   * @param auth AuthenticationManagerBuilder object, which is used to configure various
   * aspects of the authentication process, such as user details service and password
   * encoder.
   * 
   * * `userDetailsService`: The user details service is not explicitly provided in the
   * input. Therefore, it remains uninitialized.
   * * `passwordEncoder`: The password encoder is initialized with a specified value.
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }
}
