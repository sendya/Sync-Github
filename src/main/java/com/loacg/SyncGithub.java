package com.loacg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication
public class SyncGithub {

	public static void main(String[] args) {
		SpringApplication.run(SyncGithub.class, args);
	}

	@PostConstruct
	public void start() {

	}

	@PreDestroy
	public void destroy() {

	}
}
