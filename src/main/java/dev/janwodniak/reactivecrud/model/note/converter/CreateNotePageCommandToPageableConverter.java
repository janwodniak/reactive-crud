package dev.janwodniak.reactivecrud.model.note.converter;

import dev.janwodniak.reactivecrud.model.note.command.CreateNotePageCommand;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class CreateNotePageCommandToPageableConverter implements Converter<CreateNotePageCommand, Pageable> {

    @Override
    public Pageable convert(MappingContext<CreateNotePageCommand, Pageable> context) {
        var source = context.getSource();

        return PageRequest.of(
                source.getPageNumber(),
                source.getPageSize(),
                Sort.by(Sort.Direction.valueOf(source.getSortDirection().toUpperCase()), source.getSortBy())
        );
    }

}
