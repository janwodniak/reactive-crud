package dev.janwodniak.reactivecrud.validation.validator;


import dev.janwodniak.reactivecrud.validation.annotation.ClassFields;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Scope("prototype")
public class ClassFieldsValidator implements ConstraintValidator<ClassFields, String> {

    private Pattern fieldsPattern;

    @Override
    public void initialize(ClassFields constraintAnnotation) {
        var excludedFields = Set.of(constraintAnnotation.excludedFieldsNames());
        var fieldsSource = constraintAnnotation.fieldsSource();
        this.fieldsPattern = Pattern.compile(constructPatternFromFields(excludedFields, fieldsSource));
    }

    @Override
    public boolean isValid(String sortByValue, ConstraintValidatorContext constraintValidatorContext) {
        return fieldsPattern.matcher(sortByValue).matches();
    }

    private String constructPatternFromFields(Set<String> excludedFields, Class<?> fieldsSource) {
        return Arrays.stream(fieldsSource.getDeclaredFields())
                .map(Field::getName)
                .filter(fieldName -> !excludedFields.contains(fieldName))
                .collect(Collectors.joining("|"));
    }
}
