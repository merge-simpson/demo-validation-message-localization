package letsdev.demo.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class MethodArgumentNotValidExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        BindingResult bindingResult = exception.getBindingResult();
        record Response(
                Instant timestamp,
                int status,
                String error,
                @JsonInclude(Include.NON_NULL)
                String message,
                String path
        ) {}
        @SuppressWarnings("ConstantConditions")
        String message = bindingResult.hasFieldErrors() ?
                bindingResult.getFieldErrors().stream()
                        .reduce(this::getPriorItem)
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .orElse(null)
                : null;
        Response body = new Response(
                Instant.now(),
                400,
                "Bad Request",
                message,
                request.getRequestURI()
        );
        return ResponseEntity
                .badRequest()
                .body(body);
    }

    private FieldError getPriorItem(@Nonnull FieldError lError, @Nonnull FieldError rError) {
        int l = getPriority(lError);
        int r = getPriority(rError);
        return l < r ? lError : rError;
    }

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
}
