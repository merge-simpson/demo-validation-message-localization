# Modular Example (Not Actually Modularized)

예시는 실제 모듈화를 하지 않고, 복잡성을 낮추기 위해 단일 프로젝트 모듈에서 작성합니다.

## 하나의 모듈에서 관리하기 (쉬움)

멀티모듈 프로젝트는 각 모듈의 리소스 폴더에 있는 파일을 모두 인식합니다.
따라서 한 곳에서 다음처럼 추가할 수도 있습니다.
다음 방식은 다른 서비스에서 관리하는 리소스들을 공통된 한 모듈의 소스 코드에서 등록하는 방식입니다.

```java
// ❌It would work, but not recommended.
@Bean
public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    // see here:
    messageSource.setBasename(
            "classpath:validation-message",
            // scan additional resource files in each module:
            "classpath:/auth/auth-validation-message",
            "classpath:/profile/profile-validation-message"
    );
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setFallbackToSystemLocale(false);
    return messageSource;
}
```

## 각 모듈에서 관리하기 (올바른 책임 분리를 지향하기)

우리는 멀티 모듈 프로젝트에서 협업 가능한 아키텍처를 위하여, 리소스 관리의 종속성을 조정할 수 있습니다.  
예를 들어 다음 그림처럼 모듈 A가 모듈 B를 사용할 때, 모듈 A는 모듈 A와 모듈 B의 리소스를 모두 사용할 수 있습니다.
하지만 역으로 모듈 B가 모듈 A의 리소스를 '미리 알고 있는 것처럼' 사용하는 것은
모듈 B가 충분히 독립적으로 동작할 수 있도록 캡슐화되지 않았다는 것을 의미하기 때문에 삼가야 합니다.  
(위 코드에서 모듈 B는 공통된 모듈, 모듈 A는 auth, profile 모듈.)

![모듈마다 접근해도 괜찮은 리소스를 표현한 사진입니다.
모듈 A가 모듈 B를 사용하기 때문에
모듈 A는 모듈 A, B의 리소스를 모두 사용할 수 있지만
모듈 B는 자신의 리소스만 사용할 수 있습니다.
이것은 추천되는 옵션으로, 모듈 A가 모듈 B와 함께 로드된다면
모듈 B에서도 모듈 A의 리소스를 인식할 수 있습니다.
](../../../../../../../../docs/assets/recommended-accessable-resources-for-each-module-q40-w800.avif)

모듈이 자신이 가진 의존성만으로 독립적으로 동작할 수 있게 하려면,
각 모듈이 관리하는 리소스는 그 모듈에서 또는 그 모듈을 사용하는 모듈에서 처리하도록 합니다.

### TO BE

1. 리소스 파일을 모듈마다 나누어 관리합니다.  
    - `Auth` 모듈의 classpath(리소스 폴더 등)에
      `auth/auth-validation-message.properties` 파일과 언어 코드별 파생 파일을 만듭니다.
    - `Profile` 모듈의 classpath(리소스 폴더 등)에
      `profile/profile-validation-message.properties` 파일과 언어 코드별 파생 파일을 만듭니다.
2. Configuration 파일을 모듈마다 나누어, 이곳에서 모듈의 리소스 파일의 스캔을 관리할 수 있습니다.
    - `Auth` 모듈에 `AuthMessageSourceConfiguration` 클래스를 만듭니다.
    - `Profile` 모듈에 `ProfileMessageSourceConfiguration` 클래스를 만듭니다.

### 세분화된 모듈 운용하기

만약 모듈의 구분이 서비스 단위 구분에 그치지 않고 더욱 세분화되어 있다면,
그 서비스의 세부 모듈 중 어느 모듈에서 리소스와 설정 파일을 관리할지 고민할 수 있습니다.  

- 데이터에 대한 정책은 도메인 모듈 또는 그 인근 모듈에서 관리할 수 있습니다.
- 도메인 모듈에서 관리하지 않았으면 하는 것들 중에는 사용자로부터 입력된 요청과 응답,
  그리고 프레임워크에 의존성을 갖는 동작 등 외부 상황에 의존하는 것들이 있습니다..
- 스프링에 의존하면서 유효성 메시지를 등록하는 새로운 모듈을 구분해도 됩니다. (선호)
