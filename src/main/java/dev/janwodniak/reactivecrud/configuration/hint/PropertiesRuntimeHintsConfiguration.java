package dev.janwodniak.reactivecrud.configuration.hint;


import dev.janwodniak.reactivecrud.initializer.DataInitializerProperties;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;


@ImportRuntimeHints(PropertiesRuntimeHintsConfiguration.PropertiesRuntimeHints.class)
@Configuration
public class PropertiesRuntimeHintsConfiguration {

    static class PropertiesRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources().registerType(DataInitializerProperties.class);
        }

    }

}
