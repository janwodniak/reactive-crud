package dev.janwodniak.reactivecrud.model.note.converter;

import dev.janwodniak.reactivecrud.model.note.Note;
import dev.janwodniak.reactivecrud.model.note.command.CreateNoteCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class CreateNoteCommandToNoteConverter implements Converter<CreateNoteCommand, Note> {

    @Override
    public Note convert(MappingContext<CreateNoteCommand, Note> context) {
        var source = context.getSource();

        return Note.builder()
                .title(source.title())
                .content(source.content())
                .build();
    }

}
