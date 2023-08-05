package com.osa.domainsapp.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayConfig {

  @Bean(initMethod = "migrate")
  public Flyway flyway(FlywayProperties flywayProperties) {
    ClassicConfiguration classicConfiguration = new ClassicConfiguration();
    classicConfiguration.setDataSource(
        flywayProperties.getUrl(),
        flywayProperties.getUser(),
        flywayProperties.getPassword()
    );
    String location = flywayProperties.getLocations().get(0);
    classicConfiguration.setLocations(new Location(location));

    return new Flyway(classicConfiguration);
  }

  @Bean
  public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
    return new FlywayMigrationInitializer(flyway);
  }
}
