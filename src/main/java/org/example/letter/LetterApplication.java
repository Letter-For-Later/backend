package org.example.letter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LetterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LetterApplication.class, args);
    }

}
