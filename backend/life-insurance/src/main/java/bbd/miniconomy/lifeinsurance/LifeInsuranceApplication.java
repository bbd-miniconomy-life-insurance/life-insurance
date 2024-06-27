package bbd.miniconomy.lifeinsurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class LifeInsuranceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifeInsuranceApplication.class, args);
	}

}
