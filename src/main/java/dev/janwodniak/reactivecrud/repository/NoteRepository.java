package dev.janwodniak.reactivecrud.repository;

import dev.janwodniak.reactivecrud.model.Note;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends R2dbcRepository<Note, Long> {
}
