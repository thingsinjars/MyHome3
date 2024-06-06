package com.myhome.services.springdatajpa;

import com.myhome.configuration.properties.mail.MailProperties;
import com.myhome.configuration.properties.mail.MailTemplatesNames;
import com.myhome.domain.SecurityToken;
import com.myhome.domain.User;
import com.myhome.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * is a component of the Spring Boot application that handles email communication
 * tasks. It provides methods for sending emails, creating email templates, and
 * confirming user accounts via email. The class uses Thymeleaf to process email
 * templates and send HTML messages through email clients. Additionally, it includes
 * localized message support for retrieving appropriate messages based on property names.
 */
@Service
@ConditionalOnProperty(value = "spring.mail.devMode", havingValue = "false", matchIfMissing = false)
@RequiredArgsConstructor
@Slf4j
public class MailSDJpaService implements MailService {

  private final ITemplateEngine emailTemplateEngine;
  private final JavaMailSender mailSender;
  private final ResourceBundleMessageSource messageSource;
  private final MailProperties mailProperties;

  /**
   * generates a random code and sends an email to the user with a subject containing
   * the password recover information.
   * 
   * @param user User object whose password recovery email should be sent.
   * 
   * * `user.getName()` represents the username of the user for whom the password
   * recovery code is being generated.
   * * `randomCode` is a String input representing the random password recovery code
   * to be sent to the user's registered email address.
   * 
   * @param randomCode 6-digit recovery code sent to the user's registered email address
   * for password reset purposes.
   * 
   * @returns a boolean value indicating whether an email with a password recovery code
   * was successfully sent to the user.
   */
  @Override
  public boolean sendPasswordRecoverCode(User user, String randomCode) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("username", user.getName());
    templateModel.put("recoverCode", randomCode);
    String passwordRecoverSubject = getLocalizedMessage("locale.EmailSubject.passwordRecover");
    boolean mailSent = send(user.getEmail(), passwordRecoverSubject,
        MailTemplatesNames.PASSWORD_RESET.filename, templateModel);
    return mailSent;
  }

  /**
   * maps user data to a template model and sends an email with the subject "password
   * changed" using the `send` method.
   * 
   * @param user User object containing information about the user whose password has
   * been changed successfully.
   * 
   * * `user.getName()` is a string representing the user's name.
   * * `user.getEmail()` is a string representing the user's email address.
   * 
   * @returns a boolean value indicating whether an email was successfully sent to the
   * user's registered email address.
   */
  @Override
  public boolean sendPasswordSuccessfullyChanged(User user) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("username", user.getName());
    String passwordChangedSubject = getLocalizedMessage("locale.EmailSubject.passwordChanged");
    boolean mailSent = send(user.getEmail(), passwordChangedSubject,
        MailTemplatesNames.PASSWORD_CHANGED.filename, templateModel);
    return mailSent;
  }

  /**
   * takes a user and an email confirm token as input, generates a message with the
   * user's name and the email confirm link, and sends it to the user's email address
   * using the `MailTemplatesNames.ACCOUNT_CREATED` template file.
   * 
   * @param user user whose account has been created and is used to retrieve their email
   * address for sending an account confirmation link.
   * 
   * * `username`: The username of the created account, which is extracted from the
   * `user` object's `getName()` method.
   * * `emailConfirmLink`: A string that contains the confirmation link for the newly
   * created email address, generated using the `getAccountConfirmLink()` function.
   * 
   * These two properties are then used as inputs to the `send()` function, which sends
   * an email to the user's registered email address with the subject "locale.EmailSubject.accountCreated".
   * 
   * @param emailConfirmToken email confirmation token that is used to verify the user's
   * email address after creation.
   * 
   * * `User user`: The user who created an account.
   * * `SecurityToken emailConfirmToken`: A token used to confirm the user's email address.
   * 
   * The function first creates a map called `templateModel`, which contains the user's
   * username and the link for confirming their email address. Then, it sends an email
   * with the subject "locale.EmailSubject.accountCreated" using the `send` function.
   * The email is sent to the user's registered email address.
   * 
   * The return value of the function indicates whether the email was successfully sent
   * or not.
   * 
   * @returns a boolean value indicating whether an email was sent successfully to the
   * user's registered email address.
   */
  @Override
  public boolean sendAccountCreated(User user, SecurityToken emailConfirmToken) {
    Map<String, Object> templateModel = new HashMap<>();
    String emailConfirmLink = getAccountConfirmLink(user, emailConfirmToken);
    templateModel.put("username", user.getName());
    templateModel.put("emailConfirmLink", emailConfirmLink);
    String accountCreatedSubject = getLocalizedMessage("locale.EmailSubject.accountCreated");
    boolean mailSent = send(user.getEmail(), accountCreatedSubject,
        MailTemplatesNames.ACCOUNT_CREATED.filename, templateModel);
    return mailSent;
  }

  /**
   * sends an email to a user's registered email address with a subject containing the
   * localized message for "account confirmed". The function returns `true` if the email
   * was sent successfully, or `false` otherwise.
   * 
   * @param user User object containing the user's information that is being checked
   * for account confirmation.
   * 
   * * `Name`: A string representing the user's name.
   * 
   * The function then maps the `user` object to a `Map<String, Object>` template model
   * and uses it to send an email. The mail is sent with the subject
   * "locale.EmailSubject.accountConfirmed" and the file `MailTemplatesNames.ACCOUNT_CONFIRMED.filename`.
   * 
   * @returns a boolean value indicating whether an email was sent to the user's
   * registered email address.
   */
  @Override
  public boolean sendAccountConfirmed(User user) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("username", user.getName());
    String accountConfirmedSubject = getLocalizedMessage("locale.EmailSubject.accountConfirmed");
    boolean mailSent = send(user.getEmail(), accountConfirmedSubject,
        MailTemplatesNames.ACCOUNT_CONFIRMED.filename, templateModel);
    return mailSent;
  }

  /**
   * allows for sending HTML formatted messages through a messaging system, by creating
   * a MimeMessage object, setting its properties and then sending it through the
   * mailSender using the `send` method.
   * 
   * @param to email address of the recipient to whom the HTML message should be sent.
   * 
   * @param subject subject line of an email message that is being sent through the
   * `mailSender` object.
   * 
   * @param htmlBody HTML content of the message that will be sent to the recipient.
   */
  private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setFrom(mailProperties.getUsername());
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlBody, true);
    mailSender.send(message);
  }

  /**
   * takes an email address `emailTo`, subject, template name and a map of template
   * models as parameters. It uses Thymeleaf Engine to process the template and generates
   * an HTML message body, which is then sent via mail using the `sendHtmlMessage`
   * method. If any error occurs during the sending process, it logs the error and
   * returns `false`.
   * 
   * @param emailTo email address of the recipient to whom the email message is to be
   * sent.
   * 
   * @param subject subject line of the email to be sent.
   * 
   * @param templateName name of the Thymeleaf template to be processed and rendered
   * into an HTML message.
   * 
   * @param templateModel map of data that will be used to populate the email template's
   * placeholders, allowing for dynamic and personalized content generation.
   * 
   * * `LocaleContextHolder`: represents the current locale context, which is used to
   * set the locale for Thymeleaf template engine.
   * * `templateName`: specifies the name of the Thymeleaf template to be processed.
   * * `templateModel`: contains the data and logic for rendering a Thymeleaf template.
   * It is deserialized from the input stream and can contain various properties/attributes,
   * such as:
   * 	+ `Map<String, Object>`: a map of key-value pairs that are used to render the
   * template. Each key represents a variable in the template, and each value represents
   * the corresponding value for that variable.
   * 
   * Note: The summary provided is limited to 4 sentences and does not include any
   * information about the code author or licensing.
   * 
   * @returns a boolean value indicating whether the email was sent successfully or not.
   */
  private boolean send(String emailTo, String subject, String templateName, Map<String, Object> templateModel) {
    try {
      Context thymeleafContext = new Context(LocaleContextHolder.getLocale());
      thymeleafContext.setVariables(templateModel);
      String htmlBody = emailTemplateEngine.process(templateName, thymeleafContext);
      sendHtmlMessage(emailTo, subject, htmlBody);
    } catch (MailException | MessagingException mailException) {
      log.error("Mail send error!", mailException);
      return false;
    }
    return true;
  }

  /**
   * generates a hyperlink for email confirmation of a user's account. It takes a user
   * and security token as input, constructing the base URL using the current context
   * path and appending the user ID and token to create the final link.
   * 
   * @param user User object containing information about the user for whom the
   * confirmation link is to be generated.
   * 
   * * `user`: A `User` object containing information about a user, including their ID
   * and other attributes.
   * * `token`: A `SecurityToken` object representing a security token used to authenticate
   * the user.
   * 
   * @param token SecurityToken instance that contains information about the current
   * user and is used to generate the confirmation link for email verification.
   * 
   * * `token`: A `SecurityToken` object containing information about the token, such
   * as its `token` attribute and other attributes.
   * 
   * @returns a URL string representing the email confirmation link for a given user
   * and security token.
   */
  private String getAccountConfirmLink(User user, SecurityToken token) {
    String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .replacePath(null)
        .build()
        .toUriString();
    return String.format("%s/users/%s/email-confirm/%s", baseUrl, user.getUserId(), token.getToken());
  }

  /**
   * retrieves a message from a message source based on a property key, using the current
   * locale. If an exception occurs during message retrieval, a default message is
   * returned instead.
   * 
   * @param prop property key for which the localized message is being retrieved.
   * 
   * @returns a localized message for a given property name.
   */
  private String getLocalizedMessage(String prop) {
    String message = "";
    try {
      message = messageSource.getMessage(prop, null, LocaleContextHolder.getLocale());
    } catch (Exception e) {
      message = prop + ": localization error";
    }
    return message;
  }

}
