package dev.janwodniak.reactivecrud.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@EnableR2dbcAuditing(dateTimeProviderRef = "dateTimeProvider")
@Configuration
public class DataConfiguration {
}
