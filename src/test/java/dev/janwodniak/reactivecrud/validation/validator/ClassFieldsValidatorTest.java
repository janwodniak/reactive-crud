package dev.janwodniak.reactivecrud.validation.validator;

import dev.janwodniak.reactivecrud.validation.annotation.ClassFields;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassFieldsValidatorTest {

    private ClassFieldsValidator validator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ClassFields constraintAnnotation;

    @BeforeEach
    void setUp() {
        validator = new ClassFieldsValidator();
    }

    void setupMock(String[] excludedFieldsNames) {
        // given
        when(constraintAnnotation.excludedFieldsNames()).thenReturn(excludedFieldsNames);
        Mockito.<Class<?>>when(constraintAnnotation.fieldsSource()).thenReturn(TestClass.class);
        validator.initialize(constraintAnnotation);
    }

    @ValueSource(strings = {"field1", "field2", "field3"})
    @ParameterizedTest
    void fieldPresent_ShouldReturnTrueIfFieldNotExcluded(String testArgument) {
        setupMock(new String[]{});

        // when
        boolean isValid = validator.isValid(testArgument, constraintValidatorContext);

        // then
        assertTrue(isValid);
    }

    @ValueSource(strings = {"field1", "field2", "field3"})
    @ParameterizedTest
    void fieldPresent_ShouldReturnFalseIfFieldExcluded(String testArgument) {
        setupMock(new String[]{testArgument});

        // when
        boolean isValid = validator.isValid(testArgument, constraintValidatorContext);

        // then
        assertFalse(isValid);
    }

    @ValueSource(strings = {"field4", "field5", "field6"})
    @ParameterizedTest
    void fieldNotPresent_ShouldAlwaysReturnFalse(String testArgument) {
        setupMock(new String[]{});

        // when
        boolean isValid = validator.isValid(testArgument, constraintValidatorContext);

        // then
        assertFalse(isValid);
    }

    private record TestClass(
            String field1,
            int field2,
            boolean field3
    ) {
    }

}