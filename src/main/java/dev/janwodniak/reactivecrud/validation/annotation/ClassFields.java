package dev.janwodniak.reactivecrud.validation.annotation;

import dev.janwodniak.reactivecrud.validation.validator.ClassFieldsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ClassFieldsValidator.class)
public @interface ClassFields {
    String message() default "INVALID_CLASS_FIELDS_VALUE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> fieldsSource();

    String[] excludedFieldsNames() default {};
}
