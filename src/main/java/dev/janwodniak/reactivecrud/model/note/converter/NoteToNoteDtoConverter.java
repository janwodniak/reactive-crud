package dev.janwodniak.reactivecrud.model.note.converter;

import dev.janwodniak.reactivecrud.model.note.Note;
import dev.janwodniak.reactivecrud.model.note.dto.NoteDto;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class NoteToNoteDtoConverter implements Converter<Note, NoteDto> {

    @Override
    public NoteDto convert(MappingContext<Note, NoteDto> context) {
        var source = context.getSource();

        return NoteDto.builder()
                .id(source.id())
                .title(source.title())
                .content(source.content())
                .date(source.date())
                .build();
    }

}
