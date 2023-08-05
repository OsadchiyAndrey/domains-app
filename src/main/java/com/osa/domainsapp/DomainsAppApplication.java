package com.osa.domainsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class DomainsAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(DomainsAppApplication.class, args);
  }

}
