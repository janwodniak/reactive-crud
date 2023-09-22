package dev.janwodniak.reactivecrud.model.command;

public record EditNotePartiallyCommand(
        String title,
        String content
) {
}
