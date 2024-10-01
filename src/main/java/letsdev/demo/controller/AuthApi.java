package letsdev.demo.controller;

import jakarta.validation.Valid;
import letsdev.demo.controller.dto.SignUpRequest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public final class AuthApi {

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(
            @RequestBody @Valid SignUpRequest body
            // binding result를 파라미터로 추가하면 예외도 이곳에서 확인할 수 있습니다.
//            BindingResult bindingResult
    ) {
        List<String> messages = List.of();
//        if (bindingResult.hasFieldErrors()) {
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            messages = fieldErrors.stream()
//                    .map(FieldError::getDefaultMessage)
//                    .toList();
//            System.out.println(messages);
//        }

        // 스레드 변수에 "locale"을 저장하기 때문에 다른 사용자의 요청과 격리된 데이터를 얻습니다.
        System.out.print(STR."""
                locale: \{LocaleContextHolder.getLocale()}
                messages: \{messages.isEmpty() ? "Empty" : messages}
                """
        );
    }
}
