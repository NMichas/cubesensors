package com.github.nmichas.cubesensors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
