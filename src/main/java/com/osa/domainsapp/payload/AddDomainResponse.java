package com.osa.domainsapp.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddDomainResponse {

  int statusCode;
  boolean valid;
  String message;

  public AddDomainResponse(int statusCode, boolean valid) {
    this.statusCode = statusCode;
    this.valid = valid;
  }
}
