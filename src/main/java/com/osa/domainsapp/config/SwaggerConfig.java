package com.osa.domainsapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Collections;
import java.util.Properties;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig {

  public static final String AUTHORIZATION_HEADER = "Authorization";

  @Bean
  public OpenAPI openApi() {

    SecurityRequirement securityRequirement = new SecurityRequirement()
        .addList(AUTHORIZATION_HEADER);
    Components components = new Components()
        .addSecuritySchemes(AUTHORIZATION_HEADER, new SecurityScheme()
            .name(AUTHORIZATION_HEADER)
            .in(SecurityScheme.In.HEADER)
            .type(SecurityScheme.Type.APIKEY));

    return new OpenAPI()
        .components(components)
        .security(Collections.singletonList(securityRequirement))
        .info(getApiInfo());
  }

  private Info getApiInfo() {
    return new Info().contact(getContact());
  }

  private Contact getContact() {
    return new Contact().name("Andrii Osadchyi")
        .email("osadchiy.andre@gmail.com");
  }
}
