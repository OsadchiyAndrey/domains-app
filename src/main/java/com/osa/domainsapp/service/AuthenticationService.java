package com.osa.domainsapp.service;

import com.osa.domainsapp.payload.AuthorizationRequest;
import com.osa.domainsapp.payload.AuthorizationResponse;

public interface AuthenticationService {

  AuthorizationResponse authenticate(AuthorizationRequest request);

  String getUsername();
}
