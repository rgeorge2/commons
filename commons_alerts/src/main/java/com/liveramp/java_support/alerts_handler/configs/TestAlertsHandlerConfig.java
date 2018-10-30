package com.liveramp.java_support.alerts_handler.configs;

import com.liveramp.java_support.alerts_handler.AlertHelpers;
import com.liveramp.java_support.alerts_handler.recipients.AlertRecipient;

public class TestAlertsHandlerConfig implements AlertsHandlerConfig {

  private final AlertMessageConfig defaultMessageConfig;
  private final AlertRecipient engineeringRecipient;
  private final String from;

  public TestAlertsHandlerConfig(AlertMessageConfig defaultMessageConfig, String from, AlertRecipient engineeringRecipient) {
    this.from = from;
    this.defaultMessageConfig = defaultMessageConfig;
    this.engineeringRecipient = engineeringRecipient;
  }

  @Override
  public String getFromAddress() {
    return AlertHelpers.buildEmailAddress(from, "example.com");
  }

  @Override
  public AlertRecipient getEngineeringRecipient() {
    return engineeringRecipient;
  }

  @Override
  public AlertMessageConfig getDefaultMessageConfig() {
    return defaultMessageConfig;
  }

}
