package numble.team4.shortformserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ShortFormServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortFormServerApplication.class, args);
	}

}
