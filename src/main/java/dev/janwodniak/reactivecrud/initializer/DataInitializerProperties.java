package dev.janwodniak.reactivecrud.initializer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "data.initializer")
public record DataInitializerProperties(boolean enabled, Integer count) {
}
