package dev.janwodniak.reactivecrud.model.note.command;

import dev.janwodniak.reactivecrud.model.note.Note;
import dev.janwodniak.reactivecrud.validation.annotation.ClassFields;
import dev.janwodniak.reactivecrud.validation.annotation.ValueOfEnum;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class CreateNotePageCommand {
    @Min(value = 0, message = "PAGE_NOT_NEGATIVE")
    private int pageNumber = 0;
    @Min(value = 1, message = "PAGE_SIZE_NOT_LESS_THAN_ONE")
    private int pageSize = 5;
    @ValueOfEnum(enumClass = Sort.Direction.class, message = "INVALID_SORT_DIRECTION")
    private String sortDirection = "ASC";
    @ClassFields(fieldsSource = Note.class, excludedFieldsNames = {"version"}, message = "INVALID_SORT_BY_VALUE_FIELD")
    private String sortBy = "id";
}
