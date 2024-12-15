package com.myhome.configuration.properties.mail;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * provides a set of properties for configuring email template settings such as file
 * path, template format, encoding, and caching options.
 * Fields:
 * 	- path (String): in the EmailTemplateProperties class represents the file path
 * where an email template resides.
 * 	- format (String): represents the template file format for sending emails.
 * 	- encoding (String): is a string value representing the character encoding of the
 * email template.
 * 	- mode (String): specifies the encoding mode of an email template.
 * 	- cache (boolean): in the EmailTemplateProperties class is a boolean value
 * indicating whether the email template data should be cached for future use.
 */
@Data
@Component
@ConfigurationProperties(prefix = "email.template")
public class EmailTemplateProperties {
  private String path;
  private String format;
  private String encoding;
  private String mode;
  private boolean cache;
}
