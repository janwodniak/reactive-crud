package dev.janwodniak.reactivecrud.controlller;

import dev.janwodniak.reactivecrud.test.DatabaseCleaner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testingisdocumenting.webtau.junit5.WebTau;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;

@Slf4j
@ActiveProfiles("test")
@WebTau
@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class BaseIntegrationTest {

    private static final ZonedDateTime NOW = ZonedDateTime.of(
            2023, 9, 21, 21, 45, 0, 0, ZoneId.of("UTC")
    );

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer = initializePostgreSQLContainer();

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @LocalServerPort
    private int port;

    @MockBean
    private Clock clock;

    @SuppressWarnings("resource")
    private static PostgreSQLContainer<?> initializePostgreSQLContainer() {
        log.info("Initializing PostgreSQL container");
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("test")
                .withUsername("postgres")
                .withPassword("postgres")
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 2));
    }

    @AfterAll
    static void cleanup() {
        Optional.ofNullable(postgreSQLContainer)
                .filter(PostgreSQLContainer::isRunning)
                .ifPresent(PostgreSQLContainer::stop);
    }

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry) {
        setR2dbcProperties(registry);
        setLiquibaseProperties(registry);
    }

    private static void setR2dbcProperties(DynamicPropertyRegistry registry) {
        log.info("Setting R2DBC properties");

        var r2dbcUrl = buildPostgresUrl("r2dbc:postgresql://");
        registry.add("spring.r2dbc.url", () -> r2dbcUrl);
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
    }

    private static void setLiquibaseProperties(DynamicPropertyRegistry registry) {
        log.info("Setting Liquibase properties");

        var jdbcUrl = buildPostgresUrl("jdbc:postgresql://");
        registry.add("spring.liquibase.url", () -> jdbcUrl);
        registry.add("spring.liquibase.user", postgreSQLContainer::getUsername);
        registry.add("spring.liquibase.password", postgreSQLContainer::getPassword);
    }

    private static String buildPostgresUrl(String prefix) {
        return prefix + postgreSQLContainer.getHost()
                + ":" + postgreSQLContainer.getFirstMappedPort()
                + "/" + postgreSQLContainer.getDatabaseName();
    }

    @BeforeEach
    void setUpBaseIntegrationTest() {
        log.info("Setting up tests with base URL: http://localhost:{}", port);

        var baseUrl = "http://localhost:" + this.port;
        getCfg().setUrl(baseUrl);

        mockClockBehavior();
    }

    private void mockClockBehavior() {
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(clock.getZone()).thenReturn(NOW.getZone());
    }

    @AfterEach
    void tearDownBaseIntegrationTest() {
        log.info("Cleaning up database after test");
        databaseCleaner.cleanUp();
    }

}