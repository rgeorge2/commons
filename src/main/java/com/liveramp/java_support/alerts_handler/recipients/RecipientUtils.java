package com.liveramp.java_support.alerts_handler.recipients;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import com.liveramp.java_support.alerts_handler.configs.AlertsHandlerConfig;

public class RecipientUtils {
  public static AlertRecipient compose(final Collection<AlertRecipient> recipients) {
    return compose(recipients.toArray(new AlertRecipient[recipients.size()]));
  }

  public static AlertRecipient compose(final AlertRecipient... recipients) {
    return new AlertRecipient() {
      @Override
      public void addRecipient(RecipientListBuilder recipientListBuilder, AlertsHandlerConfig alertsHandlerConfig, AddRecipientContext context) {
        for (AlertRecipient alertRecipient : recipients) {
          alertRecipient.addRecipient(recipientListBuilder, alertsHandlerConfig, context);
        }
      }

      @Override
      public String toString() {
        return "Compose{" + Joiner.on(",").join(recipients) + "}";
      }
    };
  }

  public static AlertRecipient of(final String email) {
    return new StaticEmailRecipient(email);
  }

  public static AlertRecipient of(final Collection<String> emails) {
    return compose(Collections2.transform(emails, email -> of(email)));
  }

  public static AlertRecipient of(String first, String second, String... remaining) {
    final List<String> emails = Lists.newArrayList(remaining);
    emails.add(first);
    emails.add(second);
    return of(emails);
  }

}
