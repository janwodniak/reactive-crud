package dev.janwodniak.reactivecrud.model.note.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNotePageCommand {
    private int pageNumber = 0;
    private int pageSize = 5;
    private String sortDirection = "ASC";
    private String sortBy = "id";
}
