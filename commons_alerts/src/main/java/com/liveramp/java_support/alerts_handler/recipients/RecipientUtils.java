package com.liveramp.java_support.alerts_handler.recipients;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return "Compose{" + Stream.of(recipients).map(Object::toString).collect(Collectors.joining(",")) + "}";
      }
    };
  }

  public static AlertRecipient of(final String email) {
    return new StaticEmailRecipient(email);
  }

  public static AlertRecipient of(final Collection<String> emails) {
    return compose(emails.stream()
        .map(RecipientUtils::of)
        .collect(Collectors.toList())
        .toArray(new AlertRecipient[]{}));
  }

  public static AlertRecipient of(String first, String second, String... remaining) {
    final List<String> emails = Stream.of(remaining).collect(Collectors.toList());
    emails.add(first);
    emails.add(second);
    return of(emails);
  }

}
