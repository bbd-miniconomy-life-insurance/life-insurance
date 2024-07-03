package bbd.miniconomy.lifeinsurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LifeInsuranceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifeInsuranceApplication.class, args);
	}
}
