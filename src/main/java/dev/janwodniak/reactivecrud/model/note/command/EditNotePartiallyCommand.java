package dev.janwodniak.reactivecrud.model.note.command;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EditNotePartiallyCommand(
        @Size(max = 255, message = "TITLE_TOO_LONG")
        @Pattern(regexp = "^[A-Za-z]+$", message = "TITLE_INVALID_FORMAT")
        String title,
        @Pattern(regexp = "^[A-Za-z0-9\\s]+$", message = "CONTENT_INVALID_FORMAT")
        String content
) {
}
