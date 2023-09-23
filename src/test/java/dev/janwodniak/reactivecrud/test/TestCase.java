package dev.janwodniak.reactivecrud.test;

import lombok.Builder;

@Builder
public record TestCase<I, E>(
        String testName,
        I input,
        E expectedOutput
) {
    @Override
    public String toString() {
        return testName;
    }
}
