package ru.practicum.ewm.stats.utils.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewm.stats.utils.Constant;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

import static ru.practicum.ewm.stats.utils.Constant.CONTROLLER_PATH;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUniqueSqlViolations(ValidationException e) {
        return ApiError.builder()
                .reason(Constant.INVALID_REQUEST)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentValidViolations(MethodArgumentNotValidException e) {
        return ApiError.builder()
                .reason(Constant.INVALID_REQUEST)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeValidViolations(MethodArgumentTypeMismatchException e) {
        return ApiError.builder()
                .reason(Constant.INVALID_REQUEST)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIllegalArgument(IllegalArgumentException e) {
        return ApiError.builder()
                .reason(Constant.INVALID_REQUEST)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnknownErrors(RuntimeException e) {
        return ApiError.builder()
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
