package com.loacg;

import com.loacg.github.SyncDaemon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SyncGithub {

	public static void main(String[] args) {
		SpringApplication.run(SyncGithub.class, args);
	}

	@PostConstruct
	public void start() {

	}
}
