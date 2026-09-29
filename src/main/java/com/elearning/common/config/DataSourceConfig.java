package com.elearning.common.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
@Profile("!test")
public class DataSourceConfig {

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            URI uri = URI.create(databaseUrl);
            String userInfo = uri.getUserInfo();
            String[] credentials = userInfo != null ? userInfo.split(":") : new String[0];
            config.setJdbcUrl("jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath());
            config.setUsername(credentials.length > 0 ? credentials[0] : "elearning");
            config.setPassword(credentials.length > 1 ? credentials[1] : "elearning123");
        } else {
            config.setJdbcUrl("jdbc:postgresql://" +
                System.getenv().getOrDefault("DB_HOST", "localhost") + ":" +
                System.getenv().getOrDefault("DB_PORT", "5432") + "/" +
                System.getenv().getOrDefault("DB_NAME", "elearning"));
            config.setUsername(System.getenv().getOrDefault("DB_USERNAME", "elearning"));
            config.setPassword(System.getenv().getOrDefault("DB_PASSWORD", "elearning123"));
        }

        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }
}
