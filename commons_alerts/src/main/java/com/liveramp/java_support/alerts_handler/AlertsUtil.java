package com.liveramp.java_support.alerts_handler;

import java.util.List;

import com.liveramp.java_support.alerts_handler.configs.AlertsHandlerConfig;
import com.liveramp.java_support.alerts_handler.recipients.AddRecipientContext;
import com.liveramp.java_support.alerts_handler.recipients.AlertRecipient;
import com.liveramp.java_support.alerts_handler.recipients.RecipientListBuilder;

public class AlertsUtil {

  public static RecipientListBuilder getRecipients(List<AlertRecipient> recipients, AlertsHandlerConfig config, AddRecipientContext addRecipientContext){
    final RecipientListBuilder recipientListBuilder = new RecipientListBuilder();

    for (AlertRecipient recipient : recipients) {
      recipient.addRecipient(recipientListBuilder, config, addRecipientContext);
    }

    return recipientListBuilder;
  }

}
