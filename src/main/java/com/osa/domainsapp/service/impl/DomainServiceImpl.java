package com.osa.domainsapp.service.impl;

import com.github.seratch.jslack.api.webhook.WebhookResponse;
import com.osa.domainsapp.dao.DomainRepository;
import com.osa.domainsapp.entity.DomainEntity;
import com.osa.domainsapp.exception.DomainException;
import com.osa.domainsapp.payload.AddDomainRequest;
import com.osa.domainsapp.payload.AddDomainResponse;
import com.osa.domainsapp.service.DomainService;
import com.osa.domainsapp.service.SlackService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DomainServiceImpl implements DomainService {

  SlackService slackService;
  DomainRepository domainRepository;
  RestTemplate restTemplate;

  @Override
  public AddDomainResponse addDomain(AddDomainRequest request) {
    String domain = request.getDomain();

    domainRepository.findByDomain(domain).ifPresent(domainEntity -> {
      throw new DomainException("Domain " + domain + " already exists", HttpStatus.CONFLICT);
    });

    log.info("Received new domain: {}", domain);

    int statusCode = getStatusCode(domain);

    DomainEntity domainEntity = new DomainEntity(domain);
    DomainEntity savedDomain = domainRepository.save(domainEntity);

    slackService.sendMessage(savedDomain.getDomain());
    return new AddDomainResponse(statusCode, true);
  }

  private int getStatusCode(String domain) {
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(domain, String.class);
      log.info("Domain valid {}", response.getStatusCode());
      return response.getStatusCode().value();
    } catch (HttpServerErrorException.BadGateway e) {
      log.info("Domain valid {}", HttpStatus.BAD_GATEWAY);
      return HttpStatus.BAD_GATEWAY.value();
    } catch (HttpStatusCodeException e) {
      HttpStatus httpStatus = HttpStatus.valueOf(e.getStatusCode().value());
      log.info("Domain invalid {}", httpStatus);
      throw new DomainException(e.getMessage(), httpStatus);
    }
  }
}
