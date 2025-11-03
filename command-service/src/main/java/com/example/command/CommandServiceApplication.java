package com.example.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Command Service - Write Side of CQRS
 * Handles all commands (creates, updates) and publishes events
 */
@SpringBootApplication
@EnableKafka
@EnableTransactionManagement
public class CommandServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommandServiceApplication.class, args);
    }
}
