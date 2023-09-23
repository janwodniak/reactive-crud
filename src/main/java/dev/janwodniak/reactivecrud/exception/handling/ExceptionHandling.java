package dev.janwodniak.reactivecrud.exception.handling;

import dev.janwodniak.reactivecrud.exception.custom.NoteNotFoundException;
import dev.janwodniak.reactivecrud.model.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.Clock;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.NOT_FOUND;
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
