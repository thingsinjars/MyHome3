package com.myhome.configuration.properties.mail;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * is a Spring Boot configuration class that defines properties for email localization,
 * including path to the template file, encoding, and cache seconds.
 * Fields:
 * 	- path (String): in the EmailTemplateLocalizationProperties class represents a
 * string value specifying the location of an email template file.
 * 	- encoding (String): in the EmailTemplateLocalizationProperties class is used to
 * specify the character encoding of the email template files.
 * 	- cacheSeconds (int): in the EmailTemplateLocalizationProperties class represents
 * the number of seconds that an email template will be cached before it is refreshed
 * from the source location.
 */
@Data
@Component
@ConfigurationProperties(prefix = "email.location")
public class EmailTemplateLocalizationProperties {
  private String path;
  private String encoding;
  private int cacheSeconds;
}
