package com.liveramp.java_support.alerts_handler.recipients;

import java.util.ArrayList;
import java.util.List;

public class RecipientListBuilder {
  private List<String> emailRecipients = new ArrayList<>();

  public RecipientListBuilder() { }

  public void addEmailRecipient(String recipient) {
    emailRecipients.add(recipient);
  }

  public List<String> getEmailRecipients() {
    return new ArrayList<>(emailRecipients);
  }
}
