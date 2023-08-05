package com.osa.domainsapp;

import com.github.seratch.jslack.api.webhook.WebhookResponse;
import com.osa.domainsapp.payload.AddDomainRequest;
import com.osa.domainsapp.payload.AddDomainResponse;
import com.osa.domainsapp.payload.AuthorizationRequest;
import com.osa.domainsapp.payload.AuthorizationResponse;
import org.apache.commons.lang3.RandomStringUtils;

public class DtoTestFactory {

  public static AuthorizationRequest buildAuthorizationRequest() {
    AuthorizationRequest authorizationRequest = new AuthorizationRequest();
    authorizationRequest.setPassword(RandomStringUtils.randomAlphabetic(10));
    authorizationRequest.setUsername(RandomStringUtils.randomAlphabetic(5));
    return authorizationRequest;
  }

  public static AuthorizationResponse buildAuthorizationResponse() {
    AuthorizationResponse authorizationResponse = new AuthorizationResponse();
    authorizationResponse.setUsername(RandomStringUtils.randomAlphabetic(5));
    authorizationResponse.setToken(RandomStringUtils.randomAlphabetic(30));
    return authorizationResponse;
  }

  public static AddDomainRequest buildAddDomainRequest() {
    AddDomainRequest addDomainRequest = new AddDomainRequest();
    addDomainRequest.setDomain("http://test.com");
    return addDomainRequest;
  }

  public static AddDomainResponse buildAddDomainResponse() {
    return new AddDomainResponse(200, true);
  }
}
