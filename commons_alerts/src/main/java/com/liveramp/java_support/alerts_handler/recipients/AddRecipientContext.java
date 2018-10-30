package com.liveramp.java_support.alerts_handler.recipients;

import java.util.Optional;

public class AddRecipientContext {
  private final Optional<AlertSeverity> alertSeverityOverride;

  public AddRecipientContext(Optional<AlertSeverity> alertSeverityOverride) {
    this.alertSeverityOverride = alertSeverityOverride;
  }

  public Optional<AlertSeverity> getAlertSeverityOverride() {
    return alertSeverityOverride;
  }
}
