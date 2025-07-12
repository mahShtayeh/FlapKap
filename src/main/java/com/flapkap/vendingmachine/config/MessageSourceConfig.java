package com.flapkap.vendingmachine.config;

import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * I18n message support configuration
 *
 * @author Mahmoud Shtayeh
 */
@Configuration
@NoArgsConstructor
public class MessageSourceConfig {
    /**
     * Messages resource bundle base name
     */
    public static final String BASE_NAME = "classpath:messages/messages";

    /**
     * Messages files default encoding
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Message source bean
     *
     * @return ReloadableResourceBundleMessageSource
     */
    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(BASE_NAME);
        messageSource.setDefaultEncoding(DEFAULT_ENCODING);
        messageSource.setCacheSeconds(3600); // Refresh cache once per hour
        return messageSource;
    }

    /**
     * Local resolver using Http Headers
     *
     * @return AcceptHeaderLocaleResolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        final AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US); // Default to English (US)
        return localeResolver;
    }

    /**
     * Validator factory bean
     *
     * @param messageSource Configured message source
     * @return LocalValidatorFactoryBean
     */
    @Bean
    public LocalValidatorFactoryBean validator(final MessageSource messageSource) {
        final LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}