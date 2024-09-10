package swyp.swyp6_team7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Swyp6Team7Application {

    public static void main(String[] args) {
        SpringApplication.run(Swyp6Team7Application.class, args);
    }

}
