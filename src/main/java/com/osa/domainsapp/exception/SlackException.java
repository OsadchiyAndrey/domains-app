package com.osa.domainsapp.exception;

import org.springframework.http.HttpStatus;

public class SlackException extends BaseException {

  {
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public SlackException(String msg) {
    super(msg);
  }

  public SlackException(String msg, HttpStatus httpStatus) {
    super(msg);
    this.httpStatus = httpStatus;
  }
}
