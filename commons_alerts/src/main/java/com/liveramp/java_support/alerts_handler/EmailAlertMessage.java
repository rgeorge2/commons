package com.liveramp.java_support.alerts_handler;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.liveramp.java_support.alerts_handler.configs.AlertMessageConfig;
import com.liveramp.java_support.alerts_handler.configs.DefaultOverrideAlertMessageConfig;
import com.liveramp.java_support.alerts_handler.configs.OverrideAlertMessageConfig;

public class EmailAlertMessage implements AlertMessage {
  final String subject;
  final Optional<String> body;
  final Optional<Throwable> throwable;
  final Optional<OverrideAlertMessageConfig> alertMessageConfig;

  public EmailAlertMessage(String subject, Optional<String> body, Optional<Throwable> throwable, Optional<OverrideAlertMessageConfig> alertMessageConfig) {
    this.subject = subject;
    this.body = body;
    this.throwable = throwable;
    this.alertMessageConfig = alertMessageConfig;
  }

  @Override
  public String getSubject(AlertMessageConfig alertMessageConfig) {
    final List<String> tags = getConfigOverrides().overrideConfig(alertMessageConfig).getTags();
    return String.format("%s%s", AlertHelpers.stringifyTags(tags), subject);
  }

  @Override
  public String getMessage(AlertMessageConfig alertMessageConfig) {
    final AlertMessageConfig overriddenConfig = getConfigOverrides().overrideConfig(alertMessageConfig);
    final StringBuilder messageBody = new StringBuilder();

    if (body.isPresent()) {
      if (overriddenConfig.isAllowHtml()) {
        messageBody.append(body.get().replace("\n", "<br />"));
      } else {
        messageBody.append(body.get());
      }
    }

    if (throwable.isPresent()) {
      if (overriddenConfig.isAllowHtml()) {
        messageBody
            .append("<h4>Stack Trace:</h4><hr /><pre>")
            .append(ExceptionUtils.getStackTrace(throwable.get()))
            .append("</pre>");
      } else {
        messageBody
            .append("\n\nStack Trace:\n")
            .append(ExceptionUtils.getStackTrace(throwable.get()));
      }
    }

    return messageBody.toString();
  }

  @Override
  public OverrideAlertMessageConfig getConfigOverrides() {
    if (alertMessageConfig.isPresent()) {
      return alertMessageConfig.get();
    } else {
      return new DefaultOverrideAlertMessageConfig.Builder().build();
    }
  }

  public static void main(String[] args) {

    System.out.println();

  }


  @Override
  public String toString() {
    return "EmailAlertMessage{" +
        "subject='" + subject + '\'' +
        ", body=" + body +
        ", throwable=" + throwable +
        ", alertMessageConfig=" + alertMessageConfig +
        '}';
  }
}
