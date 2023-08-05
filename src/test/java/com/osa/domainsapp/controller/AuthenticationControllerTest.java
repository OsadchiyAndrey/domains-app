package com.osa.domainsapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.osa.domainsapp.DtoTestFactory;
import com.osa.domainsapp.payload.AuthorizationRequest;
import com.osa.domainsapp.payload.AuthorizationResponse;
import com.osa.domainsapp.service.AuthenticationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

  private MockMvc mvc;
  private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
  @Mock
  private AuthenticationService authenticationService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    AuthenticationController authenticationController = new AuthenticationController(authenticationService);
    mvc = MockMvcBuilders.standaloneSetup(authenticationController)
        .build();
  }

  @Test
  public void test_createAuthenticationToken_statusIsOk() throws Exception {
    AuthorizationRequest authorizationRequest = DtoTestFactory.buildAuthorizationRequest();
    AuthorizationResponse authorizationResponse = DtoTestFactory.buildAuthorizationResponse();
    Mockito
        .when(authenticationService.authenticate(Mockito.any(AuthorizationRequest.class)))
        .thenReturn(authorizationResponse);
    mvc.perform(MockMvcRequestBuilders.post("/api/v1/authentication/authenticate")
            .contentType(APPLICATION_JSON)
            .content(objectWriter.writeValueAsString(authorizationRequest)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.username", Is.is(authorizationResponse.getUsername())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.token", Is.is(authorizationResponse.getToken())))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void test_isAuthenticated_statusIsOk() throws Exception {
    String username = RandomStringUtils.randomAlphabetic(10);
    Mockito
        .when(authenticationService.getUsername())
        .thenReturn(username);
    mvc.perform(MockMvcRequestBuilders.get("/api/v1/authentication/get-me", username))
        .andExpect(MockMvcResultMatchers.jsonPath("$", Is.is(username)))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
