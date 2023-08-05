package com.osa.domainsapp.security;

import com.osa.domainsapp.exception.AuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Log4j2
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String token = jwtTokenProvider.resolveToken(request);
    try {
      validateToken(token);
    } catch (AuthenticationException e) {
      sendError(response, e);
    }
    chain.doFilter(request, response);
  }

  private void validateToken(String token) {
    if (token != null && jwtTokenProvider.validateToken(token)) {
      Authentication authentication = jwtTokenProvider.getAuthentication(token);
      if (authentication != null) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
  }

  private void sendError(HttpServletResponse response, AuthenticationException e) throws IOException {
    SecurityContextHolder.clearContext();
    response.sendError(e.getHttpStatus().value());
  }
}
