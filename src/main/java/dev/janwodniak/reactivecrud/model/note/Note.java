package dev.janwodniak.reactivecrud.model.note;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Builder
@Table("notes")
public record Note(
        @Id
        Long id,
        String title,
        String content,
        @LastModifiedDate
        LocalDateTime date,
        @Version
        Long version
) {
}
