package dev.janwodniak.reactivecrud.exception.custom;

import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationErrorDtoTest {

    @Test
    void shouldCreateValidationErrorDtoFromFieldError() {
        // given
        var expectedField = "testField";
        var expectedMessage = "testMessage";
        var fieldError = new FieldError("objectName", expectedField, expectedMessage);

        // when
        var validationErrorDto = ValidationErrorDto.fromFieldError(fieldError);

        // then
        assertEquals(expectedField, validationErrorDto.field());
        assertEquals(expectedMessage, validationErrorDto.message());
    }

}