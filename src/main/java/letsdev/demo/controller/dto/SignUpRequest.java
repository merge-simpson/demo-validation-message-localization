package letsdev.demo.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static letsdev.demo.domain.UserDomainConstants.UserValidation.NICKNAME_VALIDATION;
import static letsdev.demo.domain.UserDomainConstants.UserValidation.PASSWORD_VALIDATION;
import static letsdev.demo.domain.UserDomainConstants.UserValidation.USERNAME_VALIDATION;
import static letsdev.demo.domain.UserDomainConstants.UserValidationMessage.NICKNAME_MAX_SIZE_MESSAGE;
import static letsdev.demo.domain.UserDomainConstants.UserValidationMessage.NICKNAME_MIN_SIZE_MESSAGE;
import static letsdev.demo.domain.UserDomainConstants.UserValidationMessage.NICKNAME_NOT_BLANK_MESSAGE;
import static letsdev.demo.domain.UserDomainConstants.UserValidationMessage.NICKNAME_PATTERN_MESSAGE;
import static letsdev.demo.domain.UserDomainConstants.UserValidationMessage.PASSWORD_MAX_SIZE_MESSAGE;
import static letsdev.demo.domain.UserDomainConstants.UserValidationMessage.PASSWORD_MIN_SIZE_MESSAGE;
import static letsdev.demo.domain.UserDomainConstants.UserValidationMessage.PASSWORD_NOT_BLANK_MESSAGE;
import static letsdev.demo.domain.UserDomainConstants.UserValidationMessage.PASSWORD_PATTERN_MESSAGE;
import static letsdev.demo.domain.UserDomainConstants.UserValidationMessage.USERNAME_NOT_BLANK_MESSAGE;
import static letsdev.demo.domain.UserDomainConstants.UserValidationMessage.USERNAME_PATTERN_MESSAGE;

public record SignUpRequest(
        @NotBlank(message = USERNAME_NOT_BLANK_MESSAGE)
        @Pattern(
                regexp = USERNAME_VALIDATION,
                message = USERNAME_PATTERN_MESSAGE
        )
        @Size(min = 3, message = USERNAME_PATTERN_MESSAGE)
        @Size(max = 30, message = USERNAME_PATTERN_MESSAGE)
        String username,

        @JsonProperty("password")
        @NotBlank(message = PASSWORD_NOT_BLANK_MESSAGE)
        @Pattern(
                regexp = PASSWORD_VALIDATION,
                message = PASSWORD_PATTERN_MESSAGE
        )
        @Size(min = 8, message = PASSWORD_MIN_SIZE_MESSAGE)
        @Size(max = 100, message = PASSWORD_MAX_SIZE_MESSAGE)
        String rawPassword,

        @NotBlank(message = NICKNAME_NOT_BLANK_MESSAGE)
        @Pattern(
                regexp = NICKNAME_VALIDATION,
                message = NICKNAME_PATTERN_MESSAGE
        )
        @Size(min = 3, message = NICKNAME_MIN_SIZE_MESSAGE)
        @Size(max = 30, message = NICKNAME_MAX_SIZE_MESSAGE)
        String nickname
) {
}
