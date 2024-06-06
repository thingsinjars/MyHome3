package com.myhome.configuration.properties.mail;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * defines properties for email localization including path to a template file,
 * encoding, and cache seconds.
 * Fields:
 * 	- path (String): in the EmailTemplateLocalizationProperties class represents a
 * string value specifying the location of an email template file.
 * 	- encoding (String): in the EmailTemplateLocalizationProperties class represents
 * a string value specifying the character encoding of email template files.
 * 	- cacheSeconds (int): in the EmailTemplateLocalizationProperties class represents
 * the number of seconds an email template will be cached before being refreshed from
 * its source location.
 */
@Data
@Component
@ConfigurationProperties(prefix = "email.location")
public class EmailTemplateLocalizationProperties {
  private String path;
  private String encoding;
  private int cacheSeconds;
}
