package com.osa.domainsapp.service;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import com.osa.domainsapp.service.impl.SlackServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class SlackServiceImplTest {

  @Mock
  private Slack slack;
  @Mock
  private WebhookResponse webhookResponse;
  @InjectMocks
  private SlackServiceImpl slackService;

  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(slackService, "webhookUrl", "https://test.com/");
  }

  @Test
  public void test_sendMessage_success() throws IOException {
    String domain = "example.com";

    MockedStatic<Slack> slackMockedStatic = Mockito.mockStatic(Slack.class);
    slackMockedStatic.when(() -> Slack.getInstance()).thenReturn(slack);
    Mockito.when(slack.send(Mockito.anyString(), Mockito.any(Payload.class))).thenReturn(webhookResponse);

    WebhookResponse response = slackService.sendMessage(domain);

    Assertions.assertEquals(webhookResponse, response);
  }
}
