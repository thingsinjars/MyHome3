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
 * TODO
 */
@Configuration
public class FileUploadConfig {

  @Value("${files.maxSizeKBytes}")
  private int maxSizeKBytes;

  /**
   * creates a `MultipartConfig` object, setting limits on maximum file size and request
   * size.
   * 
   * @returns a `MultipartConfig` object configured with maximum file and request sizes
   * in kilobytes.
   * 
   * 	- The MultipartConfigFactory object is created with the `setMaxFileSize()` and
   * `setMaxRequestSize()` methods, which set the maximum file size in kilobytes (KB)
   * and request size in KB, respectively.
   * 	- The createMultipartConfig() method returns a newly created MultipartConfig instance.
   * 	- The MultipartConfig instance has several attributes, including the maximum file
   * size, maximum request size, and the number of files that can be uploaded simultaneously.
   */
  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize(DataSize.ofKilobytes(maxSizeKBytes));
    factory.setMaxRequestSize(DataSize.ofKilobytes(maxSizeKBytes));
    return factory.createMultipartConfig();
  }
}
