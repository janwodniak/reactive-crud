package dev.janwodniak.reactivecrud.service.implementation;

import dev.janwodniak.reactivecrud.exception.custom.NoteNotFoundException;
import dev.janwodniak.reactivecrud.model.Note;
import dev.janwodniak.reactivecrud.model.command.EditNoteCommand;
import dev.janwodniak.reactivecrud.model.command.EditNotePartiallyCommand;
import dev.janwodniak.reactivecrud.repository.NoteRepository;
import dev.janwodniak.reactivecrud.service.ReactiveNoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

import static java.util.Optional.ofNullable;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class DefaultNoteService implements ReactiveNoteService {

    private final NoteRepository noteRepository;

    @Transactional(readOnly = true)
    @Override
    public Mono<Note> getNoteById(Long id) {
        log.info("Fetching note with ID: {}", id);
        return noteRepository.findById(id)
                .doOnSuccess(note -> log.debug("Found note: {}", note))
                .switchIfEmpty(noteNotFoundException(id));
    }

    @Override
    public Mono<Note> createNote(Note note) {
        log.info("Creating a new note.");
        return noteRepository.save(note)
                .doOnSuccess(savedNote -> log.info("Successfully created note with ID: {}", savedNote.id()));
    }

    @Override
    public Mono<Note> editNote(Long id, EditNoteCommand command) {
        log.info("Editing note with ID: {}", id);
        return updateNote(id, existingNote -> Note.builder()
                .id(existingNote.id())
                .title(command.title())
                .content(command.content())
                .version(existingNote.version())
                .build()
        );
    }

    @Override
    public Mono<Note> editNotePartially(Long id, EditNotePartiallyCommand command) {
        log.info("Partially editing note with ID: {}", id);
        return updateNote(id, existingNote -> Note.builder()
                .id(existingNote.id())
                .title(ofNullable(command.title()).orElse(existingNote.title()))
                .content(ofNullable(command.content()).orElse(existingNote.content()))
                .version(existingNote.version())
                .build()
        );
    }

    @Override
    public Mono<Void> deleteNote(Long id) {
        log.info("Deleting note with ID: {}", id);
        return noteRepository.findById(id)
                .switchIfEmpty(noteNotFoundException(id))
                .flatMap(note -> {
                    log.debug("Found note to delete: {}", note);
                    return noteRepository.delete(note);
                })
                .doOnSuccess(v -> log.info("Successfully deleted note with ID: {}", id));
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Page<Note>> searchNotes(String title, Pageable pageable) {
        return noteRepository.findByTitleContainingIgnoreCase(title, pageable)
                .collectList()
                .zipWhen(notes -> noteRepository.countByTitleContainingIgnoreCase(title))
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    private Mono<Note> updateNote(Long id, UnaryOperator<Note> noteMapper) {
        return noteRepository.findById(id)
                .map(noteMapper)
                .flatMap(note -> {
                    log.debug("Updated note: {}", note);
                    return noteRepository.save(note);
                })
                .switchIfEmpty(noteNotFoundException(id));
    }

    private <T> Mono<T> noteNotFoundException(Long id) {
        log.error("Note with ID {} not found.", id);
        return Mono.error(new NoteNotFoundException(id));
    }

}