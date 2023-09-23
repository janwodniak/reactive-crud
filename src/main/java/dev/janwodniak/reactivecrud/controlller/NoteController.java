package dev.janwodniak.reactivecrud.controlller;

import dev.janwodniak.reactivecrud.model.note.Note;
import dev.janwodniak.reactivecrud.model.note.command.CreateNoteCommand;
import dev.janwodniak.reactivecrud.model.note.command.CreateNotePageCommand;
import dev.janwodniak.reactivecrud.model.note.command.EditNoteCommand;
import dev.janwodniak.reactivecrud.model.note.command.EditNotePartiallyCommand;
import dev.janwodniak.reactivecrud.model.note.dto.NoteDto;
import dev.janwodniak.reactivecrud.service.ReactiveNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.status;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final ReactiveNoteService noteService;
    private final ModelMapper modelMapper;

    @GetMapping
    Mono<ResponseEntity<Page<NoteDto>>> searchNotes(@RequestParam Optional<String> title, @Valid CreateNotePageCommand command) {
        return noteService.searchNotes(title.orElse(""), toPageable(command))
                .map(this::toDtoPage)
                .map(ResponseEntity::ok);
    }

    @GetMapping("{id}")
    Mono<ResponseEntity<NoteDto>> getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    Mono<ResponseEntity<NoteDto>> createNote(@RequestBody @Valid CreateNoteCommand command) {
        return noteService.createNote(toEntity(command))
                .map(this::toDto)
                .map(noteDto -> status(CREATED).body(noteDto));
    }

    @DeleteMapping("{id}")
    Mono<ResponseEntity<HttpStatus>> deleteNote(@PathVariable Long id) {
        return noteService.deleteNote(id)
                .thenReturn(noContent().build());
    }

    @PutMapping("{id}")
    Mono<ResponseEntity<NoteDto>> editNote(@PathVariable Long id, @RequestBody @Valid EditNoteCommand command) {
        return noteService.editNote(id, command)
                .map(this::toDto)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("{id}")
    Mono<ResponseEntity<NoteDto>> editNotePartially(@PathVariable Long id, @RequestBody @Valid EditNotePartiallyCommand command) {
        return noteService.editNotePartially(id, command)
                .map(this::toDto)
                .map(ResponseEntity::ok);
    }

    private NoteDto toDto(Note note) {
        return modelMapper.map(note, NoteDto.class);
    }

    private Note toEntity(CreateNoteCommand command) {
        return modelMapper.map(command, Note.class);
    }

    private Page<NoteDto> toDtoPage(Page<Note> notePage) {
        return notePage.map(this::toDto);
    }

    private Pageable toPageable(CreateNotePageCommand command) {
        return modelMapper.map(command, Pageable.class);
    }

}
