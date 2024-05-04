package uz.buildia.attendancetracker.init;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13.4")
            .withDatabaseName("attendance_db")
            .withUsername("user")
            .withPassword("password")
            .withInitScript("create-schema.sql")
            .withReuse(true);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        boolean isLocalRun = Boolean.parseBoolean(
                applicationContext.getEnvironment().getProperty("local-run"));
        if (isLocalRun) {
            startPostgres(applicationContext);
        }
    }

    private void startPostgres(ConfigurableApplicationContext applicationContext) {

        POSTGRES.start();

        TestPropertyValues values = TestPropertyValues.of(
                "spring.datasource.driver-class-name:" + POSTGRES.getDriverClassName(),
                "spring.datasource.url:" + POSTGRES.getJdbcUrl(),
                "spring.datasource.username:" + POSTGRES.getUsername(),
                "spring.datasource.password:" + POSTGRES.getPassword()
        );

        values.applyTo(applicationContext);
    }
}
