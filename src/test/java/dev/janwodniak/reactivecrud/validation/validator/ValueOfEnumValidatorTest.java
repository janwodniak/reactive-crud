package dev.janwodniak.reactivecrud.validation.validator;

import dev.janwodniak.reactivecrud.validation.annotation.ValueOfEnum;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testingisdocumenting.webtau.junit5.WebTau;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTau
@ExtendWith(MockitoExtension.class)
class ValueOfEnumValidatorTest {


    private ValueOfEnumValidator validator;

    @Mock
    private ValueOfEnum constraintAnnotation;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    static Stream<String> presentValuesOfTestEnum() {
        return Stream.of(TestEnum.values()).map(TestEnum::name);
    }

    static Stream<String> absentValuesOfTestEnum() {
        return Stream.of("TEST4", "TEST5", "TEST6");
    }

    @BeforeEach
    void setUp() {
        validator = new ValueOfEnumValidator();
    }

    @MethodSource("presentValuesOfTestEnum")
    @ParameterizedTest
    void shouldReturnTrueIfStringValueOfEnumIsPresent(String testValue) {
        // given
        var testEnum = TestEnum.class;

        // when
        Mockito.<Class<?>>when(constraintAnnotation.enumClass()).thenReturn(testEnum);
        validator.initialize(constraintAnnotation);
        var isValid = validator.isValid(testValue, constraintValidatorContext);

        // then
        assertTrue(isValid);
    }

    @MethodSource("absentValuesOfTestEnum")
    @ParameterizedTest
    void shouldReturnFalseIfStringValueOfEnumIsAbsent(String testValue) {
        // given
        var testEnum = TestEnum.class;

        // when
        Mockito.<Class<?>>when(constraintAnnotation.enumClass()).thenReturn(testEnum);
        validator.initialize(constraintAnnotation);
        var isValid = validator.isValid(testValue, constraintValidatorContext);

        // then
        assertFalse(isValid);
    }

    enum TestEnum {
        TEST1, TEST2, TEST3
    }

}