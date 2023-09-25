package dev.janwodniak.reactivecrud.service;

import dev.janwodniak.reactivecrud.model.note.Note;
import dev.janwodniak.reactivecrud.model.note.command.EditNoteCommand;
import dev.janwodniak.reactivecrud.model.note.command.EditNotePartiallyCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface ReactiveNoteService {


    /**
     * Retrieve a single note by its identifier.
     *
     * @param id the ID of the note
     * @return a Mono emitting the found note
     */
    Mono<Note> getNoteById(Long id);

    /**
     * Create a new note.
     *
     * @param note the note data to create
     * @return a Mono emitting the created note
     */
    Mono<Note> createNote(Note note);

    /**
     * Edit an existing note using provided data.
     *
     * @param command containing data for note editing
     * @return a Mono emitting the edited note
     */
    Mono<Note> editNote(Long id, EditNoteCommand command);

    /**
     * Edit a note partially using provided data.
     *
     * @param command containing partial data for note editing
     * @return a Mono emitting the partially edited note
     */
    Mono<Note> editNotePartially(Long id, EditNotePartiallyCommand command);

    /**
     * Delete a note by its identifier.
     *
     * @param id the ID of the note
     * @return a Mono indicating completion
     */
    Mono<Void> deleteNote(Long id);

    /**
     * Search notes by title containing a given substring with pagination.
     *
     * @param title    the substring to search for within note titles
     * @param pageable the pagination and sorting information
     * @return a Mono emitting a page of notes with titles containing the given substring
     */
    Mono<Page<Note>> searchNotes(String title, Pageable pageable);

}
