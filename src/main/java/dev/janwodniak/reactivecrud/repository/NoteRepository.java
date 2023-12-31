package dev.janwodniak.reactivecrud.repository;

import dev.janwodniak.reactivecrud.model.note.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NoteRepository extends R2dbcRepository<Note, Long> {

    Flux<Note> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Mono<Long> countByTitleContainingIgnoreCase(String title);

}
