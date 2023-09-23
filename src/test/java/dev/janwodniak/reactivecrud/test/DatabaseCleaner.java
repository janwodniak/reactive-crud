package dev.janwodniak.reactivecrud.test;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.util.Optional.ofNullable;

@Slf4j
@Component
@Profile("test")
@Scope("prototype")
@RequiredArgsConstructor
public class DatabaseCleaner {

    private final R2dbcProperties r2dbcProperties;
    private final LiquibaseProperties liquibaseProperties;
    private Connection connection;
    private Liquibase liquibase;

    @PostConstruct
    public void initialize() {
        log.info("Initializing DatabaseCleaner...");
        connection = initializeConnection();
        liquibase = initializeLiquibase(connection);
        log.info("DatabaseCleaner initialized successfully.");
    }

    @PreDestroy
    public void closeConnection() {
        log.info("Closing database connection...");
        ofNullable(connection).ifPresent(conn -> {
            try {
                conn.close();
                log.info("Database connection closed successfully.");
            } catch (SQLException e) {
                log.error("Error closing database connection", e);
                throw new RuntimeException("Error closing database connection", e);
            }
        });
    }

    private Connection initializeConnection() {
        try {
            log.debug("Establishing database connection to {}", liquibaseProperties.getUrl());
            return DriverManager.getConnection(liquibaseProperties.getUrl(), r2dbcProperties.getUsername(), r2dbcProperties.getPassword());
        } catch (SQLException e) {
            log.error("Error initializing database connection", e);
            throw new RuntimeException("Error initializing database connection", e);
        }
    }

    private Liquibase initializeLiquibase(Connection connection) {
        try {
            log.debug("Initializing Liquibase with changelog {}", liquibaseProperties.getChangeLog());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            return new Liquibase(liquibaseProperties.getChangeLog(), new ClassLoaderResourceAccessor(), database);
        } catch (DatabaseException e) {
            log.error("Error initializing Liquibase", e);
            throw new RuntimeException("Error initializing Liquibase", e);
        }
    }

    public void cleanUp() {
        try {
            log.info("Cleaning up database...");
            liquibase.dropAll();
            liquibase.update(new Contexts());
            log.info("Database cleanup completed successfully.");
        } catch (LiquibaseException e) {
            log.error("Error during database cleanup", e);
            throw new RuntimeException("Error during database cleanup", e);
        }
    }

}