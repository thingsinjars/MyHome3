package com.myhome.configuration;

import com.myhome.configuration.properties.mail.EmailTemplateLocalizationProperties;
import com.myhome.configuration.properties.mail.EmailTemplateProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Locale;

/**
 * defines properties for email localization and creates a ResourceBundleMessageSource
 * instance to handle email messages. It also sets up a Spring Template Engine with
 * Thymeleaf template resolver and message source.
 */
@Configuration
@RequiredArgsConstructor
public class EmailTemplateConfig {

  private final EmailTemplateProperties templateProperties;
  private final EmailTemplateLocalizationProperties localizationProperties;

  /**
   * creates a `ResourceBundleMessageSource` instance with configuration parameters set
   * from localization properties.
   * 
   * @returns a `ResourceBundleMessageSource` object configured to handle email-related
   * messages.
   * 
   * * `ResourceBundleMessageSource`: This is the class that represents the message source.
   * * `setBasename()`: Sets the basename of the message source to the value of the
   * `localizationProperties.getPath()` method.
   * * `setDefaultLocale()`: Sets the default locale of the message source to English.
   * * `setDefaultEncoding()`: Sets the default encoding of the message source to the
   * value of the `localizationProperties.getEncoding()` method.
   * * `setCacheSeconds()`: Sets the cache seconds for the message source to the value
   * of the `localizationProperties.getCacheSeconds()` method.
   */
  @Bean
  public ResourceBundleMessageSource emailMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename(localizationProperties.getPath());
    messageSource.setDefaultLocale(Locale.ENGLISH);
    messageSource.setDefaultEncoding(localizationProperties.getEncoding());
    messageSource.setCacheSeconds(localizationProperties.getCacheSeconds());
    return messageSource;
  }

  /**
   * creates a new instance of Spring Template Engine, sets its template resolver and
   * message source, and returns it.
   * 
   * @param emailMessageSource message source for email-related templates, providing
   * messages related to emails to the Spring Template Engine.
   * 
   * * `ResourceBundleMessageSource`: This is a message source that provides messages
   * in a resource bundle format. It has various attributes such as `basenames`,
   * `defaultBasename`, and `fallbackFactory`. These attributes enable the message
   * source to retrieve messages from a resource bundle and provide them to the template
   * engine.
   * 
   * @returns a Spring Template Engine instance with Thymeleaf template resolver and
   * email message source.
   * 
   * The `SpringTemplateEngine` instance is set with a `templateResolver`, which is an
   * object of type `ThymeleafTemplateResolver`. This resolver provides access to
   * Thymeleaf-specific template engine features.
   * 
   * A `MessageSource` object, which is responsible for providing message keys and their
   * associated messages, is also set as the `templateEngineMessageSource` property.
   * In this case, it is an instance of `ResourceBundleMessageSource`.
   */
  @Bean
  public SpringTemplateEngine thymeleafTemplateEngine(ResourceBundleMessageSource emailMessageSource) {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(thymeleafTemplateResolver());
    templateEngine.setTemplateEngineMessageSource(emailMessageSource);
    return templateEngine;
  }

  /**
   * creates a Thymeleaf Template Resolver, setting properties such as prefix, suffix,
   * mode, encoding, and caching based on template properties.
   * 
   * @returns a `ITemplateResolver` object configured to resolve Thymeleaf templates
   * based on specified properties.
   * 
   * * `ClassLoaderTemplateResolver`: The type of template resolver used to resolve templates.
   * * `templatePath`: The path to the Thymeleaf template file.
   * * `fileSeparator`: The separator used in the file system.
   * * `templateProperties`: The properties of the Thymeleaf template, such as mode,
   * format, encoding, and cacheability.
   * * `setPrefix()`: Sets the prefix for the resolved template path.
   * * `setSuffix()`: Sets the suffix for the resolved template path.
   * * `setTemplateMode()`: Sets the mode of the Thymeleaf template.
   * * `setCharacterEncoding()`: Sets the character encoding used for the resolved template.
   * * `setCacheable()`: Indicates whether the resolved template should be cached or not.
   */
  private ITemplateResolver thymeleafTemplateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

    String templatePath = templateProperties.getPath();
    String fileSeparator = System.getProperty("file.separator");
    templateResolver.setPrefix(templatePath.endsWith(fileSeparator) ? templatePath : templatePath + fileSeparator);

    templateResolver.setSuffix(templateProperties.getFormat());
    templateResolver.setTemplateMode(templateProperties.getMode());
    templateResolver.setCharacterEncoding(templateProperties.getEncoding());
    templateResolver.setCacheable(templateProperties.isCache());
    return templateResolver;
  }

}
