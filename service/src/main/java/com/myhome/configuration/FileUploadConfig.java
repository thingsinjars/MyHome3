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

import javax.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

/**
 * configures a `MultipartConfig` object with maximum file and request size limits
 * in kilobytes. The `multipartConfigElement()` method creates a `MultipartConfig`
 * instance with set maximum file and request sizes, and returns it as a `MultipartConfigElement`.
 */
@Configuration
public class FileUploadConfig {

  @Value("${files.maxSizeKBytes}")
  private int maxSizeKBytes;

  /**
   * creates a `MultipartConfig` object with customized maximum file and request sizes,
   * which can be used to configure the multipart request handler in Spring.
   * 
   * @returns a `MultipartConfig` instance with configured maximum file and request
   * sizes in kilobytes.
   * 
   * * `MultipartConfigFactory`: This is the class that is used to create the
   * `MultipartConfig` object.
   * * `setMaxFileSize()` and `setMaxRequestSize()`: These two methods define the maximum
   * file size and maximum request size, respectively, in kilobytes (KB). The values
   * are set using `DataSize.ofKilobytes()` method, which is a part of Spring Boot's
   * built-in `DataSize` class.
   * * `createMultipartConfig()`: This method creates a new instance of the `MultipartConfig`
   * class and returns it as output.
   */
  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize(DataSize.ofKilobytes(maxSizeKBytes));
    factory.setMaxRequestSize(DataSize.ofKilobytes(maxSizeKBytes));
    return factory.createMultipartConfig();
  }
}
