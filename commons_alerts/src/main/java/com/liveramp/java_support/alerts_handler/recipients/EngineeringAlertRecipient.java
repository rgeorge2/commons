package com.liveramp.java_support.alerts_handler.recipients;

import java.util.Optional;

import com.liveramp.java_support.alerts_handler.configs.AlertsHandlerConfig;

public class EngineeringAlertRecipient implements AlertRecipient {
  private final AlertSeverity severity;

  public EngineeringAlertRecipient(AlertSeverity severity) {
    this.severity = severity;
  }

  @Override
  public void addRecipient(RecipientListBuilder recipientListBuilder, AlertsHandlerConfig alertsHandlerConfig, AddRecipientContext context) {
    final AlertRecipient engineeringRecipient = alertsHandlerConfig.getEngineeringRecipient();
    final AddRecipientContext contextWithSeverity = new AddRecipientContext(Optional.of(severity));

    engineeringRecipient.addRecipient(recipientListBuilder, alertsHandlerConfig, contextWithSeverity);
  }

  @Override
  public String toString() {
    return "Engineering{" + severity.toString() + "}";
  }
}
