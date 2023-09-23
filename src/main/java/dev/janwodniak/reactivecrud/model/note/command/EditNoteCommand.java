package dev.janwodniak.reactivecrud.model.note.command;

public record EditNoteCommand(
        String title,
        String content
) {
}
