package com.elearning.db;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class MigrationIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("elearning")
        .withUsername("elearning")
        .withPassword("elearning123");

    @Test
    void allMigrationsApplyCleanlyOnFreshDatabase() {
        Flyway flyway = Flyway.configure()
            .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
            .baselineOnMigrate(true)
            .baselineVersion("1")
            .locations("classpath:db/migration")
            .load();

        MigrateResult result = flyway.migrate();

        assertTrue(result.migrationsExecuted > 0, "At least one migration should execute");
        assertTrue(result.success, "Migration result should be successful");
    }

    @Test
    void migrationsProduceExpectedTables() throws Exception {
        Flyway flyway = Flyway.configure()
            .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
            .baselineOnMigrate(true)
            .baselineVersion("1")
            .locations("classpath:db/migration")
            .load();

        flyway.migrate();

        try (Connection conn = postgres.createConnection("");
             Statement stmt = conn.createStatement()) {

            // Verify core tables exist
            String[] expectedTables = {
                "users", "courses", "lessons", "enrollments", "quizzes", "quiz_questions",
                "orders", "order_items", "payments", "refunds",
                "coupons", "coupon_redemptions", "offers",
                "ads", "ad_events",
                "notifications", "notification_preferences",
                "quiz_attempts", "lesson_progress",
                "audit_log", "password_reset_tokens", "instructor_applications", "announcements"
            };

            for (String table : expectedTables) {
                ResultSet rs = stmt.executeQuery(
                    "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = '" + table + "')");
                rs.next();
                assertTrue(rs.getBoolean(1), "Table " + table + " should exist");
            }
        }
    }

    @Test
    void seedDataIsInserted() throws Exception {
        Flyway flyway = Flyway.configure()
            .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
            .baselineOnMigrate(true)
            .baselineVersion("1")
            .locations("classpath:db/migration")
            .load();

        flyway.migrate();

        try (Connection conn = postgres.createConnection("");
             Statement stmt = conn.createStatement()) {

            // Check seed users
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE id >= 1001");
            rs.next();
            assertTrue(rs.getInt(1) >= 14, "Should have at least 14 seed users");

            // Check seed courses
            rs = stmt.executeQuery("SELECT COUNT(*) FROM courses WHERE id >= 1001");
            rs.next();
            assertTrue(rs.getInt(1) >= 8, "Should have at least 8 seed courses");

            // Check seed lessons
            rs = stmt.executeQuery("SELECT COUNT(*) FROM lessons WHERE id >= 1001");
            rs.next();
            assertTrue(rs.getInt(1) >= 20, "Should have at least 20 seed lessons");

            // Check seed coupons
            rs = stmt.executeQuery("SELECT COUNT(*) FROM coupons WHERE id >= 1001");
            rs.next();
            assertTrue(rs.getInt(1) >= 3, "Should have at least 3 seed coupons");

            // Check seed ads
            rs = stmt.executeQuery("SELECT COUNT(*) FROM ads WHERE id >= 1001");
            rs.next();
            assertTrue(rs.getInt(1) >= 3, "Should have at least 3 seed ads");
        }
    }

    @Test
    void flywaySchemaHistoryShowsAllMigrations() throws Exception {
        Flyway flyway = Flyway.configure()
            .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
            .baselineOnMigrate(true)
            .baselineVersion("1")
            .locations("classpath:db/migration")
            .load();

        flyway.migrate();

        try (Connection conn = postgres.createConnection("");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(
                "SELECT version, description, script, success FROM flyway_schema_history ORDER BY installed_rank");
            List<String> migrations = new ArrayList<>();
            while (rs.next()) {
                migrations.add(rs.getString("version") + " - " + rs.getString("description") +
                    " (success=" + rs.getBoolean("success") + ")");
            }

            assertTrue(migrations.size() >= 4, "Should have at least 4 migration entries");
            assertTrue(migrations.stream().allMatch(m -> m.contains("success=true")),
                "All migrations should be marked successful");
        }
    }
}
