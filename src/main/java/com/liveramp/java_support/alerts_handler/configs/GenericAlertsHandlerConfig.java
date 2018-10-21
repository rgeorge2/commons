package com.liveramp.java_support.alerts_handler.configs;

import com.liveramp.java_support.alerts_handler.AlertHelpers;
import com.liveramp.java_support.alerts_handler.recipients.AlertRecipient;

public class GenericAlertsHandlerConfig implements AlertsHandlerConfig {
  private final AlertMessageConfig defaultMessageConfig;
  private final AlertRecipient engineeringRecipient;
  private final String fromList;
  private final String fromDomain;

  public GenericAlertsHandlerConfig(AlertMessageConfig defaultMessageConfig,
                                    String fromList,
                                    String fromDomain,
                                    AlertRecipient engineeringRecipient) {
    this.fromList = fromList;
    this.fromDomain = fromDomain;
    this.defaultMessageConfig = defaultMessageConfig;
    this.engineeringRecipient = engineeringRecipient;
  }

  @Override
  public String getFromAddress() {
    return AlertHelpers.buildEmailAddress(fromList, fromDomain);
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
