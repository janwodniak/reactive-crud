package dev.janwodniak.reactivecrud.service.implementation;

import dev.janwodniak.reactivecrud.exception.custom.NoteNotFoundException;
import dev.janwodniak.reactivecrud.model.note.Note;
import dev.janwodniak.reactivecrud.model.note.command.EditNoteCommand;
import dev.janwodniak.reactivecrud.model.note.command.EditNotePartiallyCommand;
import dev.janwodniak.reactivecrud.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class DefaultNoteServiceTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
    private static Note note;
    @Mock
    private NoteRepository noteRepository;
    @InjectMocks
    private DefaultNoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        note = Note.builder()
                .id(1L)
                .title("title")
                .content("content")
                .date(NOW)
                .version(1L)
                .build();
    }

    @Test
    void getNoteByIdShouldReturnNoteWhenExists() {
        // given
        var noteId = 1L;
        given(noteRepository.findById(noteId)).willReturn(Mono.just(note));

        // when
        var result = noteService.getNoteById(noteId);

        // then
        StepVerifier.create(result)
                .expectNext(note)
                .verifyComplete();
    }

    @Test
    void getNoteByIdShouldThrowExceptionWhenNotFound() {
        // given
        var noteId = 1L;
        given(noteRepository.findById(noteId)).willReturn(Mono.empty());

        // when
        var result = noteService.getNoteById(noteId);

        // then
        StepVerifier.create(result)
                .expectError(NoteNotFoundException.class)
                .verify();
    }

    @Test
    void createNoteShouldReturnCreatedNote() {
        // given
        given(noteRepository.save(note)).willReturn(Mono.just(note));

        // when
        var result = noteService.createNote(note);

        // then
        StepVerifier.create(result)
                .expectNext(note)
                .verifyComplete();
    }

    @Test
    void editNoteShouldReturnEditedNote() {
        // given
        var noteId = 1L;
        var command = new EditNoteCommand("newTitle", "newContent");
        given(noteRepository.findById(noteId)).willReturn(Mono.just(note));
        given(noteRepository.save(any(Note.class))).willReturn(Mono.just(new Note(1L, "newTitle", "newContent", NOW, 1L)));

        // when
        var result = noteService.editNote(noteId, command);

        // then
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    void editNotePartiallyShouldReturnEditedNote() {
        // given
        var noteId = 1L;
        var command = new EditNotePartiallyCommand("newTitle", null);
        given(noteRepository.findById(noteId)).willReturn(Mono.just(note));
        given(noteRepository.save(any(Note.class))).willReturn(Mono.just(new Note(1L, "newTitle", "content", NOW, 1L)));

        // when
        var result = noteService.editNotePartially(noteId, command);

        // then
        StepVerifier.create(result)
                .expectNextMatches(note -> note.title().equals("newTitle") && note.content().equals("content"))
                .verifyComplete();
    }

    @Test
    void deleteNoteShouldCompleteWhenNoteExists() {
        // given
        Long noteId = 1L;
        given(noteRepository.findById(noteId)).willReturn(Mono.just(note));
        given(noteRepository.delete(note)).willReturn(Mono.empty());

        // when
        var result = noteService.deleteNote(noteId);

        // then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void deleteNoteShouldThrowExceptionWhenNoteNotFound() {
        // given
        var noteId = 1L;
        given(noteRepository.findById(noteId)).willReturn(Mono.empty());

        // when
        var result = noteService.deleteNote(noteId);

        // then
        StepVerifier.create(result)
                .expectError(NoteNotFoundException.class)
                .verify();
    }

    @Test
    void searchNotesShouldReturnPagedResult() {
        // given
        var title = "title";
        var pageable = PageRequest.of(0, 5);
        var notes = List.of(note);
        given(noteRepository.findByTitleContainingIgnoreCase(title, pageable)).willReturn(Flux.fromIterable(notes));
        given(noteRepository.countByTitleContainingIgnoreCase(title)).willReturn(Mono.just(1L));

        // when
        var result = noteService.searchNotes(title, pageable);

        // then
        StepVerifier.create(result)
                .expectNextMatches(page -> page.getTotalElements() == 1 && page.getContent().get(0).title().equals("title"))
                .verifyComplete();
    }

}