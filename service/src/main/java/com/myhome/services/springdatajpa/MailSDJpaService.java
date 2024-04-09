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
 * TODO
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
   * sends a password recovery email to a user with a randomly generated code.
   * 
   * @param user user for whom the password recovery code is being generated and sent.
   * 
   * 	- `user.getName()` represents the user's name.
   * 	- `randomCode` is a string parameter representing the random code sent to the
   * user for password recovery.
   * 
   * @param randomCode 6-digit recovery code sent to the user's email address for
   * password reset.
   * 
   * 	- `randomCode`: A String that represents a unique code sent to the user for
   * password recovery.
   * 
   * The function first creates a `Map<String, Object>` template model with two key-value
   * pairs: `username` and `recoverCode`. These values are then used to construct an
   * email subject and message using the `send` function. The `mailSent` boolean value
   * is returned as the function result.
   * 
   * @returns a boolean value indicating whether an email with a password recovery code
   * was successfully sent to the user's registered email address.
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
   * maps user information to a template model and sends an email to the user's email
   * address with the subject "locale.EmailSubject.passwordChanged".
   * 
   * @param user user whose password has been successfully changed, providing the
   * necessary information to craft the email notification.
   * 
   * 	- `name`: A `String` property representing the user's name.
   * 	- `email`: An `EmailAddress` property representing the user's email address.
   * 
   * The function creates a `Map` called `templateModel`, which contains a single entry
   * with the key being `username` and the value being the `name` of the user. The
   * function then sends an email using the `send` method, passing in the user's email
   * address and a customized subject derived from a localized message using the
   * `getLocalizedMessage` method. The email is sent using the
   * `MailTemplatesNames.PASSWORD_CHANGED.filename` template file name.
   * 
   * The function returns a boolean value indicating whether the email was successfully
   * sent.
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
   * maps user and security token data to a message template and sends an email
   * confirmation link to the user's registered email address.
   * 
   * @param user user whose account is being created and confirmed.
   * 
   * 	- `user`: The user object containing the name and email fields.
   * 
   * The function first creates a map of template model objects, where the `username`
   * field is set to the user's name and the `emailConfirmLink` field is set to the
   * link for confirming the account creation via email. Then, it sends an email with
   * the subject "locale.EmailSubject.accountCreated" using the
   * `MailTemplatesNames.ACCOUNT_CREATED.filename` template file, passing in the
   * `user.getEmail()` address and the map of template model objects as parameters.
   * Finally, it returns a boolean value indicating whether the email was sent successfully
   * or not.
   * 
   * @param emailConfirmToken email confirmation token for the created account, which
   * is used to verify the user's email address during the account confirmation process.
   * 
   * 	- `user`: A `User` object representing the user whose account was created.
   * 	- `securityToken`: A unique token generated by the server for email confirmation.
   * 
   * The `templateModel` is created with two properties:
   * 
   * 	- `username`: The username of the newly created account.
   * 	- `emailConfirmLink`: The URL of the email confirmation page.
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
   * sends an email to a user with a subject containing information about their account
   * being confirmed.
   * 
   * @param user User object containing the user's information that is being confirmed.
   * 
   * 	- `username`: A string representing the user's name.
   * 
   * The function then proceeds to create and send an email using the `send` method,
   * with the subject generated from a localized message ("locale.EmailSubject.accountConfirmed").
   * The template filename is retrieved from the `MailTemplatesNames.ACCOUNT_CONFIRMED`
   * constant, and the `templateModel` map contains the user's name as its single entry.
   * Finally, the function returns a boolean value indicating whether the email was
   * sent successfully.
   * 
   * @returns a boolean value indicating whether an email was sent successfully to the
   * user's registered email address.
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
   * creates an email message using a `MimeMessage` object and sends it to a specified
   * recipient with a customized subject and body.
   * 
   * @param to email address of the recipient to whom the HTML message will be sent.
   * 
   * 	- `to`: The String representing the recipient's email address. It can be an
   * individual email address or an email address range separated by commas (e.g., "john@example.com,mary@example.com").
   * 	- `subject`: The String representing the email subject line. It is a concise
   * description of the message's content.
   * 	- `htmlBody`: The String containing the HTML body of the message. It can include
   * formatting, images, links, and other elements to enhance the message's presentation.
   * 
   * @param subject subject line of the email to be sent.
   * 
   * 	- `to`: The email address of the recipient.
   * 	- `subject`: A string that represents the subject line of the email. It can contain
   * various attributes such as the recipient's name, a brief description of the message,
   * and any formatting options.
   * 	- `htmlBody`: The HTML content of the email message.
   * 
   * @param htmlBody HTML content of the message to be sent through email.
   * 
   * 	- `true`: indicates whether the message body is in HTML format
   * 	- `"UTF-8"`: represents the encoding format of the message body
   * 	- `mailProperties.getUsername()`: retrieves the username for sending emails
   * 	- `to`: specifies the recipient's email address
   * 	- `subject`: defines the subject line of the email
   * 	- `htmlBody`: contains the HTML content of the message
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
   * takes an email recipient (`emailTo`), subject, template name, and model as input.
   * It then uses Thymeleaf to process the template and generates an HTML message body.
   * Finally, it sends the message using a mail client.
   * 
   * @param emailTo email address to which the HTML message will be sent.
   * 
   * 	- `emailTo`: This is a string parameter representing the email address to which
   * the message will be sent.
   * 
   * The rest of the code in the function can be executed without modification.
   * 
   * @param subject subject line of the email to be sent.
   * 
   * 	- `String subject`: The subject line of the email to be sent.
   * 	- `emailTo`: The recipient's email address.
   * 	- `templateName`: The name of the Thymeleaf template to be processed for the email
   * body.
   * 	- `templateModel`: A map of Thymeleaf model objects that are passed as variables
   * to the template engine for rendering.
   * 
   * @param templateName name of the Thymeleaf template to be processed and rendered
   * into an HTML message for sending via email.
   * 
   * 	- `String`: The name of the Thymeleaf template to be processed.
   * 	- `Context`: An object that contains variables and other contextual information
   * for the template processing.
   * 	- `LocaleContextHolder`: A class that provides access to the current locale.
   * 	- `emailTo`: The recipient's email address.
   * 	- `subject`: The subject of the email message.
   * 	- `templateModel`: A map containing variables and other data that can be used in
   * the template.
   * 
   * @param templateModel map of data that will be used to populate the email template
   * using Thymeleaf, allowing for dynamic content and formatting.
   * 
   * 	- `LocaleContextHolder`: This is an instance of `LocaleContextHolder`, which
   * manages the locale context for Thymeleaf template engine.
   * 	- `Map<String, Object>`: This is a map containing key-value pairs representing
   * the variables that will be used in the template.
   * 	- `emailTo`: This is a string representing the email address to send the message
   * to.
   * 	- `subject`: This is a string representing the subject of the email.
   * 	- `templateName`: This is a string representing the name of the Thymeleaf template
   * to use for rendering the email message.
   * 
   * The function first creates a `Context` instance using the `LocaleContextHolder`,
   * and sets the variables in the `templateModel` map as its variables. Then, it uses
   * the `emailTemplateEngine` to process the `templateName` with the `thymeleafContext`,
   * and generates an HTML body from the resulting template output. Finally, the function
   * sends the email message using the `sendHtmlMessage` function.
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
   * generates a hyperlink for an email confirmation process for a user. The base URL
   * is constructed from the current context path, and the user's ID and security token
   * are appended to form the full URL.
   * 
   * @param user User object containing information about the user for whom the
   * confirmation link will be generated.
   * 
   * 	- `user`: A `User` object representing the current user whose email confirmation
   * link is being generated.
   * 	- `token`: An instance of `SecurityToken`, which contains a unique token for
   * authenticating the user's account confirmation request.
   * 
   * @param token email confirmation token that is to be linked with the user's account,
   * which is generated by the server-side code and passed as a security token to the
   * client-side code for the purpose of verifying the user's identity.
   * 
   * 	- `token.getToken()`: The value of this property represents the token to be used
   * in the URL for email confirmation.
   * 	- `user.getUserId()`: The value of this property represents the user ID for whom
   * the email confirmation link is being generated.
   * 
   * @returns a URL string containing the base URL and user ID, followed by the email
   * confirmation token.
   * 
   * 	- `baseUrl`: The base URL of the application, which is generated using the
   * `ServletUriComponentsBuilder` class.
   * 	- `userId`: The user ID of the user for whom the email confirmation link is being
   * generated.
   * 	- `token`: The security token generated by the application, which is used to
   * authenticate the user and prevent unauthorized access to the link.
   */
  private String getAccountConfirmLink(User user, SecurityToken token) {
    String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
        .replacePath(null)
        .build()
        .toUriString();
    return String.format("%s/users/%s/email-confirm/%s", baseUrl, user.getUserId(), token.getToken());
  }

  /**
   * retrieves a localized message from a message source based on a property name,
   * handling any exceptions that may occur during the process.
   * 
   * @param prop property key for which a localized message is being retrieved.
   * 
   * 	- `prop`: A string input parameter representing the key for which a localized
   * message is to be retrieved from the message source.
   * 	- `messageSource`: An object that provides localized messages based on the passed
   * key.
   * 	- `LocaleContextHolder`: Holds the current locale context, which is used to
   * retrieve the appropriate message from the message source.
   * 
   * @returns a localized message for a given property, or an error message if there
   * was an exception during localization.
   * 
   * 	- `message`: A string that is generated by calling the `getMessage` method of the
   * `messageSource` object, passing in the `prop` parameter as a string and ignoring
   * any null values.
   * 	- `prop`: The input parameter passed to the function, which represents the key
   * for the message to be localized.
   * 	- `LocaleContextHolder`: A class that provides a way to access the current locale
   * context of the application, which is used to determine the correct message to return.
   * 
   * The function returns a string that contains the localized message for the provided
   * property, or a default message if there is an error in localization.
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
