package dev.janwodniak.reactivecrud.initializer;

import dev.janwodniak.reactivecrud.model.note.Note;
import dev.janwodniak.reactivecrud.repository.NoteRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Slf4j
@RequiredArgsConstructor
@DependsOn("liquibase")
@Component
@ConditionalOnProperty(name = "data.initializer.enabled", havingValue = "true")
public class DataInitializer {

    private final NoteRepository noteRepository;
    private final DataInitializerProperties properties;
    private final TransactionalOperator transactionalOperator;

    @PostConstruct
    public void initializeData() {
        log.info("Initializing data...");

        noteRepository.count()
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(5)))
                .doOnSuccess(count -> log.info("Database is accessible!"))
                .flatMapMany(count -> {
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

                    return notes.buffer(bufferSize).flatMap(noteRepository::saveAll);
                })
                .as(transactionalOperator::transactional)
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