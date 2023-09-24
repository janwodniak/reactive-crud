package dev.janwodniak.reactivecrud.initializer;

import dev.janwodniak.reactivecrud.model.note.Note;
import dev.janwodniak.reactivecrud.repository.NoteRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name = "data.initializer.enabled", havingValue = "true")
public class DataInitializer {

    private final NoteRepository noteRepository;
    private final DataInitializerProperties properties;

    @PostConstruct
    public void initializeData() {
        log.info("Initializing data...");

        var bufferSize = 100;

        var notes = Flux.range(0, ofNullable(properties.count()).orElse(1000))
                .map(i -> new Note(
                        null,
                        randomAlphabetic(5, 10),
                        randomAlphabetic(10, 20),
                        LocalDateTime.now(),
                        null
                ));

        notes.buffer(bufferSize)
                .flatMap(noteRepository::saveAll)
                .doOnNext(savedNotes -> log.info("Saved {} notes.", bufferSize))
                .doOnComplete(() -> log.info("Data initialization complete."))
                .doOnError(e -> log.error("Error during data initialization.", e))
                .subscribe();
    }

    @PreDestroy
    public void cleanup() {
        log.info("Cleaning up data...");

        noteRepository.deleteAll()
                .doOnTerminate(() -> log.info("Cleanup complete."))
                .doOnError(e -> log.error("Error during data cleanup.", e))
                .subscribe();
    }

}