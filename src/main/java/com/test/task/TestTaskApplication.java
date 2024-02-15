package com.test.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestTaskApplication {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");;
        System.setProperty("PROJECT_PATH", projectPath);
        SpringApplication.run(TestTaskApplication.class, args);
    }

}
