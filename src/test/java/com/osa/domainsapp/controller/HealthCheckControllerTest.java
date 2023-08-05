package com.osa.domainsapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
public class HealthCheckControllerTest {

  private MockMvc mvc;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    HealthCheckController healthCheckController = new HealthCheckController();
    mvc = MockMvcBuilders.standaloneSetup(healthCheckController).build();
  }

  @Test
  public void test_healthCheckSuccess() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/health")).andExpect(MockMvcResultMatchers.status().isOk());
  }
}
