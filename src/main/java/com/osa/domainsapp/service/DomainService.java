package com.osa.domainsapp.service;

import com.github.seratch.jslack.api.webhook.WebhookResponse;
import com.osa.domainsapp.payload.AddDomainRequest;
import com.osa.domainsapp.payload.AddDomainResponse;

public interface DomainService {

  AddDomainResponse addDomain(AddDomainRequest request);
}
