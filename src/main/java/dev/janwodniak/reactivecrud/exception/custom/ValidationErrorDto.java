package dev.janwodniak.reactivecrud.exception.custom;

import org.springframework.validation.FieldError;

public record ValidationErrorDto(
        String field,
        String message
) {

    public static ValidationErrorDto fromFieldError(FieldError fieldError) {
        return new ValidationErrorDto(
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );
    }

}
