package com.example.samplespring.person;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonConfig {
    @Bean
    CommandLineRunner commandLineRunner(
            PersonRepository repository) {
        return args -> {
            Person mariam = new Person(
                    "Mariam",
                    1000F
            );

            Person alex = new Person(
                    "Alex",
                    2500F
            );
            Person larry = new Person(
                    "Larry",
                    4200F
            );
            Person tina = new Person(
                    "Tina",
                    500F
            );
            Person peter = new Person(
                    "Peter",
                    2500F
            );
            Person kevin = new Person(
                    "Kevin",
                    1745F
            );
            Person ben = new Person(
                    "Ben",
                    5000F
            );
            Person janice = new Person(
                    "Janice",
                    1333.37F
            );
            Person jackson = new Person(
                    "Jackson",
                    2314F
            );

            repository.saveAll(
                    List.of(mariam, alex, larry, tina, peter, kevin, ben, janice, jackson)
            );
        };
    }
}
