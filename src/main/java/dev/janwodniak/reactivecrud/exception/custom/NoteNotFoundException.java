package dev.janwodniak.reactivecrud.exception.custom;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(Long id) {
        super("NOTE_WITH_ID_%d_NOT_FOUND".formatted(id));
    }

}
