package com.osa.domainsapp.service;

import com.osa.domainsapp.DtoTestFactory;
import com.osa.domainsapp.EntityTestFactory;
import com.osa.domainsapp.dao.DomainRepository;
import com.osa.domainsapp.entity.DomainEntity;
import com.osa.domainsapp.exception.DomainException;
import com.osa.domainsapp.payload.AddDomainRequest;
import com.osa.domainsapp.payload.AddDomainResponse;
import com.osa.domainsapp.service.impl.DomainServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;

@ExtendWith(MockitoExtension.class)
public class DomainServiceImplTest {

  @Mock
  private SlackService slackService;
  @Mock
  private DomainRepository domainRepository;
  @Mock
  private RestTemplate restTemplate;
  @InjectMocks
  private DomainServiceImpl domainService;

  @Test
  public void test_add_domain_success() {
    AddDomainRequest addDomainRequest = DtoTestFactory.buildAddDomainRequest();
    DomainEntity domainEntity = EntityTestFactory.buildDomainEntity();
    ResponseEntity<String> mockResponseEntity = new ResponseEntity<>("Success", HttpStatus.OK);

    Mockito.when(domainRepository.findByDomain(Mockito.anyString())).thenReturn(Optional.empty());
    Mockito.when(domainRepository.save(Mockito.any())).thenReturn(domainEntity);
    Mockito.when(slackService.sendMessage(Mockito.anyString())).thenReturn(null);
    Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any(Class.class)))
        .thenReturn(mockResponseEntity);

    AddDomainResponse response = domainService.addDomain(addDomainRequest);

    Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assertions.assertTrue(response.isValid());

    Mockito.verify(slackService).sendMessage(addDomainRequest.getDomain());
    Mockito.verify(domainRepository).findByDomain(addDomainRequest.getDomain());
    Mockito.verify(domainRepository).save(Mockito.any(DomainEntity.class));
  }

  @Test
  public void test_addDomain_shouldThrowDomainExceptionOnDuplicate() {
    AddDomainRequest addDomainRequest = DtoTestFactory.buildAddDomainRequest();
    DomainEntity domainEntity = EntityTestFactory.buildDomainEntity();
    Mockito.when(domainRepository.findByDomain(Mockito.anyString())).thenReturn(Optional.of(domainEntity));
    Assertions.assertThrows(DomainException.class, () -> domainService.addDomain(addDomainRequest));
  }

  @Test
  public void test_addDomain_shouldThrowDomainExceptionOnErrorCode() {
    AddDomainRequest addDomainRequest = DtoTestFactory.buildAddDomainRequest();
    HttpServerErrorException.GatewayTimeout mock = Mockito.mock(HttpServerErrorException.GatewayTimeout.class);
    Mockito.when(mock.getStatusCode()).thenReturn(GATEWAY_TIMEOUT);

    Mockito.when(domainRepository.findByDomain(Mockito.anyString())).thenReturn(Optional.empty());
    Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any(Class.class)))
        .thenThrow(mock);

    Assertions.assertThrows(DomainException.class, () -> domainService.addDomain(addDomainRequest));
  }

  @Test
  public void test_addDomain_successOn502Error() {
    AddDomainRequest addDomainRequest = DtoTestFactory.buildAddDomainRequest();
    DomainEntity domainEntity = EntityTestFactory.buildDomainEntity();

    Mockito.when(domainRepository.save(Mockito.any())).thenReturn(domainEntity);
    Mockito.when(slackService.sendMessage(Mockito.anyString())).thenReturn(null);
    HttpServerErrorException.BadGateway mock = Mockito.mock(HttpServerErrorException.BadGateway.class);
    Mockito.when(domainRepository.findByDomain(Mockito.anyString())).thenReturn(Optional.empty());
    Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any(Class.class)))
        .thenThrow(mock);

    AddDomainResponse response = domainService.addDomain(addDomainRequest);

    Assertions.assertEquals(HttpStatus.BAD_GATEWAY.value(), response.getStatusCode());
    Assertions.assertTrue(response.isValid());

    Mockito.verify(slackService).sendMessage(addDomainRequest.getDomain());
    Mockito.verify(domainRepository).findByDomain(addDomainRequest.getDomain());
    Mockito.verify(domainRepository).save(Mockito.any(DomainEntity.class));
  }
}
