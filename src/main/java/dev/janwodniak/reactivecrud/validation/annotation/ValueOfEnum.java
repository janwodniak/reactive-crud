package dev.janwodniak.reactivecrud.validation.annotation;

import dev.janwodniak.reactivecrud.validation.validator.ValueOfEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {
    Class<? extends Enum<?>> enumClass();

    String message() default "MUST_BE_ANY_OF_{enumClass}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
