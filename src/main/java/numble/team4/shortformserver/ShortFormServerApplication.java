package numble.team4.shortformserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class ShortFormServerApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
		+ "classpath:application.yml,"
		+ "classpath:aws.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(ShortFormServerApplication.class)
			.properties(APPLICATION_LOCATIONS)
			.run(args);
	}

}
