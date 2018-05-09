package gettingstarted;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import child.FileConfig;

@SpringBootApplication
public class SpringContentApplication {

	@Configuration
	@Import(FileConfig.class)
	public static class AppConfig {

	}
	public static void main(String[] args) {
		SpringApplication.run(SpringContentApplication.class, args);
	}
}
