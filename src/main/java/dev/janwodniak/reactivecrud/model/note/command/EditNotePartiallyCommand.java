package dev.janwodniak.reactivecrud.model.note.command;

public record EditNotePartiallyCommand(
        String title,
        String content
) {
}
