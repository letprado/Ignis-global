package globalsolution.ignis_orbital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class IgnisOrbitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgnisOrbitalApplication.class, args);
	}
}
