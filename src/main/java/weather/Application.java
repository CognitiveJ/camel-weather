package weather;

import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.SpringApplication.run;

/**
 * Bootstraps the launch of the Spring application and sets up the camel config
 * simply run the main method within an IDE
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        final ConfigurableApplicationContext app =
                run(Application.class, args);

        final CamelSpringBootApplicationController applicationController
                = app.getBean(CamelSpringBootApplicationController.class);

        applicationController.blockMainThread();
    }
}
