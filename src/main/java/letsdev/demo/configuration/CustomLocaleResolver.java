package letsdev.demo.configuration;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.Cookie;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("localeResolver")
public class CustomLocaleResolver implements LocaleResolver {

    private static final String LANG_PARAM = "lang";
    private static final String COOKIE_NAME = "LOCALE_LANG";
    private static final String SESSION_ATTRIBUTE_NAME = "LOCALE_LANG";
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    // 지원하는 언어 목록
    private static final List<Locale> SUPPORTED_LOCALES = List.of(
            Locale.of("en"), // 영어
            Locale.of("ko"), // 한국어
            Locale.of("zh"), // 중국어
            Locale.of("ja")  // 일본어 (일어)
            // ... fr, de(독일어), es(스페인어), tl(필리핀 타갈로그어), vi(베트남어), id(인도네시아어), ar(아랍어)
            // ... th(태국어), it(이탈리아어), ... ISO-639-1 두 자리 언어 코드
    );
    private static final Set<String> SUPPORTED_LOCALE_LANGUAGES = SUPPORTED_LOCALES.stream()
            .map(Locale::getLanguage)
            .collect(Collectors.toSet());

    private final ThreadLocal<HttpServletRequest> threadRequest = new NamedThreadLocal<>("request");

    public CustomLocaleResolver() {
        if (Locale.getDefault() != DEFAULT_LOCALE) {
            Locale.setDefault(DEFAULT_LOCALE);
        }
    }

    public static boolean supportsLanguage(Locale locale) {
        return supportsLanguage(locale.getLanguage());
    }

    public static boolean supportsLanguage(String localeLanguageCode) {
        return SUPPORTED_LOCALE_LANGUAGES.contains(localeLanguageCode);
    }

    @Override
    @Nonnull
    public Locale resolveLocale(@Nonnull HttpServletRequest request) {
        threadRequest.set(request);

        Locale locale = fromUrl() // 1. URL 파라미터(lang)에서 locale 추출
                .or(this::fromSession) // 2. 세션에서 locale 추출
                .or(this::fromCookie) // 3. 쿠키에서 locale 추출
                // 4. Accept-Language 헤더 또는 서버 기본값에서 locale 추출
                .or(this::fromAcceptLanguageHeaderOrServerDefault)
                .orElse(DEFAULT_LOCALE)/* 논리적으로는 이곳까지 오지 않음. */;

        threadRequest.remove();
        return locale;
    }

    @Override
    public void setLocale(
            HttpServletRequest request,
            HttpServletResponse response,
            Locale locale
    ) {
        // 세션과 쿠키에 locale 저장
        request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME, locale);
        Cookie localeLangCookie = new Cookie(COOKIE_NAME, locale.getLanguage());
        localeLangCookie.setMaxAge(3600 * 24 * 30);  // 30일 간 유지하는 예시
        localeLangCookie.setPath("/");
        response.addCookie(localeLangCookie);
    }

    private Optional<Locale> fromUrl() {
        HttpServletRequest request = threadRequest.get();
        String langParam = request.getParameter(LANG_PARAM);
        if (StringUtils.hasText(langParam)) {
            Locale locale = Locale.of(langParam);
            if (SUPPORTED_LOCALES.contains(locale)) {
                return Optional.of(locale);
            }
        }
        return Optional.empty();
    }

    private Optional<Locale> fromSession() {
        HttpServletRequest request = threadRequest.get();
        Locale sessionLocale = (Locale) request.getSession().getAttribute(SESSION_ATTRIBUTE_NAME);
        if (sessionLocale != null && SUPPORTED_LOCALES.contains(sessionLocale)) {
            return Optional.of(sessionLocale);
        }
        return Optional.empty();
    }

    private Optional<Locale> fromCookie() {
        HttpServletRequest request = threadRequest.get();
        Optional<String> cookieLocale = Optional.ofNullable(request.getCookies())
                .flatMap(
                        cookies -> Arrays.stream(cookies)
                                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                                .map(Cookie::getValue)
                                .findFirst()
                );
        if (cookieLocale.isPresent() && SUPPORTED_LOCALE_LANGUAGES.contains(cookieLocale.get())) {
            return cookieLocale.map(Locale::of);
        }
        return Optional.empty();
    }

    /**
     * Accept-Language 헤더에서 locale 추출
     *  Accept-Language 헤더가 없다면 시스템 기본값 (Locale.getDefault())
     *
     * @return Accept-Language 헤더에서 시스템이 지원하는 언어 중 우선순위대로 로케일 반환,
     *         지원하는 것이 없다면 시스템 기본값 반환. (Non-empty)
     */
    private Optional<Locale> fromAcceptLanguageHeaderOrServerDefault() {
        HttpServletRequest request = threadRequest.get();

        // 이 메서드는 절대 null 또는 빈 목록을 반환하지 않음. (시스템 기본값을 포함하기 때문.)
        List<Locale> requestLocales = Collections.list(request.getLocales());
        for (Locale requestLocale : requestLocales) {
            if (SUPPORTED_LOCALE_LANGUAGES.contains(requestLocale.getLanguage())) {
                return Optional.of(requestLocale);
            }
        }

        // 논리상으로는 이곳에 오지 않음.
        return Optional.empty();
    }
}
