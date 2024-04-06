package com.myhome.configuration.properties.mail;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * provides a set of properties for configuring email template settings, including
 * the path to the template file, the format of the template, and caching options.
 * Fields:
 * 	- path (String): represents a file path where an email template resides.
 * 	- format (String): in the EmailTemplateProperties class is a string value specifying
 * the template file format for sending emails.
 * 	- encoding (String): in the EmailTemplateProperties class represents a string
 * value specifying the character encoding of the email template.
 * 	- mode (String): in the EmailTemplateProperties class represents the encoding
 * mode of the email template.
 * 	- cache (boolean): in EmailTemplateProperties is a boolean value indicating whether
 * the email template data should be cached for future use.
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
