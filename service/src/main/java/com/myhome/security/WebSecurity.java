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
 * TODO
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
   * sets up security features for an API, disabling CORS and CSFR, and enforcing
   * stateful session management. It also adds filters to authorize requests based on
   * specific URLs and HTTP methods, and enables authenticated access to the API.
   * 
   * @param http HTTP security configuration object, which is used to configure various
   * security features such as CORS, CSFR, session management, and authorization rules
   * for different URL paths.
   * 
   * 	- `cors()` - Enables Cross-Origin Resource Sharing (CORS) functionality.
   * 	- `csrf()`. disable() - Disables Cross-Site Request Forgery (CSRF) protection.
   * 	- `headers().frameOptions().disable()` - Disables the Frame option for HTTP headers.
   * 	- `sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)` -
   * Sets the session creation policy to stateless, which means that no sessions will
   * be created.
   * 	- `addFilterAfter(getCommunityFilter(), MyHomeAuthorizationFilter.class)` - Adds
   * a filter after the community filter.
   * 	- `authorizeRequests()` - Authorizes requests based on various matchers and allows
   * or denies them accordingly. The matchers include:
   * 	+ `antMatchers(environment.getProperty("api.public.h2console.url.path"))` - Matches
   * URLs with the given path.
   * 	+ `permitAll()` - Allows all requests without any restrictions.
   * 	+ `antMatchers(environment.getProperty("api.public.actuator.url.path"))` - Matches
   * URLs with the given path.
   * 	+ `permitAll()` - Allows all requests without any restrictions.
   * 	+ `antMatchers(HttpMethod.POST, environment.getProperty("api.public.registration.url.path"))`
   * - Matches POST requests to the given URL.
   * 	+ `permitAll()` - Allows all POST requests without any restrictions.
   * 	+ `antMatchers(HttpMethod.POST, environment.getProperty("api.public.login.url.path"))`
   * - Matches POST requests to the given URL.
   * 	+ `permitAll()` - Allows all POST requests without any restrictions.
   * 	+ `antMatchers(HttpMethod.OPTIONS, environment.getProperty("api.public.cors.url.path"))`
   * - Matches OPTIONS requests to the given URL.
   * 	+ `permitAll()` - Allows all OPTIONS requests without any restrictions.
   * 	+ `antMatchers(HttpMethod.GET, environment.getProperty("api.public.confirm-email.url.path"))`
   * - Matches GET requests to the given URL.
   * 	+ `permitAll()` - Allows all GET requests without any restrictions.
   * 	+ `antMatchers(HttpMethod.GET, environment.getProperty("api.public.resend-confirmation-email.url.path"))`
   * - Matches GET requests to the given URL.
   * 	+ `permitAll()` - Allows all GET requests without any restrictions.
   * 	+ `antMatchers(HttpMethod.POST, environment.getProperty("api.public.confirm-email.url.path"))`
   * - Matches POST requests to the given URL.
   * 	+ `permitAll()` - Allows all POST requests without any restrictions.
   * 	+ `/swagger/<any>` - Matches any Swagger request.
   * 	+ `<any>` - Matches any other request.
   * 	- `addFilter(new MyHomeAuthorizationFilter(authenticationManager(), environment,
   * appJwtEncoderDecoder))` - Adds a filter to authorize requests based on the JWT encoder/decoder.
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
   * creates a `CommunityAuthorizationFilter` instance, using the `authenticationManager()`
   * and `communityService` objects provided as parameters. This filter will likely
   * restrict access to certain community-related resources based on user authentication.
   * 
   * @returns a `Filter` object implementing community authorization logic.
   * 
   * 	- The `getCommunityFilter` function returns an instance of the `Filter` class.
   * 	- The `Filter` object is constructed using the `AuthenticationManager` and
   * `CommunityService` objects as parameters.
   * 	- The `AuthenticationManager` object represents the authentication mechanism used
   * to authenticate users, while the `CommunityService` object manages the communities
   * in the system.
   */
  private Filter getCommunityFilter() throws Exception {
    return new CommunityAuthorizationFilter(authenticationManager(), communityService);
  }

  /**
   * configures authentication settings by providing a user details service and password
   * encoder to an AuthenticationManagerBuilder instance.
   * 
   * @param auth AuthenticationManagerBuilder object, which is being configured by
   * setting the userDetailsService and passwordEncoder properties.
   * 
   * 	- `userDetailsService`: This is an instance of `UserDetailsService`, which provides
   * methods for retrieving and manipulating user details.
   * 	- `passwordEncoder`: This is an instance of `PasswordEncoder`, which encodes
   * passwords securely.
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }
}
