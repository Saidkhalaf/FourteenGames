package be.kdg.gobackend;

import be.kdg.gobackend.shared.config.RegistrationConfig;
import be.kdg.gobackend.shared.config.rabbitMQ.RabbitMQProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({RabbitMQProperties.class, RegistrationConfig.class})
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
