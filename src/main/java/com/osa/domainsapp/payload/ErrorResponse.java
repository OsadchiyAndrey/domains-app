package com.osa.domainsapp.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {

  @JsonProperty("status")
  private HttpStatus status;

  @JsonProperty("message")
  private String message;


  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @JsonProperty("time")
  private LocalDateTime timestamp;

  @JsonProperty("url")
  private String url;

  @JsonProperty("type")
  private String type;

  @JsonProperty("stack_trace")
  private String stackTrace;

  public ErrorResponse() {
    timestamp = LocalDateTime.now();
  }
}
