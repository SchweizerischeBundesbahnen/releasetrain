package ch.sbb.releasetrain.webui;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import ch.sbb.releasetrain.webui.director.Director;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        List<String> profiles = Arrays.asList(context.getEnvironment().getActiveProfiles());
        if (!profiles.contains("springboot")) {
            context.getBean(Director.class).direct();
        }
    }
}
