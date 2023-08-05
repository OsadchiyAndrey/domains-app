package com.osa.domainsapp.service.impl;

import com.osa.domainsapp.payload.AuthorizationRequest;
import com.osa.domainsapp.payload.AuthorizationResponse;
import com.osa.domainsapp.security.JwtTokenProvider;
import com.osa.domainsapp.security.SecurityUserDetails;
import com.osa.domainsapp.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

  AuthenticationManager authenticationManager;
  JwtTokenProvider jwtTokenProvider;

  @Override
  public AuthorizationResponse authenticate(AuthorizationRequest request) {
    String username = request.getUsername();
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
    String token = jwtTokenProvider.generateToken(request.getUsername());
    return new AuthorizationResponse(token, username);
  }

  @Override
  public String getUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(authentication) || authentication instanceof AnonymousAuthenticationToken) {
      return "anonymus";
    }
    return ((SecurityUserDetails) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal()).getUsername();
  }
}
