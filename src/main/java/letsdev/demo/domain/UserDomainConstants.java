package letsdev.demo.domain;

public final class UserDomainConstants {
    private UserDomainConstants() {}

    public static final class UserValidation {
        public static final String USERNAME_VALIDATION = "^[A-Za-z\\d]+$";
        public static final String PASSWORD_VALIDATION = "^[A-Za-z\\d~!@#$%^&*?_=\\-+,./:;]+$";
        public static final String NICKNAME_VALIDATION = "^[A-Za-z\\d가-힣ㄱ-ㅣ]+$";
        private UserValidation() {}
    }

    public static final class UserValidationMessage {
        public static final String USERNAME_NOT_BLANK_MESSAGE = "{validation.user.username.notBlank}";
        public static final String USERNAME_PATTERN_MESSAGE = "{validation.user.username.pattern}";

        public static final String PASSWORD_NOT_BLANK_MESSAGE = "{validation.user.password.notBlank}";
        public static final String PASSWORD_PATTERN_MESSAGE = "{validation.user.password.pattern}";
        public static final String PASSWORD_MIN_SIZE_MESSAGE = "{validation.user.password.minSize}";
        public static final String PASSWORD_MAX_SIZE_MESSAGE = "{validation.user.password.maxSize}";

        public static final String NICKNAME_NOT_BLANK_MESSAGE = "{validation.profile.nickname.notBlank}";
        public static final String NICKNAME_PATTERN_MESSAGE = "{validation.profile.nickname.pattern}";
        public static final String NICKNAME_MIN_SIZE_MESSAGE = "{validation.profile.nickname.minSize}";
        public static final String NICKNAME_MAX_SIZE_MESSAGE = "{validation.profile.nickname.maxSize}";
        private UserValidationMessage() {}
    }
}
