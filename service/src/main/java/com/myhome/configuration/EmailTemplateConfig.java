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
 * TODO
 */
@Configuration
@RequiredArgsConstructor
public class EmailTemplateConfig {

  private final EmailTemplateProperties templateProperties;
  private final EmailTemplateLocalizationProperties localizationProperties;

  /**
   * creates a `ResourceBundleMessageSource` instance that retrieves email messages
   * from a resource bundle file. The file path, default locale, and encoding are set
   * using properties from an external `localizationProperties` object. The cache seconds
   * can also be set using the same object.
   * 
   * @returns a ResourceBundleMessageSource instance set with configuration properties
   * for email localization.
   * 
   * 	- `ResourceBundleMessageSource messageSource`: The output is a `ResourceBundleMessageSource`,
   * which means it provides access to message keys in a resource bundle.
   * 	- `setBasename(localizationProperties.getPath())`: The basename of the resource
   * bundle is set to the value of `localizationProperties.getPath()`.
   * 	- `setDefaultLocale(Locale.ENGLISH)`: The default locale for the resource bundle
   * is set to English.
   * 	- `setDefaultEncoding(localizationProperties.getEncoding())`: The default encoding
   * for the resource bundle is set to the value of `localizationProperties.getEncoding()`.
   * 	- `setCacheSeconds(localizationProperties.getCacheSeconds())`: The number of
   * seconds that the resource bundle will be cached is set to the value of `localizationProperties.getCacheSeconds()`.
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
   * creates a new instance of the `SpringTemplateEngine` class and sets its template
   * resolver and message source to the specified values, returning the instance.
   * 
   * @param emailMessageSource message source for email-related messages in the Spring
   * Template Engine.
   * 
   * 1/ ResourceBundleMessageSource: This is an interface that provides a way to retrieve
   * message keys in a resource bundle format. It has methods for getting message keys
   * and their corresponding values.
   * 
   * @returns a Spring Template Engine instance configured to use Thymeleaf templates
   * and email message source.
   * 
   * 	- The `SpringTemplateEngine` instance is created with various configuration options
   * set, including the `templateResolver` and `templateEngineMessageSource`.
   * 	- The `templateResolver` sets the template engine's resolver to `thymeleafTemplateResolver`,
   * which allows for the use of Thymeleaf templates.
   * 	- The `templateEngineMessageSource` sets the message source for the template
   * engine, in this case an instance of `ResourceBundleMessageSource` for handling
   * email-related messages.
   * 
   * The other attributes of the returned output, such as its name or any other
   * configuration options set, are not explicitly mentioned in the provided code snippet.
   */
  @Bean
  public SpringTemplateEngine thymeleafTemplateEngine(ResourceBundleMessageSource emailMessageSource) {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(thymeleafTemplateResolver());
    templateEngine.setTemplateEngineMessageSource(emailMessageSource);
    return templateEngine;
  }

  /**
   * creates a Thymeleaf template resolver that sets the prefix, suffix, template mode,
   * character encoding, and caching based on properties.
   * 
   * @returns a `ITemplateResolver` instance set up to resolve Thymeleaf templates based
   * on properties defined in the `templateProperties` class.
   * 
   * 	- `templateProperties`: This is an instance of `ITemplateProperties`, which
   * contains information about the Thymeleaf template resolver.
   * 	+ `path`: The path to the template file.
   * 	+ `format`: The template file format (e.g., "html", "xml").
   * 	+ `mode`: The template rendering mode (e.g., "HTML", "XML").
   * 	+ `encoding`: The character encoding of the template file.
   * 	+ `cacheable`: A boolean indicating whether the template is cacheable.
   * 
   * The properties are set using the `setPrefix`, `setSuffix`, `setTemplateMode`, and
   * `setCharacterEncoding` methods, respectively. Additionally, the `setCacheable`
   * method is used to indicate whether the template is cacheable or not.
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
