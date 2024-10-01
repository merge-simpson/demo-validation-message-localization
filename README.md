# 기본 정보

- 로케일(locale; "로켈"로 발음) 언어 코드: ISO-639 두 자리 언어 코드를 기본으로 사용  
- 번역 메시지 소스 지원 확장자: `.properties`, `.xml`

# Locale 인식 우선순위

이 예시 프로젝트에서 다음 항목은 서버가 지원하는 언어 목록에 해당할 때만 반영됩니다.

1. lang 파라미터 (일반적인 언어 파라미터 이름)
   - `lang=ko` 등 URL 파라미터로 로케일 언어를 선택합니다.
   - `lang` 파라미터를 보내면 이후 세션과 쿠키에 저장됩니다.
   - 세션과 쿠키에 저장할 때는 서버 지원 언어 목록이 아니더라도 입력된 대로 저장됩니다.
   - 세션과 쿠키에 저장할 때는 서버 지원 언어 목록이 아니면, 저장만 되고 로케일은 다음 순위에 있는 항목들로 선택됩니다.
2. 세션 "LOCALE_LANG" (커스텀된 세션 속성 이름)
    - 이 방식은 필수가 아니며, 예시를 위해 포함되었습니다.
    - 이 예시 프로젝트에서 이 세션 항목은 다른 기능을 통해 추가되지 않고, `lang` 파라미터가 있을 때만 추가됩니다.
3. 쿠키 "LOCALE_LANG" (커스텀된 쿠키 이름)
    - 이 방식은 필수가 아니며, 예시를 위해 포함되었습니다.
    - 이 예시 프로젝트에서 이 쿠키 항목은 다른 기능을 통해 추가되지 않고, `lang` 파라미터가 있을 때만 추가됩니다.
    - 예시 프로젝트에서 `max-age`는 30일로 하였습니다.
4. Accept-Language 헤더
   - 주로 브라우저가 자동으로 붙여 주는 헤더입니다.
   - 사용자 상호작용으로 추가될 수 있는 `lang` 파라미터, 세션, 쿠키에 비해 우선순위가 낮습니다.
   - 서버가 지원하는 언어일 때만 이 헤더의 로케일 언어를 사용합니다.
   - 목록으로 제공되면 각 언어의 우선순위에 따라 선택됩니다.
5. 시스템 기본 값 (또는 애플리케이션 설정 기본값)
    - 이 예시 프로젝트는 시스템의 기본 값 대신 `Locale.ENGLISH`를 사용합니다.

# 사용자 입력 정보

## Lang 파라미터

사용자 요청에 `lang` 파라미터가 있을 때 로케일이 명시적으로 변경됩니다.
`LocaleChangeInterceptor`와 `LocaleResolver` 빈에 의해 로케일이 변경됩니다.

## 세션과 쿠키

(이 예시 프로젝트에서는) 사용자가 `lang` 파라미터를 보내면 그 정보를 세션과 쿠키에 저장합니다.  
사용자의 정보를 기억하는 것이기 때문에 서버가 지원하는 언어 코드가 아니더라도 기억합니다.  
하지만 서버가 지원하지 않는 언어 코드일 때는 기억된 내용도 사용자의 로케일로 취급하지 않고 무시됩니다.

## Accept-Language Header (Browser Default)

주로 브라우저가 자동으로 붙이는 헤더입니다.  
목록으로 제공되면 항목을 콤마(,)로 구분하며, 선호도 가중치 `q`의 값이 큰 순서로 우선순위를 구분합니다.  
_(0 ≤ `q` ≤ 1)_

만약 `Accept-Language` 헤더가 `en-US,de;q=0.9,ko;q=0.8`라는 내용으로 도착한다면 우선순위는 다음 순서로 적용됩니다.  

1. `en-US`는 `q`가 생략되어 `q=1`로 임의 적용이 됩니다. `q`가 가장 높기 때문에 우선순위가 가장 높습니다.
2. `de;q=0.9`는 독일어 언어 코드(`de`)로, `q=0.9`로 두 번째로 선호도가 높습니다.
3. `ko;q=0.8`은 한국어 언어 코드(`ko`)로, `q=0.8`로 세 번째로 선호도가 높습니다.
4. 작성된 순서와 별개로 선호도 `q`의 대소 관계에 따라 로케일 우선순위를 결정합니다.  
  (만약 `en-US`를 `en-US;q=0.7`로 작성하여,
   `Accept-Language` 헤더의 내용이 `en-US;q=0.7,de;q=0.9,ko;q=0.8` 순서로 나열되어도
   `q`의 대소 관계에 따라 `de;q=0.9`, `ko;q=0.8`, `en-US;q=0.7`순으로 선호도 및 우선순위를 인식합니다.)

`Accept-Language` 헤더의 로케일 언어 목록 중
서버에서 지원하는 로케일 언어에 해당하는 언어만 선택되며,
그중 선호도 `q`가 가장 높은 언어를 로케일 언어로 선택합니다.

예시에 있는 `en-US`는 언어 코드(`en`)와 국가 코드(`US`)가 있는 로케일입니다.
`en-US` 로케일이 `en` 로케일과 호환되도록 코드를 작성합니다.

# 기타 정보

## 모듈의 구분

모듈의 구분에 대한 설명은
[src/main/java/letsdev/demo/configuration/modular/example/README.md](
  ./src/main/java/letsdev/demo/configuration/modular/example/README.md
)에 있습니다.

## Validation 우선순위

한 필드에 대해 여러 유효성 필드 에러(validation field error)가 발생하면, 유효성 필드 에러 간 우선순위는 다음과 같습니다.
`priority` 값이 낮을수록 높은 우선순위를 갖습니다.

```java
private int getPriority(FieldError error) {
    return switch (error.getCode()) {
        case String s when s.startsWith("NotNull") -> 10;
        case String s when s.startsWith("NotEmpty") || s.startsWith("NotBlank") -> 20;
        case String s when s.startsWith("Size") -> 30;
        case String s when s.startsWith("Pattern") -> 40;
        case String s when s.startsWith("Email") -> 50;
        case String s when s.startsWith("Min") || s.startsWith("Max") -> 60;
        case String s when s.startsWith("Digits") -> 70;
        case String s when s.startsWith("Future") || s.startsWith("Past") -> 80;
        case String s when s.startsWith("Positive") || s.startsWith("Negative") -> 90;
        case String s when s.startsWith("AssertTrue") || s.startsWith("AssertFalse") -> 100;
        case null, default -> Integer.MAX_VALUE;
    };
}
```

예를 들어 `@NotBlank`, `@Size`, `@Pattern`에서 유효성이 맞지 않는다면 우선순위는 다음과 같습니다.

- `@NotBlank` 오류
- `@Size` 오류
- `@Pattern` 오류

## 일부만 번역된 언어

서버가 지원하는 언어 목록에 있는 로케일이더라도 만약 일부 메시지만 번역되었다면, 번역되지 않은 메시지는
`Accept-Language` 헤더 등에 있던 다음 선호 언어가 아니라 애플리케이션 기본 로케일인 `en` 메시지로 반환됩니다.
다국어 메시지 소스를 다루는 리졸버(resolver)의 기본 동작 때문입니다.

만약 `lang` 파라미터 없이, `Accept-Language` 헤더의 내용이 `zh-CN,ja;q=0.9`로 도착했다면
메시지 반환 흐름은 다음과 같습니다.

- 서버 지원 언어 목록에 `zh`가 있기 때문에 스레드 로케일에 `zh`가 담깁니다.
  - 두 번째 선호 언어 코드인 `ja`는 무시되고, 이 사용자의 로케일을 `zh`로 해석합니다.
- 메시지 리졸버는 `zh` 속성이 제공되는 메시지는 `zh` 메시지로 출력합니다.
- 메시지 리졸버는 `zh` 속성이 제공하지 않는 메시지는 이 애플리케이션의 기본 로케일인 `en` 메시지로 출력합니다.
- 이 동안 메시지 리졸버와 스레드 로케일은 사용자가 제공한 `ja`가 두 번째 선호도를 갖고 있었다는 것을 기억하지 않습니다.

# Example Request

## 기본 값

- 언어 코드를 작성하지 않았을 때 영어로 validation 메시지가 옵니다.
  - URL: http://localhost:8080/sign-up
  - Method: `POST`
  - Request Body (JSON)
    ```json
    {
      "username": "",
      "password": "InputPassPhrase",
      "nickname": "John"
    }
    ```
  - Response Body
    ```json
    {
      "timestamp": "2024-10-01T00:00:00.000000Z",
      "status": 400,
      "error": "Bad Request",
      "message": "Please enter a username.",
      "path": "/sign-up"
    }
    ```

## 언어별 lang 파라미터

지원 언어 목록

- `en`: 영어 
- `ko`: 한국어
- `ja`: 일본어
- `zh`: 중국어

요청 및 응답 예시

- `lang=ko` 파라미터를 담아서 보낸 요청은 validation 메시지가 한국어로 옵니다.
  - URL: http://localhost:8080/sign-up?lang=ko
  - Method: `POST`
      <details>
        <summary>Request Body (JSON)</summary>
        
      ```json
      {
        "username": "",
        "password": "InputPassPhrase",
        "nickname": "John"
      }
      ```
    
      </details>
  - Response Body (Example)

    ```json
    {
      "timestamp": "2024-10-01T00:00:00.000000Z",
      "status": 400,
      "error": "Bad Request",
      "message": "사용자 이름을 입력하세요.",
      "path": "/sign-up"
    }
    ```

- `lang=fr`(지원 언어가 아님) 파라미터를 담아서 보낸 요청은 validation 메시지가 기본 언어인 영어로 옵니다.
  - URL: http://localhost:8080/sign-up?lang=fr
  - Method: `POST`
      <details>
        <summary>Request Body (JSON)</summary>

      ```json
      {
        "username": "",
        "password": "InputPassPhrase",
        "nickname": "John"
      }
      ```

      </details>
  - Response Body (Example)
    ```json
    {
      "message": "Please enter a username.",
      "...": ""
    }
    ```
    > `...`은 생략된 JSON 필드를 나타냅니다.

### 세션 및 쿠키에 저장됨

[세션과 쿠키](#세션과-쿠키) 항목을 참고하세요.
`lang` 파라미터로 전달한 내용은 세션이나 쿠키에 저장됩니다.
갱신은 `lang` 파라미터로 새로운 언어 코드를 전달하면 됩니다.

지원 언어 목록

- `en`: 영어
- `ko`: 한국어
- `ja`: 일본어
- `zh`: 중국어

## 언어 및 지역에 따른 Accept-Language 헤더와 우선순위

`lang` 파라미터가 없거나 지원 언어가 아니고, 세션과 쿠키에 저장된 로케일 언어가 없거나 

지원 언어 목록

- `en`: 영어
- `ko`: 한국어
- `ja`: 일본어
- `zh`: 중국어

요청 및 응답 예시

- 첫 번째 항목이 언어 및 지역 코드, 두 번째 항목이 언어 코드이며 이 두 언어 코드가 일치하는 것이 보통입니다.
    - URL: http://localhost:8080/sign-up
    - Method: `POST`
    - Accept-Language: `en-US,en;q=0.9,ko;q=0.8`
        <details>
          <summary>Request Body (JSON)</summary>

        ```json
        {
          "username": "",
          "password": "InputPassPhrase",
          "nickname": "John"
        }
        ```

        </details>
    - Response Body (Example)
      ```json
      {
        "message": "Please enter a username.",
        "...": ""
      }
      ```
      > `...`은 생략된 JSON 필드를 나타냅니다.

- 앞 목록이 지원 언어가 아니고 뒤 목록에 지원 언어가 있다면,
  지원언어 중 가장 선호도(`q`)가 높은 언어로 validation 메시지를 응답합니다.
    - URL: http://localhost:8080/sign-up
    - Method: `POST`
    - Accept-Language: `de-DE,de;q=0.9,ko;q=0.8`
        <details>
          <summary>Request Body (JSON)</summary>

        ```json
        {
          "username": "",
          "password": "InputPassPhrase",
          "nickname": "John"
        }
        ```

        </details>
    - Response Body (Example)
      ```json
      {
        "message": "사용자 이름을 입력하세요.",
        "...": ""
      }
      ```
      > `...`은 생략된 JSON 필드를 나타냅니다.

- 모든 목록이 지원 언어가 아니면, 기본 언어인 영어로 validation 메시지를 응답합니다.
    - URL: http://localhost:8080/sign-up
    - Method: `POST`
    - Accept-Language: `de-DE,de;q=0.9,fr;q=0.8`
        <details>
          <summary>Request Body (JSON)</summary>

        ```json
        {
          "username": "",
          "password": "InputPassPhrase",
          "nickname": "John"
        }
        ```

        </details>
    - Response Body (Example)
      ```json
      {
        "message": "Please enter a username.",
        "...": ""
      }
      ```
      > `...`은 생략된 JSON 필드를 나타냅니다.

- 첫 번째 항목이 지원 언어지만 국가 코드를 포함할 때도 첫 번째 항목의 언어를 잘 파싱하여 validation 메시지를 응답합니다.
    - URL: http://localhost:8080/sign-up
    - Method: `POST`
    - Accept-Language: `en-US,de;q=0.9,ko;q=0.8`
        <details>
          <summary>Request Body (JSON)</summary>

        ```json
        {
          "username": "",
          "password": "InputPassPhrase",
          "nickname": "John"
        }
        ```

        </details>
    - Response Body (Example)
      ```json
      {
        "message": "Please enter a username.",
        "...": ""
      }
      ```
      > `...`은 생략된 JSON 필드를 나타냅니다.

- 정렬되어 있지 않아도 우선순위를 잘 해석하여,
  서버가 지원하는 언어 중 우선순위가 가장 높은 언어로 validation 메시지를 응답합니다.
    - URL: http://localhost:8080/sign-up
    - Method: `POST`
    - Accept-Language: `en-US;q=0.7,de;q=0.9,ko;q=0.8`
        <details>
          <summary>Request Body (JSON)</summary>

        ```json
        {
          "username": "",
          "password": "InputPassPhrase",
          "nickname": "John"
        }
        ```

        </details>
    - Response Body (Example)
      ```json
      {
        "message": "사용자 이름을 입력하세요.",
        "...": ""
      }
      ```
      > `...`은 생략된 JSON 필드를 나타냅니다.
