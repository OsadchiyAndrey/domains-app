package com.osa.domainsapp.exception;

import org.springframework.http.HttpStatus;

public class DomainException extends BaseException {

  public DomainException(String msg) {
    super(msg);
  }

  public DomainException(String msg, HttpStatus httpStatus) {
    super(msg);
    this.httpStatus = httpStatus;
  }
}
