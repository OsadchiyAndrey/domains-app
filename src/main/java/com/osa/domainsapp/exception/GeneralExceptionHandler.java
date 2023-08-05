package com.osa.domainsapp.exception;

import com.osa.domainsapp.payload.AddDomainResponse;
import com.osa.domainsapp.payload.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Log4j2
@RestControllerAdvice
public class GeneralExceptionHandler {

  public static final String ERROR_MESSAGE_TEMPLATE = "message: %s %n requested uri: %s";

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResponse> handleBaseApiException(BaseException e, WebRequest request) {
    return getErrorResponseResponseEntity(request, e.getHttpStatus(), e.getMessage(),
            e.getClass().getSimpleName(), ExceptionUtils.getStackTrace(e));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e, WebRequest request) {
    final HttpStatus status = getStatusForAllExceptions(e);
    final String path = request.getDescription(false);
    log.error(String.format(ERROR_MESSAGE_TEMPLATE, e.getMessage(), path), e);
    return getErrorResponseResponseEntity(request, status, e.getMessage(),
            e.getClass().getSimpleName(), ExceptionUtils.getStackTrace(e));
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e, WebRequest request) {
    HttpStatus status = HttpStatus.UNAUTHORIZED;
    final String localizedMessage = e.getLocalizedMessage();
    final String path = request.getDescription(false);
    String message = (StringUtils.isNotEmpty(localizedMessage) ? localizedMessage : status.getReasonPhrase());
    log.error(String.format(ERROR_MESSAGE_TEMPLATE, message, path), e);
    return getErrorResponseResponseEntity(request, status, e.getMessage(), e.getClass().getSimpleName(), ExceptionUtils.getStackTrace(e));
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<AddDomainResponse> handleDomainValidation(DomainException ex) {
    return ResponseEntity.ok(new AddDomainResponse(ex.getHttpStatus().value(), false, ex.getMessage()));
  }

  private ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(WebRequest request, HttpStatus status,
                                                                       String message, String simpleName,
                                                                       String stackTrace) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(status);
    errorResponse.setMessage(message);
    errorResponse.setType(simpleName);
    errorResponse.setUrl(getUrlFromWebRequest(request));
    errorResponse.setStackTrace(stackTrace);
    return new ResponseEntity<>(errorResponse, status);

  }

  private HttpStatus getStatusForAllExceptions(Exception e) {
    ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
    return responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
  }

  private static String getUrlFromWebRequest(WebRequest request) {
    return ((ServletWebRequest) request).getRequest().getRequestURI();
  }
}
