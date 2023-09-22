package dev.janwodniak.reactivecrud.model.command;

public record EditNoteCommand(
        String title,
        String content
) {
}
