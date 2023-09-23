package dev.janwodniak.reactivecrud.model.http;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public record HttpResponse(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        int httpStatusCode,
        HttpStatus httpStatus,
        String reason,
        String message
) {
}
