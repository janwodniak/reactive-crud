package dev.janwodniak.reactivecrud.validation.validator;


import dev.janwodniak.reactivecrud.validation.annotation.ClassFields;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

@Component
@RequiredArgsConstructor
@Scope("prototype")
public class ClassFieldsValidator implements ConstraintValidator<ClassFields, String> {

    private Pattern fieldsPattern;

    private static String getStringPattern(String[] excludedFields, Class<?> fieldsSource) {
        return stream(fieldsSource.getDeclaredFields())
                .map(Field::getName)
                .filter(fieldName -> !asList(excludedFields).contains(fieldName))
                .collect(Collectors.joining("|"));
    }

    @Override
    public void initialize(ClassFields constraintAnnotation) {
        String[] excludedFields = constraintAnnotation.excludedFieldsNames();
        Class<?> fieldsSource = constraintAnnotation.fieldsSource();
        this.fieldsPattern = Pattern.compile(getStringPattern(excludedFields, fieldsSource));
    }

    @Override
    public boolean isValid(String sortByValue, ConstraintValidatorContext constraintValidatorContext) {
        return fieldsPattern.matcher(sortByValue).matches();
    }

}
