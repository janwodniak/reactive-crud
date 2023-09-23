package dev.janwodniak.reactivecrud.model.note.command;

public record CreateNoteCommand(
        String title,
        String content
) {
}
