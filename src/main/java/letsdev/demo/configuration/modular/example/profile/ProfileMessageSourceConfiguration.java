package letsdev.demo.configuration.modular.example.profile;

import jakarta.annotation.PostConstruct;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ProfileMessageSourceConfiguration {

    private final MessageSource messageSource;

    public ProfileMessageSourceConfiguration(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostConstruct
    public void profileMessageSource() {
        if (messageSource instanceof ReloadableResourceBundleMessageSource source) {
            source.addBasenames("classpath:validation/profile/profile-validation-message");
        }
    }
}
