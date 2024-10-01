package letsdev.demo.configuration.modular.example.auth;

import jakarta.annotation.PostConstruct;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class AuthMessageSourceConfiguration {

    private final MessageSource messageSource;

    public AuthMessageSourceConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 각 모듈에서
     */
    @PostConstruct
    public void authMessageSource() {
        if (messageSource instanceof ReloadableResourceBundleMessageSource source) {
            source.addBasenames("classpath:validation/auth/auth-validation-message");
        }
    }
}
