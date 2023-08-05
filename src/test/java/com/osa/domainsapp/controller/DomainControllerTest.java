package com.osa.domainsapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.osa.domainsapp.DtoTestFactory;
import com.osa.domainsapp.exception.DomainException;
import com.osa.domainsapp.exception.GeneralExceptionHandler;
import com.osa.domainsapp.payload.AddDomainRequest;
import com.osa.domainsapp.payload.AddDomainResponse;
import com.osa.domainsapp.service.DomainService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class DomainControllerTest {

  private MockMvc mvc;
  @Mock
  private DomainService domainService;
  private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();


  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    DomainController domainController = new DomainController(domainService);
    mvc = MockMvcBuilders.standaloneSetup(domainController)
        .setControllerAdvice(new GeneralExceptionHandler())
        .build();
  }

  @Test
  public void test_addDomain_success() throws Exception {
    AddDomainRequest addDomainRequest = DtoTestFactory.buildAddDomainRequest();
    AddDomainResponse addDomainResponse = DtoTestFactory.buildAddDomainResponse();
    Mockito
        .when(domainService.addDomain(Mockito.any(AddDomainRequest.class)))
        .thenReturn(addDomainResponse);
    mvc.perform(MockMvcRequestBuilders.post("/api/v1/domains")
            .contentType(APPLICATION_JSON)
            .content(objectWriter.writeValueAsString(addDomainRequest)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Is.is(addDomainResponse.getStatusCode())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Is.is(addDomainResponse.isValid())))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void test_addDomain_shouldHandleDomainErrors() throws Exception {
    String message = "Test";
    AddDomainRequest addDomainRequest = DtoTestFactory.buildAddDomainRequest();
    AddDomainResponse addDomainResponse = new AddDomainResponse(500, false, message);
    Mockito
        .when(domainService.addDomain(Mockito.any(AddDomainRequest.class)))
        .thenThrow(new DomainException(message, HttpStatus.INTERNAL_SERVER_ERROR));

    mvc.perform(MockMvcRequestBuilders.post("/api/v1/domains")
            .contentType(APPLICATION_JSON)
            .content(objectWriter.writeValueAsString(addDomainRequest)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", Is.is(addDomainResponse.getStatusCode())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.valid", Is.is(addDomainResponse.isValid())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is(addDomainResponse.getMessage())))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }
}
