package dev.janwodniak.reactivecrud.configuration.hint;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@ImportRuntimeHints(LiquibaseRuntimeHintsConfiguration.LiquibaseRuntimeHints.class)
@Configuration
public class LiquibaseRuntimeHintsConfiguration {

    static class LiquibaseRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources().registerPattern("db/*");
        }

    }

}
