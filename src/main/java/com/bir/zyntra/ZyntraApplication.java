package com.bir.zyntra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ZyntraApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZyntraApplication.class, args);
	}

}
