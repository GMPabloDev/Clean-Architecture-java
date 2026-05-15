package io.gianmarco.cleanArchitecture.infrastructure.config.seed;

import io.gianmarco.cleanArchitecture.infrastructure.persistence.repositories.seed.DatabaseCleaner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("seed")
@Configuration
public class SeedConfig {

    @Bean
    CommandLineRunner run(DatabaseCleaner cleaner) {
        return args -> {
            cleaner.clean(); // 🔥 limpia todo

            System.out.println("SEED ejecutada");
        };
    }
}
