package dev.janwodniak.reactivecrud.model.note.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EditNoteCommand(
        @NotNull(message = "TITLE_REQUIRED")
        @Size(max = 255, message = "TITLE_TOO_LONG")
        @Pattern(regexp = "^[A-Za-z]+$", message = "TITLE_INVALID_FORMAT")
        String title,
        @NotNull(message = "CONTENT_REQUIRED")
        @Pattern(regexp = "^[A-Za-z0-9\\s]+$", message = "CONTENT_INVALID_FORMAT")
        String content
) {
}
