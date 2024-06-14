package ru.practicum.ewm.utils.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.ewm.utils.errors.exceptions.ConflictConstraintUniqueException;
import ru.practicum.ewm.utils.errors.exceptions.NotAllowedException;
import ru.practicum.ewm.utils.errors.exceptions.NotFoundException;
import ru.practicum.ewm.utils.formatter.HttpStatusFormatter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.ewm.utils.errors.ErrorConstants.ACTION_IS_NOT_ALLOWED;
import static ru.practicum.ewm.utils.errors.ErrorConstants.DATA_INTEGRITY_VIOLATION;
import static ru.practicum.ewm.utils.errors.ErrorConstants.INCORRECTLY_MADE_REQUEST;
import static ru.practicum.ewm.utils.errors.ErrorConstants.OBJECT_NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentValidViolations(MethodArgumentNotValidException e) {

        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.BAD_REQUEST))
                .reason(INCORRECTLY_MADE_REQUEST)
                .message(constructMessage(e))
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleMethodArgumentValidViolations(MethodArgumentNotValidException e) --> {}", e);
        writeLog(apiError);
        return apiError;
    }


    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableExceptionViolations(MethodArgumentNotValidException e) {

        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.BAD_REQUEST))
                .reason(INCORRECTLY_MADE_REQUEST)
                .message(constructMessage(e))
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleHttpMessageNotReadableExceptionViolations(MethodArgumentNotValidException e) --> {}", e);
        writeLog(apiError);
        return apiError;
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeValidViolations(MethodArgumentTypeMismatchException e) {

        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.BAD_REQUEST))
                .reason(INCORRECTLY_MADE_REQUEST)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleMethodArgumentTypeValidViolations(MethodArgumentTypeMismatchException e) --> {}", e);
        writeLog(apiError);
        return apiError;
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingParameters(MissingServletRequestParameterException e) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.BAD_REQUEST))
                .reason(INCORRECTLY_MADE_REQUEST)
                .message(constructMessage(e))
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleMissingParameters(MissingServletRequestParameterException e) --> {}", e);
        writeLog(apiError);
        return apiError;
    }

    @ExceptionHandler(NotAllowedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRuntimeViolations(NotAllowedException e) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.CONFLICT))
                .reason(ACTION_IS_NOT_ALLOWED)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleRuntimeViolations(NotAllowedException e) --> {}", e);
        writeLog(apiError);
        return apiError;
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(NotFoundException e) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.NOT_FOUND))
                .reason(OBJECT_NOT_FOUND)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleNotFound(NotFoundException e) --> {}", e);
        writeLog(apiError);
        return apiError;
    }

    @ExceptionHandler(ConflictConstraintUniqueException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRuntimeViolations(ConflictConstraintUniqueException e) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.CONFLICT))
                .reason(DATA_INTEGRITY_VIOLATION)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleRuntimeViolations(ConflictConstraintUniqueException e) --> {}", e);
        writeLog(apiError);
        return apiError;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUniqueSqlViolations(ConstraintViolationException e) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.BAD_REQUEST))
                .reason(INCORRECTLY_MADE_REQUEST)
                .message(constructMessage(e))
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleUniqueSqlViolations(ConstraintViolationException e) --> {}", e);
        writeLog(apiError);
        return apiError;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUniqueSqlViolations(ValidationException e) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.BAD_REQUEST))
                .reason(INCORRECTLY_MADE_REQUEST)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleUniqueSqlViolations(ValidationException e) --> {}", e);
        writeLog(apiError);
        return apiError;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictUniqueConstraintViolations(DataIntegrityViolationException e) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.CONFLICT))
                .reason(DATA_INTEGRITY_VIOLATION)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleConflictUniqueConstraintViolations(DataIntegrityViolationException e --> {}", e);
        writeLog(apiError);
        return apiError;
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIllegalArgument(IllegalArgumentException e) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.BAD_REQUEST))
                .reason(INCORRECTLY_MADE_REQUEST)
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleIllegalArgument(IllegalArgumentException e) --> {}", e);
        writeLog(apiError);
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnknownErrors(RuntimeException e) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatusFormatter.formatHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR))
                .reason(e.getClass().getName())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("handleUnknownErrors(RuntimeException e) --> {}", e);
        writeLog(apiError);
        return apiError;
    }

    private String constructMessage(BindException e) {
        FieldError error = e.getBindingResult().getFieldError();
        assert error != null;
        return ErrorMessage.builder()
                .field(error.getField())
                .error(error.getDefaultMessage())
                .value(error.getRejectedValue())
                .build().toString();
    }

    private String constructMessage(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        return violations.stream()
                .map(violation -> new ErrorMessage(
                        violation.getPropertyPath().toString(),
                        violation.getMessage(),
                        violation.getInvalidValue()))
                .collect(Collectors.toList())
                .get(0)
                .toString();
    }

    private String constructMessage(MissingServletRequestParameterException e) {
        return ErrorMessage.builder()
                .field(e.getParameterType() + " " + e.getParameterName())
                .error(e.getMessage())
                .value(null)
                .build()
                .toString();
    }

    private void writeLog(ApiError apiError) {
        log.info("ApiError --> {}", apiError);
    }
}