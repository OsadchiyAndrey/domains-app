package com.osa.domainsapp.controller;

import com.osa.domainsapp.payload.AuthorizationRequest;
import com.osa.domainsapp.payload.AuthorizationResponse;
import com.osa.domainsapp.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/authentication")
@AllArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping(value = "/authenticate")
  @Operation(summary = "Authentication request", description = "Endpoint for authorization and getting jwt token")
  public ResponseEntity<AuthorizationResponse> createAuthenticationToken(@RequestBody AuthorizationRequest request) {
    AuthorizationResponse authorizationResponse = authenticationService.authenticate(request);
    return ResponseEntity.ok(authorizationResponse);
  }

  @GetMapping("/get-me")
  @Operation(summary = "Authentication check", description = "Endpoint to check user authentication")
  public String isAuthenticated() {
    return authenticationService.getUsername();
  }

}
