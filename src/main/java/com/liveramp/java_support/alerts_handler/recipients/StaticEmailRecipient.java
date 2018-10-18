package com.liveramp.java_support.alerts_handler.recipients;

import com.liveramp.java_support.alerts_handler.configs.AlertsHandlerConfig;

class StaticEmailRecipient implements AlertRecipient {
  private final String recipient;

  public StaticEmailRecipient(String recipient) {
    this.recipient = recipient;
  }

  @Override
  public void addRecipient(RecipientListBuilder recipientListBuilder, AlertsHandlerConfig config, AddRecipientContext addRecipientContext) {
    recipientListBuilder.addEmailRecipient(recipient);
  }

  @Override
  public String toString() {
    return "StaticEmailRecipient{" +
        "recipient='" + recipient + '\'' +
        '}';
  }
}
