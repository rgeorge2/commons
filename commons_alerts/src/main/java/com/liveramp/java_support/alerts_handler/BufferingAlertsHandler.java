package com.liveramp.java_support.alerts_handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.liveramp.java_support.alerts_handler.configs.AlertsHandlerConfig;
import com.liveramp.java_support.alerts_handler.configs.DefaultAlertMessageConfig;
import com.liveramp.java_support.alerts_handler.configs.TestAlertsHandlerConfig;
import com.liveramp.java_support.alerts_handler.recipients.AddRecipientContext;
import com.liveramp.java_support.alerts_handler.recipients.AlertRecipient;
import com.liveramp.java_support.alerts_handler.recipients.RecipientListBuilder;

public class BufferingAlertsHandler implements AlertsHandler {

  public static class MessageBuffer {

    private final List<AlertMessage> sentAlerts = new ArrayList<>();
    private final Map<String, Set<AlertMessage>> sentEmails = new HashMap<>();

    public List<AlertMessage> getSentAlerts() {
      return sentAlerts;
    }

    public Map<String, Set<AlertMessage>> getSentEmails() {
      return sentEmails;
    }
  }

  private final MessageBuffer buffer;
  private final AlertsHandlerConfig config;

  public BufferingAlertsHandler(MessageBuffer buffer, AlertRecipient recipient) {

    config = new TestAlertsHandlerConfig(new DefaultAlertMessageConfig(true, new ArrayList<>()),
        "noreply",
        recipient
    );

    this.buffer = buffer;

  }

  @Override
  public void sendAlert(AlertMessage contents, AlertRecipient recipient, AlertRecipient... additionalRecipients) {

    RecipientListBuilder recipients = new RecipientListBuilder();

    AddRecipientContext context = new AddRecipientContext(Optional.empty());
    recipient.addRecipient(recipients, config, context);
    for (AlertRecipient additionalRecipient : additionalRecipients) {
      additionalRecipient.addRecipient(recipients, config, context);
    }

    for (String emailRecipient : recipients.getEmailRecipients()) {
      synchronized (buffer) {
        buffer.sentEmails.merge(
            emailRecipient,
            new HashSet<>(Collections.singletonList(contents)),
            (acc, elem) -> {acc.addAll(elem); return acc;}
            );
      }
    }

    synchronized (buffer) {
      buffer.sentAlerts.add(contents);
    }
  }

  @Override
  public void sendAlert(String subject, String body, AlertRecipient recipient, AlertRecipient... additionalRecipients) {
    sendAlert(AlertMessages.build(subject, body), recipient, additionalRecipients);
  }

  @Override
  public void sendAlert(String subject, Throwable t, AlertRecipient recipient, AlertRecipient... additionalRecipients) {
    sendAlert(AlertMessages.build(subject, t), recipient, additionalRecipients);
  }

  @Override
  public void sendAlert(String subject, String body, Throwable t, AlertRecipient recipient, AlertRecipient... additionalRecipients) {
    AlertMessage message = AlertMessages.builder(subject)
        .setBody(body)
        .setThrowable(t)
        .build();
    sendAlert(message, recipient, additionalRecipients);
  }

  @Override
  public RecipientListBuilder resolveRecipients(List<AlertRecipient> recipients) {
    return AlertsUtil.getRecipients(recipients, config, new AddRecipientContext(Optional.empty()));
  }

}
