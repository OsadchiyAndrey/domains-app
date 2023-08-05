package com.osa.domainsapp.config;

import com.osa.domainsapp.security.JwtConfigurer;
import com.osa.domainsapp.security.JwtUnAuthorizedResponseAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;

  private final UserDetailsService userDetailsService;
  private final JwtConfigurer jwtConfigurer;

  public SecurityConfig(JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint,
                        @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                        JwtConfigurer jwtConfigurer) {
    this.jwtUnAuthorizedResponseAuthenticationEntryPoint = jwtUnAuthorizedResponseAuthenticationEntryPoint;
    this.userDetailsService = userDetailsService;
    this.jwtConfigurer = jwtConfigurer;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder)
      throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder)
        .and().build();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf().disable()
        .authorizeHttpRequests(authorize ->
            authorize
                .requestMatchers(HttpMethod.POST, "/api/v1/authentication/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/form-results")
                .permitAll()
                .requestMatchers("/configuration/**", "/swagger-ui/**",
                    "/v3/api-docs/**", "/webjars/**")
                .permitAll()
                .requestMatchers("/bars/pk").permitAll()
                .requestMatchers(HttpMethod.OPTIONS)
                .permitAll())
        .exceptionHandling()
        .authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint)
        .and()
        .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .apply(jwtConfigurer)
        .and()
        .headers().frameOptions().sameOrigin()
        .cacheControl();
    return http.build();
  }
}
