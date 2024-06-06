package com.myhome.configuration.properties.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * provides configuration properties for email settings, including host, username,
 * password, port, protocol, debug, and dev mode.
 * Fields:
 * 	- host (String): in the MailProperties class represents the mail server's hostname.
 * 	- username (String): in MailProperties represents a string value representing the
 * username for email authentication purposes.
 * 	- password (String): in MailProperties is of type String.
 * 	- port (int): in MailProperties represents an integer value indicating the mail
 * server's port number for communication.
 * 	- protocol (String): in the MailProperties class represents a string value
 * specifying the email transport protocol to use for sending emails.
 * 	- debug (boolean): in MailProperties is a boolean indicating whether debugging
 * mode is enabled for the mail server configuration.
 * 	- devMode (boolean): in MailProperties represents a boolean flag indicating whether
 * the mail server configuration is intended for development or production use.
 */
@Data
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {
  private String host;
  private String username;
  private String password;
  private int port;
  private String protocol;
  private boolean debug;
  private boolean devMode;
}

