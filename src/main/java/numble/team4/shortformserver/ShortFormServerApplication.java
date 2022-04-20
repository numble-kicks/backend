package numble.team4.shortformserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class ShortFormServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortFormServerApplication.class, args);
	}

}
