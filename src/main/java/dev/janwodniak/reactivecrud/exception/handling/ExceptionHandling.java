package dev.janwodniak.reactivecrud.exception.handling;

import dev.janwodniak.reactivecrud.exception.custom.NoteNotFoundException;
import dev.janwodniak.reactivecrud.exception.custom.ValidationErrorDto;
import dev.janwodniak.reactivecrud.model.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionHandling {

    private final Clock clock;

    @ExceptionHandler(NoteNotFoundException.class)
    public Mono<ResponseEntity<HttpResponse>> handleNoteNotFoundException(NoteNotFoundException exc) {
        return createHttpResponse(NOT_FOUND, exc.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<List<ValidationErrorDto>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
        log.warn("Validation failed: {}", exc.getMessage());
        var errors = exc.getFieldErrors().stream()
                .map(ValidationErrorDto::fromFieldError)
                .toList();

        errors.forEach(error -> log.debug("Field error: {}", error));
        return Mono.just(badRequest().body(errors));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<List<ValidationErrorDto>>> handleMethodArgumentNotValidException(WebExchangeBindException exc) {
        log.warn("Web exchange binding failed: {}", exc.getMessage());
        var errors = exc.getFieldErrors().stream()
                .map(ValidationErrorDto::fromFieldError)
                .toList();

        errors.forEach(error -> log.debug("Field error: {}", error));
        return Mono.just(badRequest().body(errors));
    }

    private Mono<ResponseEntity<HttpResponse>> createHttpResponse(HttpStatus httpStatus, String message) {
        log.debug("Creating HTTP response with status: {} and message: {}", httpStatus, message);
        return Mono.just(status(httpStatus).body(
                HttpResponse.builder()
                        .timestamp(now(clock))
                        .httpStatusCode(httpStatus.value())
                        .httpStatus(httpStatus)
                        .reason(httpStatus.getReasonPhrase())
                        .message(message)
                        .build()
        ));
    }

}
