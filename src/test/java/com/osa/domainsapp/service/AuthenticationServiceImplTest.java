package com.osa.domainsapp.service;

import com.osa.domainsapp.DtoTestFactory;
import com.osa.domainsapp.payload.AuthorizationRequest;
import com.osa.domainsapp.payload.AuthorizationResponse;
import com.osa.domainsapp.security.JwtTokenProvider;
import com.osa.domainsapp.security.SecurityUserDetails;
import com.osa.domainsapp.service.impl.AuthenticationServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

  @Mock
  private AuthenticationManager authenticationManager;
  @Mock
  private JwtTokenProvider jwtTokenProvider;
  @Mock
  private Authentication authentication;
  @Mock
  private SecurityContext securityContext;
  @Mock
  private SecurityUserDetails securityUserDetails;
  @Mock
  private AnonymousAuthenticationToken anonymousAuthenticationToken;
  @InjectMocks
  private AuthenticationServiceImpl authenticationService;

  @Test
  public void test_authenticate_success() {
    String token = RandomStringUtils.randomAlphabetic(10);
    AuthorizationRequest authorizationRequest = DtoTestFactory.buildAuthorizationRequest();
    Mockito.when(jwtTokenProvider.generateToken(Mockito.anyString())).thenReturn(token);
    AuthorizationResponse authenticate = authenticationService.authenticate(authorizationRequest);

    Assertions.assertEquals(authorizationRequest.getUsername(), authenticate.getUsername());
    Assertions.assertEquals(token, authenticate.getToken());
  }

  @Test
  public void test_getUsername_returnCurrentAuthUser() {
    String expected = RandomStringUtils.randomAlphabetic(10);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    Mockito.when(authentication.getPrincipal()).thenReturn(securityUserDetails);
    Mockito.when(securityUserDetails.getUsername()).thenReturn(expected);
    SecurityContextHolder.setContext(securityContext);
    String username = authenticationService.getUsername();
    Assertions.assertEquals(expected, username);
  }

  @Test
  public void test_getUsername_returnAnonymusIfAuthIsNull() {
    String expected = "anonymus";
    Mockito.when(securityContext.getAuthentication()).thenReturn(null);
    SecurityContextHolder.setContext(securityContext);
    String username = authenticationService.getUsername();
    Assertions.assertEquals(expected, username);
  }

  @Test
  public void test_getUsername_returnAnonymusIfTokenAnonymus() {
    String expected = "anonymus";
    Mockito.when(securityContext.getAuthentication()).thenReturn(anonymousAuthenticationToken);
    SecurityContextHolder.setContext(securityContext);
    String username = authenticationService.getUsername();
    Assertions.assertEquals(expected, username);
  }
}
