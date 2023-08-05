package com.osa.domainsapp.service;

import com.github.seratch.jslack.api.webhook.WebhookResponse;

public interface SlackService {

  WebhookResponse sendMessage(String domain);
}
