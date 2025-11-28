package com.assignment.bugLogging;

import org.springframework.boot.SpringApplication;

public class TestBugLoggingApplication {

	public static void main(String[] args) {
		SpringApplication.from(BugLoggingApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
