package com.osa.domainsapp.service.impl;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import com.osa.domainsapp.payload.AddDomainRequest;
import com.osa.domainsapp.service.SlackService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class SlackServiceImpl implements SlackService {

  @Value("${slack.webhook.url}")
  private String webhookUrl;

  @Override
  public WebhookResponse sendMessage(String domain) {
    try {
      Payload payload = Payload.builder()
          .text(domain)
          .build();

      return Slack.getInstance().send(webhookUrl, payload);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
