package anand.passport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PassportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PassportServiceApplication.class, args);
	}
}
