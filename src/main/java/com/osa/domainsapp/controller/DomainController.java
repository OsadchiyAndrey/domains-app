package com.osa.domainsapp.controller;

import com.osa.domainsapp.payload.AddDomainRequest;
import com.osa.domainsapp.payload.AddDomainResponse;
import com.osa.domainsapp.service.DomainService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/domains")
@AllArgsConstructor
public class DomainController {

  private final DomainService domainService;

  @PostMapping
  @Operation(summary = "Add domain", description = "Endpoint to add domain to db and send slack message")
  public ResponseEntity<AddDomainResponse> addDomain(@Valid @RequestBody AddDomainRequest request) {
    AddDomainResponse response = domainService.addDomain(request);
    return ResponseEntity.ok(response);
  }
}
