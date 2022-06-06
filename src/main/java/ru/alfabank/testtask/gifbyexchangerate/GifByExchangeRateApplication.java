package ru.alfabank.testtask.gifbyexchangerate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootApplication
public class GifByExchangeRateApplication {

	public static void main(String[] args) {
		SpringApplication.run(GifByExchangeRateApplication.class, args);
	}

}
