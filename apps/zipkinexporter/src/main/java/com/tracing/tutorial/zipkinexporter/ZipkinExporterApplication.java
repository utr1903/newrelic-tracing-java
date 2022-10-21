package com.tracing.tutorial.zipkinexporter;

import com.tracing.tutorial.zipkinexporter.service.zipkin.ZipkinProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ZipkinExporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinExporterApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() { return new RestTemplate(); }

	@Bean
	public ZipkinProcessor zipkinProcessor() {
		return new ZipkinProcessor();
	}
}
